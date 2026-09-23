package com.mgn.ai.di

import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.remoteconfig.remoteConfig
import kotlinx.serialization.json.Json
import com.mgn.ai.AppScope
import com.mgn.ai.data.ai.tools.local.LocalTools
import com.mgn.ai.data.ai.tools.ChatToolFactory
import com.mgn.ai.data.ai.subagent.SubAgentEngine
import com.mgn.ai.data.ai.subagent.SubAgentRunRegistry
import com.mgn.ai.data.ai.tools.TodoStorage
import com.mgn.ai.data.donation.DonationRepository
import com.mgn.ai.data.event.AppEventBus
import com.mgn.ai.data.repository.ConversationDeletionCoordinator
import com.mgn.ai.service.ChatNotificationManager
import com.mgn.ai.service.ChatService
import com.mgn.ai.ui.pages.extensions.workspace.WorkspaceTerminalSessionManager
import com.mgn.ai.utils.EmojiData
import com.mgn.ai.utils.EmojiUtils
import com.mgn.ai.utils.JsonInstant
import com.mgn.ai.utils.SoundEffectPlayer
import com.mgn.ai.utils.UpdateChecker
import com.mgn.ai.web.WebServerManager
import com.mgn.ai.tts.provider.TTSManager
import org.koin.dsl.module

val appModule = module {
    single<Json> { JsonInstant }

    single {
        AppEventBus()
    }

    single {
        LocalTools(get(), get(), get(), get())
    }

    single {
        UpdateChecker(
            client = get(),
            appScope = get(),
        )
    }

    single {
        AppScope()
    }

    single<EmojiData> {
        EmojiUtils.loadEmoji(get())
    }

    single {
        TTSManager(get())
    }

    single {
        Firebase.crashlytics
    }

    single {
        Firebase.analytics
    }

    single {
        Firebase.remoteConfig
    }

    single {
        DonationRepository(get())
    }

    single {
        SoundEffectPlayer(get())
    }

    single {
        WorkspaceTerminalSessionManager(get(), get())
    }

    // 生成通知与业务解耦：ChatService 只发事件，通知由这里消费；
    // createdAtStart 保证进程启动即订阅，否则后台生成的事件会因无订阅者而丢失
    single(createdAtStart = true) {
        ChatNotificationManager(
            context = get(),
            appScope = get(),
            eventBus = get(),
            settingsStore = get(),
        )
    }

    single { SubAgentRunRegistry() }
    single {
        SubAgentEngine(
            json = get(),
            registry = get(),
            providerManager = get(),
        )
    }

    single {
        TodoStorage(get())
    }

    single {
        ChatToolFactory(
            json = get(),
            memoryRepository = get(),
            conversationRepository = get(),
            localTools = get(),
            mcpManager = get(),
            skillManager = get(),
            workspaceRepository = get(),
            settingsStore = get(),
            subAgentEngine = get(),
            todoStorage = get(),
        )
    }

    single {
        ChatService(
            context = get(),
            appScope = get(),
            appEventBus = get(),
            settingsStore = get(),
            conversationRepo = get(),
            memoryRepository = get(),
            generationLoop = get(),
            translationHandler = get(),
            templateTransformer = get(),
            providerManager = get(),
            chatToolFactory = get(),
            mcpManager = get(),
            filesManager = get(),
            workspaceRepository = get(),
            folderRepository = get(),
            todoStorage = get()
        )
    }

    single<ConversationDeletionCoordinator> { get<ChatService>() }

    single {
        WebServerManager(
            context = get(),
            appScope = get(),
            chatService = get(),
            conversationRepo = get(),
            folderRepo = get(),
            settingsStore = get(),
            filesManager = get()
        )
    }
}
