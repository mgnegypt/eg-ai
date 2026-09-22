package com.mgn.ai.ai.provider

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import okhttp3.Headers

@Serializable
data class TextRequestHeader(
    val name: String,
    val value: String,
)

/**
 * A dry-run preview of the exact HTTP request that would be sent to the
 * provider for a text generation call. Used by the chat runtime inspector.
 *
 * Adapted from rikkahub-lune (AGPL-3.0, same license). Unlike the original,
 * secret header values are redacted here so API keys never appear in the UI.
 */
@Serializable
data class TextRequestPreview(
    val providerName: String,
    val apiName: String,
    val method: String = "POST",
    val url: String,
    val stream: Boolean,
    val headers: List<TextRequestHeader>,
    val body: JsonObject,
)

fun Headers.toPreviewHeaders(): List<TextRequestHeader> {
    return List(size) { index ->
        TextRequestHeader(
            name = name(index),
            value = value(index),
        )
    }
}

/** Masks a secret for display, keeping only a short suffix for identification. */
fun String.redactedSecret(visibleSuffixLength: Int = 4): String {
    val suffix = takeLast(visibleSuffixLength)
    return if (length <= visibleSuffixLength) "••••" else "••••$suffix"
}
