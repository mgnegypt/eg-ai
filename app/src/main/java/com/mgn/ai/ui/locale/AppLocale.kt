package com.mgn.ai.ui.locale

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.mgn.ai.ui.hooks.PREFS_NAME
import java.util.Locale

object AppLanguage {
    const val SYSTEM = ""
    const val ENGLISH = "en"
    const val ARABIC = "ar"

    val ALL = listOf(SYSTEM, ENGLISH, ARABIC)
}

private const val KEY_APP_LANGUAGE = "app_language"

/**
 * Returns the stored app-language tag: "" (follow system), "en" or "ar".
 */
fun Context.getAppLanguageTag(): String {
    return getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .getString(KEY_APP_LANGUAGE, AppLanguage.SYSTEM) ?: AppLanguage.SYSTEM
}

/**
 * Persists the app-language tag and applies it immediately.
 *
 * Both the AndroidX per-app-locale API and a manual configuration-context
 * wrap are used so the UI refreshes on every API level (26+) regardless of
 * which Activity is in front. Callers should also recreate the current
 * Activity (see [Context.findActivity]) to force an instant UI refresh.
 */
fun Context.setAppLanguageTag(tag: String) {
    val normalized = when (tag) {
        AppLanguage.ENGLISH, AppLanguage.ARABIC -> tag
        else -> AppLanguage.SYSTEM
    }
    getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .edit()
        .putString(KEY_APP_LANGUAGE, normalized)
        .apply()
    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(normalized))
}

/**
 * Wraps [base] so its resources resolve strings in the stored app language.
 * Used from Application/Activities' attachBaseContext; works on API 26+.
 */
fun applyAppLocale(base: Context): Context {
    val tag = base.getAppLanguageTag()
    if (tag.isBlank()) return base
    val locale = Locale.forLanguageTag(tag)
    val config = Configuration(base.resources.configuration)
    config.setLocale(locale)
    config.setLayoutDirection(locale)
    return base.createConfigurationContext(config)
}

fun Context.findActivity(): Activity? {
    var context: Context? = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}
