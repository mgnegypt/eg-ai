package com.mgn.ai.ui.pages.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mgn.ai.R
import com.mgn.ai.data.ai.tools.TodoItem
import com.mgn.ai.data.ai.tools.TodoList
import com.mgn.ai.data.ai.tools.TodoStatus
import com.mgn.ai.ui.components.message.getSectionExpanded
import com.mgn.ai.ui.components.message.setSectionExpanded
import me.rerere.hugeicons.HugeIcons
import me.rerere.hugeicons.stroke.ArrowDown01
import me.rerere.hugeicons.stroke.ArrowRight01
import me.rerere.hugeicons.stroke.ArrowUp01
import me.rerere.hugeicons.stroke.Cancel01
import me.rerere.hugeicons.stroke.LeftToRightListBullet
import me.rerere.hugeicons.stroke.Sparkles
import me.rerere.hugeicons.stroke.Tick01

/**
 * Task-plan banner above the chat input, driven by TodoStorage.
 *
 * Adapted from Inonvation/rikkahub (AGPL-3.0, same license).
 */
@Composable
private fun TodolistBanner(
    todolist: TodoList,
    onDismiss: () -> Unit,
    stateKey: String? = null,
    modifier: Modifier = Modifier,
) {
    val inProgressItems = todolist.items.filter { it.status == TodoStatus.in_progress }
    val pendingCount = todolist.items.count { it.status == TodoStatus.pending }
    val completed = todolist.items.count { it.status == TodoStatus.completed || it.status == TodoStatus.cancelled }
    val total = todolist.items.size
    val allDone = completed == total
    val hasActive = inProgressItems.isNotEmpty() || pendingCount > 0

    // 有活跃任务时默认展开，全部完成时默认折叠；用户手动展开/折叠过则优先恢复记忆
    var expanded by remember(hasActive) {
        mutableStateOf(stateKey?.let { getSectionExpanded(it) } ?: hasActive)
    }

    // todo 列表条目增删时自动重新显示（仅 ID 集合变化，内容变更不触发）
    val itemsFingerprint = todolist.items.map { it.id }.toSet().hashCode()
    var dismissed by remember { mutableStateOf(false) }
    LaunchedEffect(itemsFingerprint) {
        dismissed = false
    }

    // 本地状态层：用于支持条目的出场动画，key 为 todolist 确保切换会话时重新初始化
    val displayItems = remember(todolist) { mutableStateListOf<TodoItem>().apply { addAll(todolist.items) } }
    val removingIds = remember { mutableStateOf<Set<String>>(emptySet()) }

    LaunchedEffect(todolist.items) {
        val oldIds = displayItems.map { it.id }.toSet()
        val newIds = todolist.items.map { it.id }.toSet()

        val removedIds = oldIds - newIds
        val addedIds = newIds - oldIds

        // 清理不再需要移除的条目（被重新加入的），只保留当前仍在移除列表中的
        removingIds.value = removingIds.value.intersect(removedIds)

        // 标记已移除条目，触发出场动画（清理由各条目在动画完成后自行处理）
        if (removedIds.isNotEmpty()) {
            removingIds.value = removingIds.value + removedIds
        }

        // 添加新条目，触发入场动画
        displayItems.addAll(todolist.items.filter { it.id in addedIds })

        // 更新已存在的条目，触发颜色过渡
        todolist.items.filter { it.id in oldIds.intersect(newIds) }.forEach { updated ->
            val index = displayItems.indexOfFirst { it.id == updated.id }
            if (index >= 0) displayItems[index] = updated
        }
    }

    val progress = if (total > 0) completed.toFloat() / total else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(500),
        label = "progress",
    )

    AnimatedVisibility(visible = !dismissed) {
        Card(
            modifier = modifier.animateContentSize(),
            shape = RoundedCornerShape(12.dp),
            onClick = {
                expanded = !expanded
                // 记录用户手动展开/折叠，切换窗口回来保持
                if (stateKey != null) setSectionExpanded(stateKey, expanded)
            },
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // 标题行
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f),
                    ) {
                        Icon(
                            imageVector = if (allDone) HugeIcons.Tick01 else HugeIcons.LeftToRightListBullet,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(Modifier.width(6.dp))
                        if (!expanded) {
                            val collapsedText = when {
                                allDone -> stringResource(R.string.chat_message_tool_todo_all_done)
                                inProgressItems.isNotEmpty() -> inProgressItems.first().content
                                pendingCount > 0 -> stringResource(R.string.chat_message_tool_todo_pending_count, pendingCount)
                                else -> stringResource(R.string.chat_message_tool_todo_title)
                            }
                            Text(
                                text = collapsedText,
                                style = MaterialTheme.typography.titleSmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = if (allDone) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface,
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.chat_message_tool_todo_title),
                                style = MaterialTheme.typography.titleSmall,
                            )
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (allDone) stringResource(R.string.chat_message_tool_todo_done_label)
                                else "$completed/$total",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (allDone) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(Modifier.width(4.dp))
                        Icon(
                            imageVector = if (expanded) HugeIcons.ArrowUp01 else HugeIcons.ArrowDown01,
                            contentDescription = stringResource(
                                if (expanded) R.string.chat_message_tool_todo_collapse
                                else R.string.chat_message_tool_todo_expand
                            ),
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(Modifier.width(4.dp))
                        Icon(
                            imageVector = HugeIcons.Cancel01,
                            contentDescription = stringResource(R.string.chat_message_tool_todo_close),
                            modifier = Modifier
                                .size(16.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                ) {
                                    dismissed = true
                                    // 全部完成后关闭 = 彻底隐藏这个任务集：持久化指纹，
                                    // 切换界面 / 重启进程都不再显示；AI 换新任务后自动重新出现。
                                    // 任务未完成时关闭仍只是本次折叠（下轮提醒/更新后重新显示）。
                                    onDismiss()
                                },
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                // 折叠态也显示细进度条，展开态显示完整进度条
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = if (expanded) 4.dp else 6.dp),
                    strokeCap = StrokeCap.Round,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )

                // 展开态：显示更新说明和任务列表
                AnimatedVisibility(
                    visible = expanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut(),
                ) {
                    Column {
                        Spacer(Modifier.size(4.dp))
                        displayItems.forEach { item ->
                            key(item.id) {
                                val isRemoving = item.id in removingIds.value
                                val visibleState = remember { MutableTransitionState(false) }

                                LaunchedEffect(isRemoving) {
                                    visibleState.targetState = !isRemoving
                                }

                                // 出场动画完成后从 displayItems 中移除
                                LaunchedEffect(isRemoving, visibleState.isIdle) {
                                    if (isRemoving && visibleState.isIdle &&
                                        !visibleState.currentState && !visibleState.targetState
                                    ) {
                                        displayItems.removeAll { it.id == item.id }
                                    }
                                }

                                AnimatedVisibility(
                                    visibleState = visibleState,
                                    enter = fadeIn(animationSpec = tween(300)) +
                                        slideInVertically(animationSpec = tween(300)) { it },
                                    exit = fadeOut(animationSpec = tween(300)) +
                                        slideOutVertically(animationSpec = tween(300)) { -it },
                                ) {
                                    TodoItemRow(item)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TodoItemRow(item: TodoItem) {
    val iconTint by animateColorAsState(
        targetValue = when (item.status) {
            TodoStatus.completed -> MaterialTheme.colorScheme.primary
            TodoStatus.in_progress -> MaterialTheme.colorScheme.tertiary
            TodoStatus.cancelled -> MaterialTheme.colorScheme.error
            TodoStatus.pending -> MaterialTheme.colorScheme.onSurfaceVariant
        },
        animationSpec = tween(400),
        label = "iconTint",
    )
    val textColor by animateColorAsState(
        targetValue = when (item.status) {
            TodoStatus.completed, TodoStatus.cancelled ->
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            else -> MaterialTheme.colorScheme.onSurface
        },
        animationSpec = tween(400),
        label = "textColor",
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = when (item.status) {
                TodoStatus.completed -> HugeIcons.Tick01
                TodoStatus.in_progress -> HugeIcons.Sparkles
                TodoStatus.cancelled -> HugeIcons.Cancel01
                TodoStatus.pending -> HugeIcons.ArrowRight01
            },
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = iconTint,
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = item.content,
            style = MaterialTheme.typography.bodySmall,
            textDecoration = if (item.status == TodoStatus.completed || item.status == TodoStatus.cancelled)
                TextDecoration.LineThrough else TextDecoration.None,
            color = textColor,
        )
    }
}
