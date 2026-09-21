package com.mgn.ai.ai.provider.providers.openai

import kotlinx.coroutines.flow.Flow
import com.mgn.ai.ai.provider.ProviderSetting
import com.mgn.ai.ai.provider.TextGenerationResult
import com.mgn.ai.ai.provider.TextGenerationParams
import com.mgn.ai.ai.ui.StreamChunk
import com.mgn.ai.ai.ui.UIMessage

interface OpenAIImpl {
    suspend fun generateText(
        providerSetting: ProviderSetting.OpenAI,
        messages: List<UIMessage>,
        params: TextGenerationParams,
    ): TextGenerationResult

    suspend fun streamText(
        providerSetting: ProviderSetting.OpenAI,
        messages: List<UIMessage>,
        params: TextGenerationParams,
    ): Flow<StreamChunk>
}
