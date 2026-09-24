package com.mgn.ai.di

import android.content.Context
import com.mgn.ai.data.files.FileFolders
import com.mgn.ai.data.files.FilesManager
import com.mgn.ai.data.files.SkillManager
import com.mgn.ai.data.repository.ConversationRepository
import com.mgn.ai.data.repository.FavoriteRepository
import com.mgn.ai.data.repository.FolderRepository
import com.mgn.ai.data.repository.FilesRepository
import com.mgn.ai.data.repository.GenMediaRepository
import com.mgn.ai.data.agentrun.AgentRunRepository
import com.mgn.ai.data.db.AppDatabase
import com.mgn.ai.data.knowledge.SimpleKeywordSearcher
import com.mgn.ai.data.repository.MemoryRepository
import com.mgn.ai.data.repository.StorageManagerRepository
import com.mgn.ai.data.repository.WorkspaceRepository
import com.mgn.ai.knowledge.KnowledgeManager
import com.mgn.ai.knowledge.retrieval.KeywordSearcher
import com.mgn.ai.workspace.ProotShellRunner
import com.mgn.ai.workspace.RootfsInstaller
import com.mgn.ai.workspace.WorkspaceBindMount
import com.mgn.ai.workspace.WorkspaceManager
import org.koin.dsl.module
import java.io.File

val repositoryModule = module {
    single {
        ConversationRepository(get(), get(), get(), get(), get(), get())
    }

    single {
        FolderRepository(get(), get())
    }

    single {
        MemoryRepository(get())
    }

    single {
        GenMediaRepository(get())
    }

    single {
        FilesRepository(get())
    }

    single {
        FavoriteRepository(get())
    }

    single {
        com.mgn.ai.data.db.fts.KnowledgeChunkFtsManager(get())
    }

    single<KeywordSearcher> {
        com.mgn.ai.data.db.fts.FtsKeywordSearcher(get(), get<AppDatabase>().knowledgeChunkDao())
    }

    single {
        AgentRunRepository(get<AppDatabase>().agentRunDao())
    }

    single {
        com.mgn.ai.data.repository.ScheduledJobRepository(get<AppDatabase>().scheduledJobDao())
    }

    single {
        com.mgn.ai.data.repository.ScheduledJobRunRepository(get<AppDatabase>().scheduledJobRunDao())
    }

    single {
        com.mgn.ai.service.CronJobScheduler(get(), get())
    }

    single {
        KnowledgeManager(
            knowledgeBaseDao = get<AppDatabase>().knowledgeBaseDao(),
            knowledgeDocumentDao = get<AppDatabase>().knowledgeDocumentDao(),
            chunkDao = get<AppDatabase>().knowledgeChunkDao(),
            keywordSearcher = get(),
        )
    }

    single {
        StorageManagerRepository(
            context = get(),
            settingsStore = get(),
            conversationDAO = get(),
            conversationRepository = get(),
            conversationDeletionCoordinator = get(),
            messageNodeDAO = get(),
            genMediaDAO = get(),
        )
    }

    single {
        val context: Context = get()
        WorkspaceManager(
            baseDir = File(context.filesDir, "workspaces"),
            shellRunner = ProotShellRunner(
                nativeLibraryDir = File(context.applicationInfo.nativeLibraryDir),
            ),
            // 同一份挂载表既用于 PRoot 的 -b 参数, 也用于文件工具的路径解析, 避免两处漂移
            bindMounts = listOf(
                WorkspaceBindMount(
                    source = File(context.filesDir, FileFolders.SKILLS).apply { mkdirs() },
                    target = "/skills",
                ),
                WorkspaceBindMount(
                    source = File(context.filesDir, FileFolders.TOOL_OUTPUTS).apply { mkdirs() },
                    target = "/tool_outputs",
                ),
                WorkspaceBindMount(
                    source = File(context.filesDir, FileFolders.UPLOAD).apply { mkdirs() },
                    target = "/upload",
                ),
            ),
        )
    }

    single {
        RootfsInstaller(get())
    }

    single {
        WorkspaceRepository(get(), get(), get(), get())
    }

    single {
        FilesManager(get(), get(), get())
    }

    single {
        SkillManager(get(), get())
    }
}
