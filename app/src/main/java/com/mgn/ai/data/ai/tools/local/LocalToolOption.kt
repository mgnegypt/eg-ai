package com.mgn.ai.data.ai.tools.local

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class LocalToolOption {
    @Serializable
    @SerialName("javascript_engine")
    data object JavascriptEngine : LocalToolOption()

    @Serializable
    @SerialName("time_info")
    data object TimeInfo : LocalToolOption()

    @Serializable
    @SerialName("clipboard")
    data object Clipboard : LocalToolOption()

    @Serializable
    @SerialName("tts")
    data object Tts : LocalToolOption()

    @Serializable
    @SerialName("ask_user")
    data object AskUser : LocalToolOption()

    @Serializable
    @SerialName("screen_time")
    data object ScreenTime : LocalToolOption()

    @Serializable
    @SerialName("calendar")
    data object Calendar : LocalToolOption()

    @Serializable
    @SerialName("battery")
    data object Battery : LocalToolOption()

    @Serializable
    @SerialName("audio_info")
    data object AudioInfo : LocalToolOption()

    @Serializable
    @SerialName("sensors")
    data object Sensors : LocalToolOption()

    @Serializable
    @SerialName("storage_info")
    data object StorageInfo : LocalToolOption()

    @Serializable
    @SerialName("cron_jobs")
    data object CronJobs : LocalToolOption()

    @Serializable
    @SerialName("workflows")
    data object Workflows : LocalToolOption()
}
