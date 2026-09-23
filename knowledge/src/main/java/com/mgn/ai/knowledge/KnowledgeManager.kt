package com.mgn.ai.knowledge

import com.mgn.ai.knowledge.data.dao.KnowledgeBaseDao
import com.mgn.ai.knowledge.data.dao.KnowledgeChunkDao
import com.mgn.ai.knowledge.data.dao.KnowledgeDocumentDao
import com.mgn.ai.knowledge.data.repository.KnowledgeBaseRepository
import com.mgn.ai.knowledge.data.repository.KnowledgeDocumentRepository
import com.mgn.ai.knowledge.retrieval.KeywordSearcher
import com.mgn.ai.knowledge.retrieval.Reranker
import com.mgn.ai.knowledge.retrieval.RetrievalPipeline
import com.mgn.ai.knowledge.retrieval.RetrievalResult
import com.mgn.ai.knowledge.vector.VectorStore

class KnowledgeManager(
    private val knowledgeBaseDao: KnowledgeBaseDao,
    private val knowledgeDocumentDao: KnowledgeDocumentDao,
    val chunkDao: KnowledgeChunkDao,
    private val keywordSearcher: KeywordSearcher,
) {
    val baseRepository = KnowledgeBaseRepository(knowledgeBaseDao)
    val documentRepository = KnowledgeDocumentRepository(knowledgeDocumentDao)

    private val vectorStore = VectorStore(chunkDao)
    private val retrievalPipeline = RetrievalPipeline(
        chunkDao = chunkDao,
        vectorStore = vectorStore,
        keywordSearcher = keywordSearcher,
    )

    suspend fun search(
        query: String,
        queryEmbedding: FloatArray?,
        knowledgeBaseId: String,
        topK: Int = 10,
        similarityThreshold: Float = 0f,
        reranker: Reranker? = null,
        keywordWeight: Float = 1f,
        mmrLambda: Float = 0.7f,
    ): List<RetrievalResult> {
        return retrievalPipeline.search(
            query = query,
            queryEmbedding = queryEmbedding,
            knowledgeBaseId = knowledgeBaseId,
            topK = topK,
            similarityThreshold = similarityThreshold,
            reranker = reranker,
            keywordWeight = keywordWeight,
            mmrLambda = mmrLambda,
        )
    }

    suspend fun semanticSearch(
        query: String,
        queryEmbedding: FloatArray,
        knowledgeBaseId: String,
        topK: Int = 10,
        similarityThreshold: Float = 0f,
        reranker: Reranker? = null,
        mmrLambda: Float = 0.7f,
    ): List<RetrievalResult> {
        return retrievalPipeline.semanticSearch(
            query = query,
            queryEmbedding = queryEmbedding,
            knowledgeBaseId = knowledgeBaseId,
            topK = topK,
            similarityThreshold = similarityThreshold,
            reranker = reranker,
            mmrLambda = mmrLambda,
        )
    }

    suspend fun keywordSearch(
        query: String,
        knowledgeBaseId: String,
        topK: Int = 10,
    ): List<RetrievalResult> {
        return retrievalPipeline.keywordSearch(
            query = query,
            knowledgeBaseId = knowledgeBaseId,
            topK = topK,
        )
    }

    /**
     * 清除指定知识库的向量缓存。文档导入、重处理、删除后调用。
     */
    fun invalidateVectorCache(knowledgeBaseId: String) {
        vectorStore.invalidateCache(knowledgeBaseId)
    }

    /**
     * 清除所有知识库的向量缓存。
     */
    fun invalidateAllVectorCache() {
        vectorStore.invalidateAll()
    }
}