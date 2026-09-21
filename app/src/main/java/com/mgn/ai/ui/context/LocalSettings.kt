package com.mgn.ai.ui.context

import androidx.compose.runtime.staticCompositionLocalOf
import com.mgn.ai.data.datastore.Settings

val LocalSettings = staticCompositionLocalOf<Settings> {
    error("No SettingsStore provided")
}
