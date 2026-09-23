package com.mgn.ai.data.knowledge

import com.mgn.ai.knowledge.data.dao.KnowledgeChunkDao
import com.mgn.ai.knowledge.retrieval.KeywordSearchResult
import com.mgn.ai.knowledge.retrieval.KeywordSearcher

/**
 * Substring-based [KeywordSearcher] fallback. No FTS index required;
 * results are capped to keep full-table scans cheap.
 *
 * Adapted from Inonvation/rikkahub (AGPL-3.0, same license).
 */
class SimpleKeywordSearcher(
    private val chunkDao: KnowledgeChunkDao,
) : KeywordSearcher {
    override suspend fun search(
        query: String,
        knowledgeBaseId: String,
        topK: Int,
    ): List<KeywordSearchResult> {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return emptyList()
        return chunkDao.searchBySubstring(knowledgeBaseId, trimmed, topK).map { chunk ->
            KeywordSearchResult(
                chunk = chunk,
                rank = Int.MAX_VALUE,
                snippet = null,
                matchCount = chunk.content.split(trimmed, ignoreCase = true).size - 1,
            )
        }
    }
}
