package com.mgn.ai.data.db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Adds scheduled-jobs tables (scheduled_jobs + scheduled_job_runs).
 *
 * Schema adapted from ExTV/rikkahub-agent (AGPL-3.0, same license); DDL
 * mirrors the Room entities exactly.
 */
val Migration_28_29 = object : Migration(28, 29) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `scheduled_jobs` (
                `id` TEXT NOT NULL,
                `name` TEXT NOT NULL,
                `prompt` TEXT,
                `assistantId` TEXT NOT NULL,
                `scheduleType` TEXT NOT NULL,
                `atUnixMs` INTEGER,
                `intervalSeconds` INTEGER,
                `enabled` INTEGER NOT NULL,
                `createdAtMs` INTEGER NOT NULL,
                `lastRunAtMs` INTEGER,
                `nextRunAtMs` INTEGER,
                `mode` TEXT NOT NULL,
                `actionsJson` TEXT,
                `cronExpression` TEXT,
                `timezone` TEXT,
                `startAtUnixMs` INTEGER,
                `endAtUnixMs` INTEGER,
                `maxRuns` INTEGER,
                `runsSoFar` INTEGER NOT NULL,
                `catchup` TEXT NOT NULL,
                `description` TEXT,
                `tags` TEXT,
                PRIMARY KEY(`id`)
            )
            """.trimIndent()
        )
        db.execSQL(
            "CREATE INDEX IF NOT EXISTS `index_scheduled_jobs_enabled` ON `scheduled_jobs` (`enabled`)"
        )
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `scheduled_job_runs` (
                `id` TEXT NOT NULL,
                `jobId` TEXT NOT NULL,
                `mode` TEXT NOT NULL,
                `scheduledAtMs` INTEGER NOT NULL,
                `startedAtMs` INTEGER NOT NULL,
                `finishedAtMs` INTEGER,
                `outcome` TEXT NOT NULL,
                `conversationId` TEXT,
                `errorMessage` TEXT,
                PRIMARY KEY(`id`)
            )
            """.trimIndent()
        )
        db.execSQL(
            "CREATE INDEX IF NOT EXISTS `index_scheduled_job_runs_jobId_startedAtMs` ON `scheduled_job_runs` (`jobId`, `startedAtMs`)"
        )
        db.execSQL(
            "CREATE INDEX IF NOT EXISTS `index_scheduled_job_runs_jobId_outcome` ON `scheduled_job_runs` (`jobId`, `outcome`)"
        )
    }
}
