package com.mgn.ai.data.ai.tools.local

import android.content.ComponentName
import android.content.Context
import android.provider.Settings

/**
 * Notification-listener status bridge for workflow trigger pre-flight checks.
 *
 * Adapted from ExTV/rikkahub-agent (AGPL-3.0, same license). MGN AI does not
 * ship a listener service, so [isBound] is always false and notification
 * triggers report a clear setup message instead of firing.
 */
object NotificationListenerHandle {

    /** True iff a listener component were listed in the secure setting. */
    fun isEnabledInSettings(ctx: Context): Boolean {
        val enabled = Settings.Secure.getString(
            ctx.contentResolver,
            "enabled_notification_listeners"
        ) ?: return false
        return enabled.split(":").any { it.contains(ctx.packageName, ignoreCase = true) }
    }

    /** No listener service is shipped, so this is always false. */
    fun isBound(): Boolean = false
}
