package com.mgn.ai.data.repository

import kotlin.uuid.Uuid

/**
 * Coordinates conversation deletion between the chat service (session
 * tracking) and the repository (database + files).
 *
 * Adapted from roccla231023/ROCL (AGPL-3.0, same license).
 */
interface ConversationDeletionCoordinator {
    suspend fun deleteConversationById(conversationId: Uuid, deleteFiles: Boolean = true)

    suspend fun deleteConversationsOfAssistant(assistantId: Uuid, deleteFiles: Boolean = true)
}
