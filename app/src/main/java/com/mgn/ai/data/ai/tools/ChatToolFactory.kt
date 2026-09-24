package com.mgn.ai.data.ai.tools

import android.util.Log
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import com.mgn.ai.ai.core.Tool
import com.mgn.ai.ai.provider.BuiltInTools
import com.mgn.ai.ai.provider.Model
import com.mgn.ai.data.ai.mcp.McpManager
import com.mgn.ai.data.ai.subagent.SubAgentEngine
import com.mgn.ai.data.ai.subagent.buildSubAgentTool
import com.mgn.ai.data.ai.subagent.filterSubAgentTools
import com.mgn.ai.data.datastore.findModelById
import com.mgn.ai.knowledge.KnowledgeManager
import com.mgn.ai.knowledge.tool.KnowledgeSearchTool
import com.mgn.ai.data.datastore.findProvider
import com.mgn.ai.data.ai.tools.local.LocalTools
import com.mgn.ai.data.datastore.Settings
import com.mgn.ai.data.datastore.SettingsStore
import com.mgn.ai.data.files.SkillManager
import com.mgn.ai.data.model.Assistant
import com.mgn.ai.data.repository.ConversationRepository
import com.mgn.ai.data.repository.MemoryRepository
import com.mgn.ai.data.repository.WorkspaceRepository
import com.mgn.ai.workspace.WorkspaceShellStatus
import kotlin.uuid.Uuid

private const val TAG = "ChatToolFactory"

internal fun shouldUseExternalWebSearch(assistant: Assistant, model: Model): Boolean {
    return assistant.enableWebSearch && BuiltInTools.Search !in model.tools
}

class InvalidMcpServerNamesException(val names: List<String>) :
    IllegalStateException("Invalid MCP server names: ${names.joinToString(", ")}")

/** Creates the complete tool set for one generation run, including approval resumption. */
class ChatToolFactory(
    private val json: Json,
    private val memoryRepository: MemoryRepository,
    private val conversationRepository: ConversationRepository,
    private val localTools: LocalTools,
    private val mcpManager: McpManager,
    private val skillManager: SkillManager,
    private val workspaceRepository: WorkspaceRepository,
    private val settingsStore: SettingsStore,
    private val subAgentEngine: SubAgentEngine,
    private val todoStorage: TodoStorage,
    private val knowledgeManager: KnowledgeManager,
    private val workflowRepository: com.mgn.ai.workflow.repository.WorkflowRepository,
    private val workflowEngine: com.mgn.ai.workflow.execution.WorkflowEngine,
) {
    suspend fun createTools(
        settings: Settings,
        assistant: Assistant,
        model: Model,
        workspaceCwd: String? = null,
        conversationId: Uuid? = null,
    ): List<Tool> {
        val assembled = buildList {
        if (assistant.enableMemory) {
            val memoryAssistantId = if (assistant.useGlobalMemory) {
                MemoryRepository.GLOBAL_MEMORY_ID
            } else {
                assistant.id.toString()
            }
            addAll(
                buildMemoryTools(
                    json = json,
                    onCreation = { content -> memoryRepository.addMemory(memoryAssistantId, content) },
                    onUpdate = { id, content -> memoryRepository.updateContent(id, content) },
                    onDelete = { id -> memoryRepository.deleteMemory(id) },
                )
            )
        }
        if (shouldUseExternalWebSearch(assistant, model)) {
            addAll(createSearchTools(settings))
        }
        addAll(localTools.getTools(assistant.localTools))
        if (assistant.enableRecentChatsReference) {
            addAll(createConversationTools(conversationRepository, assistant.id))
        }
        addAll(createWorkspaceToolsIfReady(assistant.workspaceId?.toString(), workspaceCwd))
        if (assistant.enabledSkills.isNotEmpty()) {
            addAll(
                createSkillTools(
                    enabledSkills = assistant.enabledSkills,
                    allSkills = skillManager.listSkills(),
                )
            )
        }
        addAll(createSkillManageTools(skillManager))
        addAll(createMcpManageTools(mcpManager, settingsStore))
        if (settings.enableTodoList && conversationId != null) {
            add(createTodoTool(conversationId.toString(), todoStorage))
        }
        if (assistant.knowledgeBaseIds.isNotEmpty()) {
            val knowledgeTool = KnowledgeSearchTool(
                knowledgeManager = knowledgeManager,
                getAllowedKnowledgeBaseIds = { assistant.knowledgeBaseIds.map { it.toString() }.toSet() },
                getEmbeddingForBase = { null },
                getReranker = { null },
            )
            add(knowledgeTool.create())
            add(knowledgeTool.createListTool())
        }
        val mcpTools = mcpManager.getAllAvailableTools()
        val invalidNames = mcpTools
            .map { it.second }
            .distinct()
            .filter { name -> name.isEmpty() || !name.all { it in 'a'..'z' || it in 'A'..'Z' || it in '0'..'9' } }
        if (invalidNames.isNotEmpty()) {
            throw InvalidMcpServerNamesException(invalidNames)
        }
        mcpTools.forEach { (serverId, serverName, tool) ->
            add(
                Tool(
                    name = "mcp__${serverName}__${tool.name}",
                    description = tool.description ?: "",
                    parameters = { tool.inputSchema },
                    needsApproval = { tool.needsApproval },
                    execute = { mcpManager.callTool(serverId, tool.name, it.jsonObject) },
                )
            )
        }
        val workflowTools = buildList {
            add(
                com.mgn.ai.workflow.tools.workflowCreateTool(
                    repository = workflowRepository,
                    knownToolNamesProvider = { assembled.map { it.name } },
                )
            )
            add(com.mgn.ai.workflow.tools.workflowListTool(workflowRepository))
            add(com.mgn.ai.workflow.tools.workflowGetTool(workflowRepository))
            add(
                com.mgn.ai.workflow.tools.workflowUpdateTool(
                    repository = workflowRepository,
                    knownToolNamesProvider = { assembled.map { it.name } },
                )
            )
            add(com.mgn.ai.workflow.tools.workflowDeleteTool(workflowRepository))
            add(com.mgn.ai.workflow.tools.workflowSetEnabledTool(workflowRepository))
            add(com.mgn.ai.workflow.tools.workflowRunTool(workflowEngine, workflowRepository))
        }
        val withWorkflows = assembled + workflowTools
        // Sub-agent gate: no sub-agent model selected = dispatch_subagent is not
        // exposed at all (not even its schema).
        val subAgentModelId = settings.subAgentModelId ?: return withWorkflows
        val subAgentModel = settings.findModelById(subAgentModelId)
        val subAgentProvider = subAgentModel?.findProvider(settings.providers, checkOverwrite = false)
            ?.takeIf { it.enabled }
        if (subAgentModel == null || subAgentProvider == null) {
            Log.w(
                TAG,
                "createTools: sub-agent model $subAgentModelId not usable, sub-agent disabled"
            )
            return withWorkflows
        }
        val subTools = filterSubAgentTools(withWorkflows)
        return withWorkflows + buildSubAgentTool(
            engine = subAgentEngine,
            model = subAgentModel,
            settings = settings,
            tools = subTools,
            reasoningLevel = settings.subAgentReasoningLevel,
            unavailableReason = if (subTools.isEmpty()) {
                describeEmptySubAgentToolset(assistant = assistant, chatModel = model)
            } else {
                null
            },
        )
    }

    /**
     * Human-readable reason when the sub-agent is enabled but no tools pass
     * the filter this round. Adapted from ROCL (AGPL-3.0, same license).
     */
    private fun describeEmptySubAgentToolset(assistant: Assistant, chatModel: Model): String {
        val reasons = buildList {
            add(
                if (assistant.workspaceId == null) {
                    "No workspace attached, so there are no file tools"
                } else {
                    "Workspace unavailable (missing, or rootfs not READY), so there are no file tools"
                }
            )
            if (!assistant.enableWebSearch) {
                add("Web search is not enabled for this assistant")
            } else if (BuiltInTools.Search in chatModel.tools) {
                add("The chat model has built-in search, so no external search tool is attached")
            }
        }
        return reasons.joinToString("; ")
    }

    private suspend fun createWorkspaceToolsIfReady(workspaceId: String?, cwd: String?): List<Tool> {
        if (workspaceId.isNullOrBlank()) return emptyList()
        val workspace = workspaceRepository.getById(workspaceId) ?: return emptyList()
        if (workspace.shellStatus != WorkspaceShellStatus.READY.name) {
            Log.d(
                TAG,
                "createWorkspaceToolsIfReady: skip workspace tools, workspace=$workspaceId, status=${workspace.shellStatus}"
            )
            return emptyList()
        }
        return createWorkspaceTools(workspaceId, workspaceRepository, cwd)
    }
}
