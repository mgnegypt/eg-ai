package com.mgn.ai.ui.hooks

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow


const val PREFS_NAME = "mgnai.preferences"
private const val LEGACY_PREFS_NAME = "rikkahub.preferences"
private const val KEY_PREFS_MIGRATED = "mgnai_prefs_migrated"

/**
 * One-time migration of settings from the legacy preferences file.
 * Safe to call on every startup; runs only once.
 */
fun Context.migrateLegacyPreferences() {
    val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    if (prefs.getBoolean(KEY_PREFS_MIGRATED, false)) return
    val legacy = getSharedPreferences(LEGACY_PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit {
        legacy.all.forEach { (key, value) ->
            when (value) {
                is Boolean -> putBoolean(key, value)
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is Float -> putFloat(key, value)
                is Set<*> -> {
                    @Suppress("UNCHECKED_CAST")
                    putStringSet(key, value as Set<String>)
                }
            }
        }
        putBoolean(KEY_PREFS_MIGRATED, true)
    }
}

@Composable
fun rememberSharedPreferenceString(
    keyForString: String,
    defaultValue: String? = null
): MutableState<String?> {
    val context = LocalContext.current
    val prefs = remember {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    val stateFlow =
        remember(keyForString, defaultValue) { prefs.getStringFlowForKey(keyForString, defaultValue) }
    val state by stateFlow.collectAsStateWithLifecycle(prefs.getString(keyForString, defaultValue))
    val currentState = rememberUpdatedState(state)
    return remember {
        object : MutableState<String?> {
            override var value: String?
                get() = currentState.value
                set(value) {
                    prefs.edit { putString(keyForString, value) }
                }

            override fun component1(): String? = value
            override fun component2(): (String?) -> Unit = { value = it }
        }
    }
}

@Composable
fun rememberSharedPreferenceBoolean(
    keyForBoolean: String,
    defaultValue: Boolean = false
): MutableState<Boolean> {
    val context = LocalContext.current
    val prefs = remember {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    val stateFlow =
        remember(keyForBoolean, defaultValue) { prefs.getBooleanFlowForKey(keyForBoolean, defaultValue) }
    val state by stateFlow.collectAsStateWithLifecycle(prefs.getBoolean(keyForBoolean, defaultValue))
    val currentState = rememberUpdatedState(state)
    return remember {
        object : MutableState<Boolean> {
            override var value: Boolean
                get() = currentState.value
                set(value) {
                    prefs.edit { putBoolean(keyForBoolean, value) }
                }

            override fun component1(): Boolean = value
            override fun component2(): (Boolean) -> Unit = { value = it }
        }
    }
}

fun Context.writeStringPreference(key: String, value: String?) {
    getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
        putString(key, value)
    }
}

fun Context.readStringPreference(key: String, defaultValue: String? = null): String? {
    return getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getString(key, defaultValue)
}

fun Context.writeBooleanPreference(key: String, value: Boolean) {
    getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
        putBoolean(key, value)
    }
}

fun Context.readBooleanPreference(key: String, defaultValue: Boolean = false): Boolean {
    return getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getBoolean(key, defaultValue)
}

fun SharedPreferences.getStringFlowForKey(keyForString: String, defaultValue: String? = null) =
    callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (keyForString == key) {
                trySend(getString(key, defaultValue))
            }
        }
        registerOnSharedPreferenceChangeListener(listener)
        if (contains(keyForString)) {
            send(
                getString(
                    keyForString,
                    defaultValue
                )
            ) // if you want to emit an initial pre-existing value
        }
        awaitClose { unregisterOnSharedPreferenceChangeListener(listener) }
    }.buffer(Channel.UNLIMITED) // so trySend never fails

fun SharedPreferences.getBooleanFlowForKey(keyForBoolean: String, defaultValue: Boolean = false) =
    callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (keyForBoolean == key) {
                trySend(getBoolean(key, defaultValue))
            }
        }
        registerOnSharedPreferenceChangeListener(listener)
        if (contains(keyForBoolean)) {
            send(
                getBoolean(
                    keyForBoolean,
                    defaultValue
                )
            ) // if you want to emit an initial pre-existing value
        }
        awaitClose { unregisterOnSharedPreferenceChangeListener(listener) }
    }.buffer(Channel.UNLIMITED) // so trySend never fails
