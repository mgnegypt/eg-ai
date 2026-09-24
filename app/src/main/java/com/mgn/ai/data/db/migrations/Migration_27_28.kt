package com.mgn.ai.data.db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Adds automation workflow tables (workflows + workflow_runs).
 *
 * Schema adapted from ExTV/rikkahub-agent (AGPL-3.0, same license); DDL
 * mirrors the Room entities exactly.
 */
val Migration_27_28 = object : Migration(27, 28) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `workflows` (
                `id` TEXT NOT NULL,
                `name` TEXT NOT NULL,
                `description` TEXT,
                `enabled` INTEGER NOT NULL DEFAULT 1,
                `definitionJson` TEXT NOT NULL,
                `createdAtMs` INTEGER NOT NULL,
                `updatedAtMs` INTEGER NOT NULL,
                `lastRunAtMs` INTEGER,
                `lastRunStatus` TEXT,
                `lastRunError` TEXT,
                `runsTodayCount` INTEGER NOT NULL DEFAULT 0,
                `runsTodayDate` TEXT NOT NULL DEFAULT '',
                PRIMARY KEY(`id`)
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `workflow_runs` (
                `rowId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `workflowId` TEXT NOT NULL,
                `firedAtMs` INTEGER NOT NULL,
                `status` TEXT NOT NULL,
                `durationMs` INTEGER NOT NULL,
                `errorMessage` TEXT,
                PRIMARY KEY(`rowId`)
            )
            """.trimIndent()
        )
        db.execSQL(
            "CREATE INDEX IF NOT EXISTS `index_workflow_runs_workflowId_firedAtMs` ON `workflow_runs` (`workflowId`, `firedAtMs`)"
        )
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `agent_runs` (
                `id` TEXT NOT NULL,
                `kind` TEXT NOT NULL,
                `domain_id` TEXT NOT NULL,
                `parent_run_id` TEXT,
                `status` TEXT NOT NULL,
                `created_at_ms` INTEGER NOT NULL,
                `updated_at_ms` INTEGER NOT NULL,
                `started_at_ms` INTEGER,
                `finished_at_ms` INTEGER,
                `last_error` TEXT,
                `metadata_json` TEXT,
                PRIMARY KEY(`id`)
            )
            """.trimIndent()
        )
        db.execSQL(
            "CREATE INDEX IF NOT EXISTS `idx_runs_status` ON `agent_runs` (`status`)"
        )
        db.execSQL(
            "CREATE INDEX IF NOT EXISTS `idx_runs_kind_dom` ON `agent_runs` (`kind`, `domain_id`)"
        )
        db.execSQL(
            "CREATE INDEX IF NOT EXISTS `idx_runs_parent` ON `agent_runs` (`parent_run_id`)"
        )
        db.execSQL(
            "CREATE INDEX IF NOT EXISTS `idx_runs_updated_at` ON `agent_runs` (`updated_at_ms`)"
        )
    }
}
