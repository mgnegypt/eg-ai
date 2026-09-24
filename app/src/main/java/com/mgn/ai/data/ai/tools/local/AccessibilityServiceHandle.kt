package com.mgn.ai.data.ai.tools.local

import android.content.ComponentName
import android.content.Context
import android.provider.Settings

/**
 * Accessibility-service status bridge for workflow trigger pre-flight checks.
 *
 * Adapted from ExTV/rikkahub-agent (AGPL-3.0, same license). MGN AI does not
 * ship an accessibility service, so [isRunning] is always false and
 * app-launch triggers report a clear setup message instead of firing.
 */
object AccessibilityServiceHandle {

    /** True iff one of our components is listed in the secure setting. */
    fun isEnabledInSettings(ctx: Context): Boolean {
        val enabled = Settings.Secure.getString(
            ctx.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false
        return enabled.split(":").any { it.contains(ctx.packageName, ignoreCase = true) }
    }

    /** No accessibility service is shipped, so this is always false. */
    fun isRunning(): Boolean = false
}
