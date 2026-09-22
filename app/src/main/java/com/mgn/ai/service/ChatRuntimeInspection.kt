package com.mgn.ai.service

import com.mgn.ai.ai.core.MessageRole
import com.mgn.ai.ai.provider.TextRequestPreview
import kotlinx.serialization.json.JsonObject

/**
 * Dry-run snapshot of a conversation's runtime state for the chat runtime
 * inspector (prompt preview, context variables, provider payload preview).
 *
 * Adapted from rikkahub-lune (AGPL-3.0, same license).
 */
data class ChatPromptPreviewMessage(
    val role: MessageRole,
    val content: String,
    val tokenEstimate: Int,
)

data class ChatRuntimeInspection(
    val assistantName: String,
    val modelName: String,
    val promptMessages: List<ChatPromptPreviewMessage>,
    val promptTokenEstimate: Int,
    val contextVariables: JsonObject,
    val payloadPreview: TextRequestPreview,
)
