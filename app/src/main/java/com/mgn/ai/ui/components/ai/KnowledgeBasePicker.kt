package com.mgn.ai.ui.components.ai

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import me.rerere.hugeicons.HugeIcons
import me.rerere.hugeicons.stroke.Bookshelf01
import com.mgn.ai.knowledge.KnowledgeManager
import com.mgn.ai.Screen
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import org.koin.compose.koinInject

@OptIn(ExperimentalUuidApi::class)
@Composable
fun KnowledgeBasePickerButton(
    selectedIds: Set<Uuid>,
    onSelectionChange: (Set<Uuid>) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    var showPicker by remember { mutableStateOf(false) }
    val hapticController = LocalHapticFeedback.current

    val hasSelection = selectedIds.isNotEmpty()

    IconButton(
        enabled = enabled,
        onClick = {
            hapticController.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            showPicker = true
        },
        modifier = modifier.size(40.dp),
    ) {
        Icon(
            HugeIcons.Bookshelf01,
            contentDescription = "Knowledge bases",
            modifier = Modifier.size(20.dp),
            tint = when {
                !enabled -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                hasSelection -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            },
        )
    }

    if (showPicker) {
        ModalBottomSheet(
            onDismissRequest = { showPicker = false },
            sheetState = rememberBottomSheetState(initialValue = SheetValue.Hidden, enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded)),
        ) {
            KnowledgeBasePicker(
                selectedIds = selectedIds,
                onSelectionChange = onSelectionChange,
                onDismiss = { showPicker = false },
            )
        }
    }
}

/**
 * 知识库选择器（供底部弹窗 / 「更多选项」入口复用，UI 统一）。
 */
@OptIn(ExperimentalUuidApi::class)
@Composable
fun KnowledgeBasePicker(
    selectedIds: Set<Uuid>,
    onSelectionChange: (Set<Uuid>) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val knowledgeManager = koinInject<KnowledgeManager>()
    val bases by knowledgeManager.baseRepository.getAllWithDocumentCount()
        .collectAsStateWithLifecycle(initialValue = emptyList())

    Column(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 24.dp),
    ) {
        Text(
            "选择知识库",
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            "Selected bases are searchable by the AI in this chat",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp),
        )

        if (bases.isEmpty()) {
            Text(
                "No knowledge bases yet",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 16.dp),
            )
        }

        LazyColumn(
            modifier = Modifier.heightIn(max = 400.dp),
        ) {
            items(bases, key = { it.id }) { base ->
                val baseUuid = Uuid.parse(base.id)
                val isSelected = baseUuid in selectedIds
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(base.name, style = MaterialTheme.typography.titleSmall)
                        if (base.description.isNotBlank()) {
                            Text(
                                base.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                            )
                        }
                        Text(
                            "${base.documentCount} docs",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Switch(
                        checked = isSelected,
                        onCheckedChange = { checked ->
                            if (checked) {
                                onSelectionChange(selectedIds + baseUuid)
                            } else {
                                onSelectionChange(selectedIds - baseUuid)
                            }
                        },
                    )
                }
            }

        }
    }
}
