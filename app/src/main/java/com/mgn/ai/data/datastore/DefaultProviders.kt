package com.mgn.ai.data.datastore

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import com.mgn.ai.ai.provider.BalanceOption
import com.mgn.ai.ai.provider.ProviderSetting
import com.mgn.ai.R
import com.mgn.ai.ui.components.richtext.MarkdownBlock
import kotlin.uuid.Uuid

val DEFAULT_AUTO_MODEL_ID = Uuid.parse("b7055fb4-39f9-4042-a88a-0d80ed76cf08")

val DEFAULT_PROVIDERS = listOf(
    ProviderSetting.OpenAI(
        id = Uuid.parse("1eeea727-9ee5-4cae-93e6-6fb01a4d051e"),
        name = "OpenAI",
        baseUrl = "https://api.openai.com/v1",
        apiKey = "",
        builtIn = true
    ),
    ProviderSetting.Google(
        id = Uuid.parse("6ab18148-c138-4394-a46f-1cd8c8ceaa6d"),
        name = "Gemini",
        apiKey = "",
        enabled = true,
        builtIn = true
    ),
    ProviderSetting.OpenAI(
        id = Uuid.parse("1b1395ed-b702-4aeb-8bc1-b681c4456953"),
        name = "AiHubMix",
        baseUrl = "https://aihubmix.com/v1",
        apiKey = "",
        enabled = true,
        builtIn = true,
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
        shortDescription = {
            Text(
                text = "Supports GPT, Claude, Gemini and 200+ more models"
            )
        },
    ),
    ProviderSetting.OpenAI(
        id = Uuid.parse("2a05506f-3a59-450a-a493-33a82bc85a81"),
        name = "APIMart",
        baseUrl = "https://api.apimart.ai/v1",
        apiKey = "",
        enabled = false,
        builtIn = true,
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
        shortDescription = {
            Text("AI image/video generation, GPT-Image-2 as low as $0.006/image")
        },
    ),
    ProviderSetting.OpenAI(
        id = Uuid.parse("56a94d29-c88b-41c5-8e09-38a7612d6cf8"),
        name = "SiliconFlow",
        baseUrl = "https://api.siliconflow.cn/v1",
        apiKey = "",
        builtIn = true,
        description = {
            MarkdownBlock(
                content = """
                    ${stringResource(R.string.silicon_flow_description)}
                    ${stringResource(R.string.silicon_flow_website)}
                """.trimIndent()
            )
        },
    ),
    ProviderSetting.OpenAI(
        id = Uuid.parse("f099ad5b-ef03-446d-8e78-7e36787f780b"),
        name = "DeepSeek",
        baseUrl = "https://api.deepseek.com/v1",
        apiKey = "",
        builtIn = true,
        balanceOption = BalanceOption(
            enabled = true,
            apiPath = "/user/balance",
            resultPath = "balance_infos[0].total_balance"
        )
    ),
    ProviderSetting.OpenAI(
        id = Uuid.parse("d6c4d8c6-3f62-4ca9-a6f3-7ade6b15ecc3"),
        name = "Moonshot",
        baseUrl = "https://api.moonshot.cn/v1",
        apiKey = "",
        enabled = true,
        builtIn = true,
        balanceOption = BalanceOption(
            enabled = true,
            apiPath = "/users/me/balance",
            resultPath = "data.available_balance"
        )
    ),
    ProviderSetting.OpenAI(
        id = Uuid.parse("d5734028-d39b-4d41-9841-fd648d65440e"),
        name = "OpenRouter",
        baseUrl = "https://openrouter.ai/api/v1",
        apiKey = "",
        builtIn = true,
        balanceOption = BalanceOption(
            enabled = true,
            apiPath = "/credits",
            resultPath = "data.total_credits - data.total_usage",
        )
    ),
    ProviderSetting.OpenAI(
        id = Uuid.parse("386e0f29-8228-4512-affe-8fd8add82d88"),
        name = "Vercel AI Gateway",
        baseUrl = "https://ai-gateway.vercel.sh/v1",
        apiKey = "",
        enabled = false,
        builtIn = true,
        balanceOption = BalanceOption(
            enabled = true,
            apiPath = "/credits",
            resultPath = "balance",
        )
    ),
    ProviderSetting.OpenAI(
        id = Uuid.parse("da020a90-f7b3-4c29-b90e-c511a0630630"),
        name = "TokenPony",
        baseUrl = "https://api.tokenpony.cn/v1",
        apiKey = "",
        enabled = false,
        builtIn = true,
        description = {
            MarkdownBlock(
                content = """
                    TokenPony is an API gateway for Chinese models, providing unified access to multiple models
                    Website: [tokenpony.cn](https://www.tokenpony.cn/79clb)
                """.trimIndent()
            )
        }
    ),
    ProviderSetting.OpenAI(
        id = Uuid.parse("f76cae46-069a-4334-ab8e-224e4979e58c"),
        name = "Alibaba Bailian",
        baseUrl = "https://dashscope.aliyuncs.com/compatible-mode/v1",
        apiKey = "",
        enabled = false,
        builtIn = true
    ),
    ProviderSetting.OpenAI(
        id = Uuid.parse("3dfd6f9b-f9d9-417f-80c1-ff8d77184191"),
        name = "Volcano Engine",
        baseUrl = "https://ark.cn-beijing.volces.com/api/v3",
        apiKey = "",
        enabled = false,
        builtIn = true
    ),
    ProviderSetting.OpenAI(
        id = Uuid.parse("3bc40dc1-b11a-46fa-863b-6306971223be"),
        name = "Zhipu AI Open Platform",
        baseUrl = "https://open.bigmodel.cn/api/paas/v4",
        apiKey = "",
        enabled = false,
        builtIn = true
    ),
    ProviderSetting.OpenAI(
        id = Uuid.parse("f4f8870e-82d3-495b-9b64-d58e508b3b2c"),
        name = "StepFun",
        baseUrl = "https://api.stepfun.com/v1",
        apiKey = "",
        enabled = false,
        builtIn = true
    ),
    ProviderSetting.OpenAI(
        id = Uuid.parse("da93779f-3956-48cc-82ef-67bb482eaaf7"),
        name = "302.AI",
        baseUrl = "https://api.302.ai/v1",
        apiKey = "",
        enabled = false,
        builtIn = true,
        description = {
            Text(
                text = buildAnnotatedString {
                    append("Enterprise-grade AI service, Website: ")
                    withLink(LinkAnnotation.Url("https://302.ai/")) {
                        withStyle(SpanStyle(MaterialTheme.colorScheme.primary)) {
                            append("https://302.ai/")
                        }
                    }
                }
            )
        }
    ),
    ProviderSetting.OpenAI(
        id = Uuid.parse("ef5d149b-8e34-404b-818c-6ec242e5c3c5"),
        name = "Tencent Hunyuan",
        baseUrl = "https://api.hunyuan.cloud.tencent.com/v1",
        apiKey = "",
        enabled = false,
        builtIn = true
    ),
    ProviderSetting.OpenAI(
        id = Uuid.parse("ff3cde7e-0f65-43d7-8fb2-6475c99f5990"),
        name = "xAI",
        baseUrl = "https://api.x.ai/v1",
        apiKey = "",
        enabled = false,
        builtIn = true,
        useResponseApi = true,
    ),
    ProviderSetting.OpenAI(
        id = Uuid.parse("aecf04fd-cb5c-4582-aed2-e8bf393923fd"),
        name = "Suixiang AI Gateway",
        baseUrl = "https://sui-xiang.com/v1",
        apiKey = "",
        enabled = false,
        builtIn = true,
        description = {
            Text(
                text = buildAnnotatedString {
                    append("Reliable, high-performance API relay service offering Claude, Codex, Gemini relay and more. Privacy-focused with no data reselling or model substitution, 1:1 top-up credit, pay-as-you-go. Multi-route redundancy, cross-region failover, automatic fault switching, and uninterrupted long-lived SSE streams.\n")
                    append("Website: ")
                    withLink(LinkAnnotation.Url("https://sui-xiang.com")) {
                        withStyle(SpanStyle(MaterialTheme.colorScheme.primary)) {
                            append("https://sui-xiang.com")
                        }
                    }
                }
            )
        },
        shortDescription = {
            Text(
                text = "Claude, Codex, Gemini relay and more, 1:1 top-up"
            )
        },
    ),
    ProviderSetting.OpenAI(
        id = Uuid.parse("afbc54ad-807e-4455-9594-7d7a546356ad"),
        name = "MaruCode",
        baseUrl = "https://api.muteki.site/v1",
        apiKey = "",
        enabled = false,
        builtIn = true,
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
    ProviderSetting.Claude(
        id = Uuid.parse("b4deabea-20fb-4101-a74c-65679c7e4754"),
        name = "MiniMax",
        baseUrl = "https://api.minimaxi.com/anthropic/v1",
        apiKey = "",
        enabled = false,
        builtIn = true,
    ),
    ProviderSetting.OpenAI(
        id = Uuid.parse("a2bafe83-eaf8-47bf-a8c7-3dd82d89f637"),
        name = "MIMO",
        baseUrl = "https://api.xiaomimimo.com/v1",
        apiKey = "",
        enabled = false,
        builtIn = true,
    ),
)
