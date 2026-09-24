package com.mgn.ai.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

/**
 * Re-arms automation after boot / update: fires workflow boot triggers.
 * (Cron rescheduling hooks in here once the scheduler lands.)
 *
 * Adapted from ExTV/rikkahub-agent (AGPL-3.0, same license), without the
 * Telegram bot parts.
 */
class WorkflowBootReceiver : BroadcastReceiver(), KoinComponent {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        if (action != Intent.ACTION_BOOT_COMPLETED &&
            action != Intent.ACTION_MY_PACKAGE_REPLACED &&
            action != "android.intent.action.QUICKBOOT_POWERON"
        ) return
        val pending = goAsync()
        scope.launch {
            try {
                val bootDispatcher: com.mgn.ai.workflow.trigger.WorkflowBootDispatcher =
                    com.mgn.ai.workflow.trigger.WorkflowBootDispatcher
                bootDispatcher.onBoot()
            } finally {
                pending.finish()
            }
        }
    }
}
