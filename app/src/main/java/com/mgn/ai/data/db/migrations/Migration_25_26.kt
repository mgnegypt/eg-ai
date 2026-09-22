package com.mgn.ai.data.db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Adds per-workspace agent customization columns.
 *
 * Adapted from rikkahub-lune (AGPL-3.0, same license). Defensive: only adds
 * columns that are missing, so databases coming from either lineage upgrade
 * cleanly.
 */
val Migration_25_26 = object : Migration(25, 26) {
    override fun migrate(db: SupportSQLiteDatabase) {
        val columns = db.query("PRAGMA table_info(`workspaces`)").use { cursor ->
            val nameIndex = cursor.getColumnIndexOrThrow("name")
            buildSet {
                while (cursor.moveToNext()) {
                    add(cursor.getString(nameIndex))
                }
            }
        }
        if ("tool_enabled" !in columns) {
            db.execSQL("ALTER TABLE `workspaces` ADD COLUMN `tool_enabled` TEXT NOT NULL DEFAULT '{}'")
        }
        if ("system_prompt_enabled" !in columns) {
            db.execSQL("ALTER TABLE `workspaces` ADD COLUMN `system_prompt_enabled` INTEGER NOT NULL DEFAULT 1")
        }
        if ("system_prompt" !in columns) {
            db.execSQL("ALTER TABLE `workspaces` ADD COLUMN `system_prompt` TEXT NOT NULL DEFAULT ''")
        }
        if ("shell_compatibility_mode" !in columns) {
            db.execSQL("ALTER TABLE `workspaces` ADD COLUMN `shell_compatibility_mode` INTEGER NOT NULL DEFAULT 0")
        }
    }
}
