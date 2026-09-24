package com.mgn.ai.di

import com.mgn.ai.ui.pages.assistant.AssistantVM
import com.mgn.ai.ui.pages.assistant.detail.AssistantDetailVM
import com.mgn.ai.ui.pages.backup.BackupVM
import com.mgn.ai.ui.pages.chat.ChatDrawerVM
import com.mgn.ai.ui.pages.chat.ChatVM
import com.mgn.ai.ui.pages.debug.DebugVM
import com.mgn.ai.ui.pages.favorite.FavoriteVM
import com.mgn.ai.ui.pages.assistant.groupchat.GroupChatTemplateDetailVM
import com.mgn.ai.ui.pages.chat.PromptOptimizeVM
import com.mgn.ai.ui.pages.search.SearchVM
import com.mgn.ai.ui.pages.storage.StorageCategoryVM
import com.mgn.ai.ui.pages.storage.StorageManagerVM
import com.mgn.ai.ui.pages.history.HistoryVM
import com.mgn.ai.ui.pages.stats.StatsVM
import com.mgn.ai.ui.pages.imggen.ImgGenVM
import com.mgn.ai.ui.pages.extensions.PromptVM
import com.mgn.ai.ui.pages.extensions.QuickMessagesVM
import com.mgn.ai.ui.pages.extensions.skills.SkillDetailVM
import com.mgn.ai.ui.pages.extensions.skills.SkillsVM
import com.mgn.ai.ui.pages.extensions.workspace.WorkspaceDetailVM
import com.mgn.ai.ui.pages.extensions.workspace.WorkspaceVM
import com.mgn.ai.ui.pages.setting.SettingVM
import com.mgn.ai.ui.pages.share.handler.ShareHandlerVM
import com.mgn.ai.ui.pages.translator.TranslatorVM
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<ChatVM> { params ->
        ChatVM(
            id = params.get(),
            context = get(),
            settingsStore = get(),
            conversationRepo = get(),
            chatService = get(),
            updateChecker = get(),
            analytics = get(),
            filesManager = get(),
            favoriteRepository = get(),
        )
    }
    viewModelOf(::ChatDrawerVM)
    viewModelOf(::SettingVM)
    viewModelOf(::DebugVM)
    viewModelOf(::HistoryVM)
    viewModelOf(::AssistantVM)
    viewModel<AssistantDetailVM> {
        AssistantDetailVM(
            id = it.get(),
            settingsStore = get(),
            memoryRepository = get(),
            filesManager = get(),
            skillManager = get(),
            workspaceRepository = get(),
        )
    }
    viewModelOf(::TranslatorVM)
    viewModel<ShareHandlerVM> {
        ShareHandlerVM(
            text = it.get(),
            settingsStore = get(),
        )
    }
    viewModelOf(::BackupVM)
    viewModelOf(::ImgGenVM)
    viewModelOf(::PromptVM)
    viewModelOf(::QuickMessagesVM)
    viewModelOf(::SkillsVM)
    viewModelOf(::SkillDetailVM)
    viewModelOf(::WorkspaceVM)
    viewModel<WorkspaceDetailVM> {
        WorkspaceDetailVM(
            id = it.get(),
            repository = get(),
            terminalSessionManager = get(),
        )
    }
    viewModelOf(::FavoriteVM)
    viewModelOf(::SearchVM)
    viewModelOf(::StatsVM)
    viewModelOf(::PromptOptimizeVM)
    viewModelOf(::StorageManagerVM)
    viewModelOf(::com.mgn.ai.workflow.ui.WorkflowsViewModel)
    viewModelOf(::com.mgn.ai.ui.pages.setting.scheduledjobs.ScheduledJobsViewModel)
    viewModel<StorageCategoryVM> {
        StorageCategoryVM(
            categoryKey = it.get(),
            settingsStore = get(),
            storageRepo = get(),
        )
    }
    viewModel<GroupChatTemplateDetailVM> {
        GroupChatTemplateDetailVM(
            id = it.get(),
            settingsStore = get(),
            workspaceRepository = get(),
        )
    }
}
