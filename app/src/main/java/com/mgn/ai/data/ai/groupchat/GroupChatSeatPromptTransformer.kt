package com.mgn.ai.data.ai.groupchat

import com.mgn.ai.ai.ui.UIMessage
import com.mgn.ai.data.ai.transformers.InputMessageTransformer
import com.mgn.ai.data.ai.transformers.TransformerContext
import com.mgn.ai.data.model.Assistant
import com.mgn.ai.data.model.GroupChatSeat
import kotlin.uuid.Uuid

class GroupChatSeatPromptTransformer(
    private val seat: GroupChatSeat,
    private val selfAssistantId: Uuid,
    private val seatDisplayNames: Map<Uuid, String>,
    private val assistantsById: Map<Uuid, Assistant>,
    private val userName: String,
) : InputMessageTransformer {
    override suspend fun transform(
        ctx: TransformerContext,
        messages: List<UIMessage>,
    ): List<UIMessage> {
        return GroupChatEngine.rewritePromptMessagesForSeat(
            messages = messages,
            seat = seat,
            selfAssistantId = selfAssistantId,
            seatDisplayNames = seatDisplayNames,
            assistantsById = assistantsById,
            userName = userName,
        )
    }
}
