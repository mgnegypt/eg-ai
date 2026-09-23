package com.mgn.ai.data.db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Adds RAG knowledge-base tables (knowledge_base, knowledge_document,
 * knowledge_chunk) with indices and cascade foreign keys.
 *
 * Schema adapted from Inonvation/rikkahub (AGPL-3.0, same license); DDL
 * mirrors the Room entities in the :knowledge module exactly.
 */
val Migration_26_27 = object : Migration(26, 27) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `knowledge_base` (
                `id` TEXT NOT NULL,
                `name` TEXT NOT NULL,
                `description` TEXT NOT NULL,
                `embedding_model_id` TEXT,
                `rerank_model_id` TEXT,
                `chunk_size` INTEGER NOT NULL,
                `chunk_overlap` INTEGER NOT NULL,
                `chunk_strategy` TEXT NOT NULL,
                `top_k` INTEGER NOT NULL,
                `similarity_threshold` REAL NOT NULL,
                `use_hyde` INTEGER NOT NULL,
                `keyword_weight` REAL NOT NULL,
                `use_multiquery` INTEGER NOT NULL,
                `context_window` INTEGER NOT NULL,
                `mmr_lambda` REAL NOT NULL,
                `parent_chunk_size` INTEGER NOT NULL,
                `status` TEXT NOT NULL,
                `error` TEXT,
                `created_at` INTEGER NOT NULL,
                `updated_at` INTEGER NOT NULL,
                PRIMARY KEY(`id`)
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `knowledge_document` (
                `id` TEXT NOT NULL,
                `knowledge_base_id` TEXT NOT NULL,
                `file_name` TEXT NOT NULL,
                `file_type` TEXT NOT NULL,
                `file_path` TEXT NOT NULL,
                `file_size` INTEGER NOT NULL,
                `file_hash` TEXT,
                `chunk_count` INTEGER NOT NULL,
                `status` TEXT NOT NULL,
                `error` TEXT,
                `created_at` INTEGER NOT NULL,
                `updated_at` INTEGER NOT NULL,
                PRIMARY KEY(`id`),
                FOREIGN KEY(`knowledge_base_id`) REFERENCES `knowledge_base`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
            )
            """.trimIndent()
        )
        db.execSQL(
            "CREATE INDEX IF NOT EXISTS `index_knowledge_document_knowledge_base_id` ON `knowledge_document` (`knowledge_base_id`)"
        )
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `knowledge_chunk` (
                `id` TEXT NOT NULL,
                `document_id` TEXT NOT NULL,
                `knowledge_base_id` TEXT NOT NULL,
                `chunk_index` INTEGER NOT NULL,
                `content` TEXT NOT NULL,
                `embedding` BLOB,
                `token_count` INTEGER NOT NULL,
                `metadata` TEXT NOT NULL,
                `context_prefix` TEXT NOT NULL,
                `parent_chunk_id` TEXT,
                PRIMARY KEY(`id`),
                FOREIGN KEY(`document_id`) REFERENCES `knowledge_document`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE,
                FOREIGN KEY(`knowledge_base_id`) REFERENCES `knowledge_base`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
            )
            """.trimIndent()
        )
        db.execSQL(
            "CREATE INDEX IF NOT EXISTS `index_knowledge_chunk_document_id` ON `knowledge_chunk` (`document_id`)"
        )
        db.execSQL(
            "CREATE INDEX IF NOT EXISTS `index_knowledge_chunk_knowledge_base_id` ON `knowledge_chunk` (`knowledge_base_id`)"
        )
    }
}
