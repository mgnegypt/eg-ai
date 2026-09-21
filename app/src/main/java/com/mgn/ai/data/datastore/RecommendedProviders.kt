package com.mgn.ai.data.datastore

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import com.mgn.ai.ai.provider.ProviderSetting
import com.mgn.ai.ui.components.richtext.MarkdownBlock
import kotlin.uuid.Uuid

/**
 * 推荐的提供商列表，在提供商设置页右上角的推荐 Sheet 中展示。
 */
val RECOMMENDED_PROVIDERS: List<ProviderSetting> = listOf(
    ProviderSetting.OpenAI(
        id = Uuid.parse("1b1395ed-b702-4aeb-8bc1-b681c4456953"),
        name = "AiHubMix",
        baseUrl = "https://aihubmix.com/v1",
        apiKey = "",
        enabled = true,
        description = {
            Text(
                text = buildAnnotatedString {
                    append("Provides high-concurrency, stable access to popular models including OpenAI, Claude, and Google Gemini")
                    appendLine()
                    append("Website: ")
                    withLink(LinkAnnotation.Url("https://aihubmix.com")) {
                        withStyle(SpanStyle(MaterialTheme.colorScheme.primary)) {
                            append("https://aihubmix.com")
                        }
                    }
                    appendLine()
                    append("Top up: ")
                    withLink(LinkAnnotation.Url("https://console.aihubmix.com/topup")) {
                        withStyle(SpanStyle(MaterialTheme.colorScheme.primary)) {
                            append("https://console.aihubmix.com/topup")
                        }
                    }
                }
            )
        },
    ),
    ProviderSetting.OpenAI(
        id = Uuid.parse("2a05506f-3a59-450a-a493-33a82bc85a81"),
        name = "APIMart",
        baseUrl = "https://api.apimart.ai/v1",
        apiKey = "",
        enabled = true,
        description = {
            Text(
                text = buildAnnotatedString {
                    append("APIMart is a low-cost API platform focused on AI image/video generation, with GPT-Image-2 as low as $0.006/image and 160+ images per $1. One async API covers both images and videos: submit a task to get an ID, fetch results via callback, batch thousands of images without timeouts, and switch models without changing code. Pay-as-you-go with no monthly fee.")
                    appendLine()
                    withLink(LinkAnnotation.Url("https://apimart.ai")) {
                        withStyle(SpanStyle(MaterialTheme.colorScheme.primary)) {
                            append("Register via this link to get started")
                        }
                    }
                }
            )
        },
    ),
    ProviderSetting.OpenAI(
        id = Uuid.parse("aecf04fd-cb5c-4582-aed2-e8bf393923fd"),
        name = "Suixiang AI Gateway",
        baseUrl = "https://sui-xiang.com/v1",
        apiKey = "",
        enabled = true,
        description = {
            Text(
                text = buildAnnotatedString {
                    append("Reliable, high-performance API relay service offering Claude, Codex, Gemini relay and more. Privacy-focused with no data reselling or model substitution, 1:1 top-up credit, pay-as-you-go. Multi-route redundancy, cross-region failover, automatic fault switching, and uninterrupted long-lived SSE streams.")
                    appendLine()
                    append("Website: ")
                    withLink(LinkAnnotation.Url("https://sui-xiang.com")) {
                        withStyle(SpanStyle(MaterialTheme.colorScheme.primary)) {
                            append("https://sui-xiang.com")
                        }
                    }
                }
            )
        },
    ),
    ProviderSetting.OpenAI(
        id = Uuid.parse("afbc54ad-807e-4455-9594-7d7a546356ad"),
        name = "MaruCode",
        baseUrl = "https://api.muteki.site/v1",
        apiKey = "",
        enabled = true,
        description = {
            Text(
                text = buildAnnotatedString {
                    append("MaruCode is a small independent API service with its own account pool, offering popular models such as Codex, Claude Code, and GPT Image. It supports the WebSocket protocol with transparent pricing (Codex 0.25x, CC 1.5x) and a transparent 1:1 exchange rate.")
                    appendLine()
                    withLink(LinkAnnotation.Url("https://api.muteki.site/register")) {
                        withStyle(SpanStyle(MaterialTheme.colorScheme.primary)) {
                            append("New users get $2 on registration")
                        }
                    }
                    appendLine()
                    withLink(LinkAnnotation.Url("https://images-2.muteki.site")) {
                        withStyle(SpanStyle(MaterialTheme.colorScheme.primary)) {
                            append("Image generation workbench🖼️")
                        }
                    }
                }
            )
        },
        useResponseApi = true,
    ),
)
