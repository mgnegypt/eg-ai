package com.mgn.ai.data.ai.tools.local

import android.content.Context
import com.mgn.ai.ai.core.Tool
import com.mgn.ai.data.datastore.SettingsStore
import com.mgn.ai.data.event.AppEventBus
import com.mgn.ai.tts.provider.TTSManager

class LocalTools(
    private val context: Context,
    private val eventBus: AppEventBus,
    private val ttsManager: TTSManager,
    private val settingsStore: SettingsStore,
    private val scheduledJobRepository: com.mgn.ai.data.repository.ScheduledJobRepository,
    private val scheduledJobRunRepository: com.mgn.ai.data.repository.ScheduledJobRunRepository,
    private val cronJobScheduler: com.mgn.ai.service.CronJobScheduler,
) {
    val javascriptTool by lazy { buildJavascriptTool() }

    val timeTool by lazy { buildTimeInfoTool() }

    val clipboardTool by lazy { buildClipboardTool(context) }

    val ttsTool by lazy { buildTextToSpeechTool(eventBus, ttsManager, settingsStore) }

    val askUserTool by lazy { buildAskUserTool() }

    val screenTimeTool by lazy { buildScreenTimeTool(context, eventBus) }

    val calendarQueryTool by lazy { buildCalendarQueryTool(context) }

    val calendarCreateTool by lazy { buildCalendarCreateTool(context) }

    fun getTools(
        options: List<LocalToolOption>,
        invocationContext: ToolInvocationContext = ToolInvocationContext.EMPTY,
    ): List<Tool> {
        val tools = mutableListOf<Tool>()
        if (options.contains(LocalToolOption.JavascriptEngine)) {
            tools.add(javascriptTool)
        }
        if (options.contains(LocalToolOption.TimeInfo)) {
            tools.add(timeTool)
        }
        if (options.contains(LocalToolOption.Clipboard)) {
            tools.add(clipboardTool)
        }
        if (options.contains(LocalToolOption.Tts)) {
            tools.add(ttsTool)
        }
        if (options.contains(LocalToolOption.AskUser)) {
            tools.add(askUserTool)
        }
        if (options.contains(LocalToolOption.ScreenTime)) {
            tools.add(screenTimeTool)
        }
        if (options.contains(LocalToolOption.Calendar)) {
            tools.add(calendarQueryTool)
            tools.add(calendarCreateTool)
        }
        if (options.contains(LocalToolOption.Battery)) {
            tools.add(batteryTool(context))
        }
        if (options.contains(LocalToolOption.AudioInfo)) {
            tools.add(audioInfoTool(context))
        }
        if (options.contains(LocalToolOption.Sensors)) {
            tools.add(listSensorsTool(context))
            tools.add(readSensorTool(context))
        }
        if (options.contains(LocalToolOption.StorageInfo)) {
            tools.add(storageTool(context))
        }
        if (options.contains(LocalToolOption.CronJobs)) {
            tools.add(
                scheduleJobTool(
                    scheduledJobRepository,
                    cronJobScheduler,
                    settingsStore,
                    knownToolNamesProvider = { tools.map { it.name } },
                )
            )
            tools.add(listJobsTool(scheduledJobRepository))
            tools.add(deleteJobTool(scheduledJobRepository, scheduledJobRunRepository, cronJobScheduler))
            tools.add(pauseJobTool(scheduledJobRepository, cronJobScheduler))
            tools.add(resumeJobTool(scheduledJobRepository, cronJobScheduler))
            tools.add(triggerJobNowTool(scheduledJobRepository, cronJobScheduler))
            tools.add(getJobHistoryTool(scheduledJobRepository, scheduledJobRunRepository))
        }
        return tools
    }
}
