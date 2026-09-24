package com.mgn.ai.ui.pages.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import com.mgn.ai.ai.core.ReasoningLevel
import com.mgn.ai.ai.provider.Model
import com.mgn.ai.ai.provider.ModelType
import com.mgn.ai.ai.provider.ProviderSetting
import me.rerere.hugeicons.HugeIcons
import me.rerere.hugeicons.stroke.AiBrain01
import me.rerere.hugeicons.stroke.AiEditing
import me.rerere.hugeicons.stroke.ArrowRight01
import com.mgn.ai.R
import com.mgn.ai.data.ai.prompts.PromptOptimizeDepth
import com.mgn.ai.data.ai.prompts.PromptOptimizeScene
import com.mgn.ai.data.ai.prompts.defaultPromptOptimizePromptForScene
import com.mgn.ai.data.datastore.Settings
import com.mgn.ai.data.datastore.findModelById
import com.mgn.ai.data.datastore.promptOptimizeDepthForScene
import com.mgn.ai.data.datastore.promptOptimizePromptForScene
import com.mgn.ai.data.datastore.promptOptimizeThinkingBudgetForScene
import com.mgn.ai.data.datastore.withPromptOptimizeDepth
import com.mgn.ai.data.datastore.withPromptOptimizePrompt
import com.mgn.ai.data.datastore.withPromptOptimizeThinkingBudget
import com.mgn.ai.ui.components.ai.ModelListSheet
import com.mgn.ai.ui.components.ai.ReasoningButton
import com.mgn.ai.ui.components.ai.rememberModelListState
import com.mgn.ai.ui.components.nav.BackButton
import com.mgn.ai.ui.components.ui.CardGroup
import com.mgn.ai.ui.theme.CustomColors
import com.mgn.ai.utils.plus
import org.koin.androidx.compose.koinViewModel
import kotlin.uuid.Uuid

@Composable
fun SettingModelPage(vm: SettingVM = koinViewModel()) {
    val settings by vm.settings.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val pagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = CustomColors.topBarColors.containerColor,
        topBar = {
            LargeFlexibleTopAppBar(
                title = { Text(stringResource(R.string.setting_model_page_title)) },
                navigationIcon = { BackButton() },
                scrollBehavior = scrollBehavior,
                colors = CustomColors.topBarColors,
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = CustomColors.cardColorsOnSurfaceContainer.containerColor
            ) {
                NavigationBarItem(
                    selected = pagerState.currentPage == 0,
                    onClick = { scope.launch { pagerState.animateScrollToPage(0) } },
                    icon = { Icon(HugeIcons.AiBrain01, null) },
                    label = { Text(stringResource(R.string.setting_model_page_tab_model)) }
                )
                NavigationBarItem(
                    selected = pagerState.currentPage == 1,
                    onClick = { scope.launch { pagerState.animateScrollToPage(1) } },
                    icon = { Icon(HugeIcons.AiEditing, null) },
                    label = { Text(stringResource(R.string.setting_model_page_tab_prompt)) }
                )
            }
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { contentPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            when (page) {
                0 -> ModelSettingsPage(settings = settings, vm = vm, contentPadding = contentPadding)
                1 -> PromptSettingsPage(settings = settings, vm = vm, contentPadding = contentPadding)
            }
        }
    }
}

@Composable
private fun ModelSettingsPage(settings: Settings, vm: SettingVM, contentPadding: PaddingValues) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding + PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            ModelSettingItem(
                title = stringResource(R.string.setting_model_page_chat_model),
                description = stringResource(R.string.setting_model_page_chat_model_desc),
                modelId = settings.chatModelId,
                providers = settings.providers,
                onSelect = { vm.updateSettings(settings.copy(chatModelId = it.id)) },
            )
        }
        item {
            ModelSettingItem(
                title = stringResource(R.string.setting_model_page_fast_model),
                description = stringResource(R.string.setting_model_page_fast_model_desc),
                modelId = settings.fastModelId,
                providers = settings.providers,
                onSelect = { vm.updateSettings(settings.copy(fastModelId = it.id)) },
                reasoningLevel = settings.fastModelReasoningLevel,
                onUpdateReasoningLevel = {
                    vm.updateSettings(settings.copy(fastModelReasoningLevel = it))
                },
            )
        }
        item {
            // Sub-agent: no model selected = disabled (dispatch_subagent not exposed at all)
            ModelSettingItem(
                title = stringResource(R.string.setting_model_page_subagent_model),
                description = stringResource(R.string.setting_model_page_subagent_model_desc),
                modelId = settings.subAgentModelId,
                providers = settings.providers,
                onSelect = { vm.updateSettings(settings.copy(subAgentModelId = it.id)) },
                onClear = { vm.updateSettings(settings.copy(subAgentModelId = null)) },
                reasoningLevel = settings.subAgentReasoningLevel,
                onUpdateReasoningLevel = {
                    vm.updateSettings(settings.copy(subAgentReasoningLevel = it))
                },
            )
        }
        item {
            SuggestionSettingItem(
                settings = settings,
                vm = vm,
            )
        }
        item {
            PromptOptimizeGroup(settings = settings, vm = vm)
        }
        item {
            KnowledgeGroup(settings = settings, vm = vm)
        }
        item {
            ModelSettingItem(
                title = stringResource(R.string.setting_model_page_translate_model),
                description = stringResource(R.string.setting_model_page_translate_model_desc),
                modelId = settings.translateModeId,
                providers = settings.providers,
                onSelect = { vm.updateSettings(settings.copy(translateModeId = it.id)) },
            )
        }
        item {
            ModelSettingItem(
                title = stringResource(R.string.setting_model_page_ocr_model),
                description = stringResource(R.string.setting_model_page_ocr_model_desc),
                modelId = settings.ocrModelId,
                providers = settings.providers,
                onSelect = { vm.updateSettings(settings.copy(ocrModelId = it.id)) },
            )
        }
        item {
            ModelSettingItem(
                title = stringResource(R.string.setting_model_page_compress_model),
                description = stringResource(R.string.setting_model_page_compress_model_desc),
                modelId = settings.compressModelId,
                providers = settings.providers,
                onSelect = { vm.updateSettings(settings.copy(compressModelId = it.id)) },
            )
        }
    }
}

@Composable
private fun SuggestionSettingItem(
    settings: Settings,
    vm: SettingVM,
) {
    CardGroup {
        item(
            headlineContent = { Text(stringResource(R.string.setting_model_page_enable_suggestion)) },
            trailingContent = {
                Switch(
                    checked = settings.enableSuggestion,
                    onCheckedChange = {
                        vm.updateSettings(settings.copy(enableSuggestion = it))
                    }
                )
            },
        )
    }
}

@Composable
private fun ModelSettingItem(
    title: String,
    description: String,
    modelId: Uuid?,
    providers: List<ProviderSetting>,
    onSelect: (Model) -> Unit,
    reasoningLevel: ReasoningLevel? = null,
    onUpdateReasoningLevel: ((ReasoningLevel) -> Unit)? = null,
    onClear: (() -> Unit)? = null,
) {
    val state = rememberModelListState(
        modelId = modelId,
        providers = providers,
        type = ModelType.CHAT,
    )

    Column {
        CardGroup(title = { Text(title) }) {
            item(
                onClick = { state.open() },
                headlineContent = { Text(title) },
                trailingContent = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = state.currentModel?.displayName
                                ?: stringResource(R.string.model_list_select_model),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Icon(
                            HugeIcons.ArrowRight01,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                        )
                    }
                },
            )
            if (reasoningLevel != null && onUpdateReasoningLevel != null) {
                item(
                    headlineContent = { Text(stringResource(R.string.assistant_page_thinking_budget)) },
                    trailingContent = {
                        ReasoningButton(
                            reasoningLevel = reasoningLevel,
                            onUpdateReasoningLevel = onUpdateReasoningLevel,
                        )
                    },
                )
            }
            if (modelId != null && onClear != null) {
                item(
                    onClick = onClear,
                    headlineContent = {
                        Text(stringResource(R.string.setting_model_page_clear_model))
                    },
                )
            }
        }
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
        )
    }

    ModelListSheet(state = state, onSelect = onSelect)
}

@Composable
private fun PromptOptimizeGroup(settings: Settings, vm: SettingVM) {
    val modelState = rememberModelListState(settings.promptOptimizeModelId, settings.providers, ModelType.CHAT)
    var showSheet by remember { mutableStateOf(false) }
    var selectedScene by remember { mutableStateOf(PromptOptimizeScene.GENERAL) }

    CardGroup(
        title = { Text(stringResource(R.string.setting_model_page_group_prompt_optimize)) },
    ) {
        item(
            onClick = { modelState.open() },
            headlineContent = { Text(stringResource(R.string.setting_model_page_prompt_optimize_model)) },
            supportingContent = { Text(stringResource(R.string.setting_model_page_prompt_optimize_model_desc)) },
            trailingContent = {
                Text(
                    text = settings.promptOptimizeModelId?.let { settings.providers.findModelById(it)?.displayName }
                        ?: stringResource(R.string.model_list_select_model),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
        )
        if (settings.promptOptimizeModelId != null) {
            item(
                onClick = { vm.updateSettings(settings.copy(promptOptimizeModelId = null)) },
                headlineContent = {
                    Text(stringResource(R.string.setting_model_page_clear_model))
                },
            )
        }
        item(
            onClick = { showSheet = true },
            headlineContent = { Text(stringResource(R.string.setting_model_page_prompt_optimize)) },
            supportingContent = { Text(stringResource(R.string.setting_model_page_prompt_optimize_desc)) },
        )
    }

    ModelListSheet(
        state = modelState,
        onSelect = { vm.updateSettings(settings.copy(promptOptimizeModelId = it.id)) },
    )

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = rememberBottomSheetState(
                initialValue = SheetValue.Hidden,
                enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded),
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
                    .imePadding()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    PromptOptimizeScene.entries.forEach { scene ->
                        FilterChip(
                            selected = selectedScene == scene,
                            onClick = { selectedScene = scene },
                            label = { Text(promptOptimizeSceneName(scene)) },
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.prompt_optimize_depth),
                        style = MaterialTheme.typography.labelLarge,
                    )
                    Spacer(Modifier.width(4.dp))
                    PromptOptimizeDepth.entries.forEach { depth ->
                        FilterChip(
                            selected = settings.promptOptimizeDepthForScene(selectedScene) == depth,
                            onClick = {
                                vm.updateSettings(settings.withPromptOptimizeDepth(selectedScene, depth))
                            },
                            label = { Text(stringResource(depthLabelRes(depth))) },
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.setting_model_page_prompt_optimize) + " · " + promptOptimizeSceneName(selectedScene),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    ReasoningButton(
                        reasoningLevel = ReasoningLevel.fromBudgetTokens(
                            settings.promptOptimizeThinkingBudgetForScene(selectedScene)
                        ),
                        onUpdateReasoningLevel = { level ->
                            vm.updateSettings(settings.withPromptOptimizeThinkingBudget(selectedScene, level.budgetTokens))
                        },
                        onlyIcon = true,
                    )
                }

                Text(
                    text = stringResource(R.string.setting_model_page_prompt_optimize_desc),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                OutlinedTextField(
                    value = settings.promptOptimizePromptForScene(selectedScene)
                        ?: defaultPromptOptimizePromptForScene(selectedScene),
                    onValueChange = { vm.updateSettings(settings.withPromptOptimizePrompt(selectedScene, it)) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 15,
                )

                TextButton(
                    onClick = {
                        if (selectedScene == PromptOptimizeScene.GENERAL) {
                            vm.updateSettings(
                                settings.withPromptOptimizePrompt(selectedScene, "").copy(promptOptimizePrompt = null)
                            )
                        } else {
                            vm.updateSettings(settings.withPromptOptimizePrompt(selectedScene, ""))
                        }
                    }
                ) {
                    Text(stringResource(R.string.setting_model_page_reset_to_default))
                }
            }
        }
    }
}

@Composable
private fun promptOptimizeSceneName(scene: PromptOptimizeScene): String = stringResource(
    when (scene) {
        PromptOptimizeScene.GENERAL -> R.string.prompt_optimize_scene_general
        PromptOptimizeScene.WRITING -> R.string.prompt_optimize_scene_writing
        PromptOptimizeScene.QUESTION -> R.string.prompt_optimize_scene_question
        PromptOptimizeScene.PROGRAMMING -> R.string.prompt_optimize_scene_programming
    }
)

@Composable
private fun depthLabelRes(depth: PromptOptimizeDepth): Int = when (depth) {
    PromptOptimizeDepth.CONCISE -> R.string.prompt_optimize_depth_concise
    PromptOptimizeDepth.MEDIUM -> R.string.prompt_optimize_depth_medium
    PromptOptimizeDepth.DETAILED -> R.string.prompt_optimize_depth_detailed
}

@Composable
private fun KnowledgeGroup(settings: Settings, vm: SettingVM) {
    val embeddingState = rememberModelListState(settings.embeddingModelId, settings.providers, ModelType.EMBEDDING)
    val rerankState = rememberModelListState(settings.rerankModelId, settings.providers, ModelType.RERANKING)

    CardGroup(
        title = { Text(stringResource(R.string.setting_model_page_group_knowledge)) },
    ) {
        item(
            onClick = { embeddingState.open() },
            headlineContent = { Text(stringResource(R.string.setting_model_page_embedding_model)) },
            supportingContent = { Text(stringResource(R.string.setting_model_page_embedding_model_desc)) },
            trailingContent = {
                Text(
                    text = settings.providers.findModelById(settings.embeddingModelId)?.displayName
                        ?: stringResource(R.string.model_list_select_model),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
        )
        item(
            onClick = { rerankState.open() },
            headlineContent = { Text(stringResource(R.string.setting_model_page_rerank_model)) },
            supportingContent = { Text(stringResource(R.string.setting_model_page_rerank_model_desc)) },
            trailingContent = {
                Text(
                    text = settings.providers.findModelById(settings.rerankModelId)?.displayName
                        ?: stringResource(R.string.model_list_select_model),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
        )
    }

    ModelListSheet(
        state = embeddingState,
        onSelect = { vm.updateSettings(settings.copy(embeddingModelId = it.id)) },
        onClear = { vm.updateSettings(settings.copy(embeddingModelId = null)) },
    )
    ModelListSheet(
        state = rerankState,
        onSelect = { vm.updateSettings(settings.copy(rerankModelId = it.id)) },
        onClear = { vm.updateSettings(settings.copy(rerankModelId = null)) },
    )
}
