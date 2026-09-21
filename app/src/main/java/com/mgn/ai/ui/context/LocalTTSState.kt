package com.mgn.ai.ui.context

import androidx.compose.runtime.compositionLocalOf
import com.mgn.ai.ui.hooks.CustomTtsState

val LocalTTSState = compositionLocalOf<CustomTtsState> { error("Not provided yet") }
