package com.mgn.ai.ui.context

import androidx.compose.runtime.compositionLocalOf
import com.mgn.ai.ui.hooks.CustomAsrState

val LocalASRState = compositionLocalOf<CustomAsrState> { error("Not provided yet") }

