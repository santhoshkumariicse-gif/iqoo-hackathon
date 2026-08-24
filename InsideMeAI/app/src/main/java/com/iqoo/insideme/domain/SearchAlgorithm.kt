package com.iqoo.insideme.domain

import com.iqoo.insideme.ai.LocalEmbeddingModel
import com.iqoo.insideme.data.local.MemoryDao
import com.iqoo.insideme.data.local.MemoryWithRelations
import org.json.JSONArray
import kotlin.math.sqrt

data class SearchResult(
    val memory: MemoryWithRelations,
    val score: Float,
    val matchReason: String
)

class RecallSearchEngine(
    private val memoryDao: MemoryDao,
    private val embeddingModel: LocalEmbeddingModel
) {
    /**
     * Hybrid Search Algorithm:
     * Combines Semantic Similarity (Cosine) + Keyword Relevance
     */
    suspend fun search(query: String): List<SearchResult> {
        // 1. Generate query embedding
        val queryEmbedding = embeddingModel.embedText(query)
        
        // 2. Fetch all memories (In production with VSS, we would let SQLite do the semantic filter)
        val allMemories = memoryDao.getAllMemories()
        
        val scoredResults = mutableListOf<SearchResult>()
        
        for (item in allMemories) {
            val semScore = if (item.embedding != null && queryEmbedding.isNotEmpty()) {
                val storedVector = parseEmbedding(item.embedding.embeddingJson)
                cosineSimilarity(queryEmbedding, storedVector)
            } else {
                0f
            }
            
            // Keyword Match Score (basic heuristics)
            var kwScore = 0f
            val qLower = query.lowercase()
            
            if (item.memory.extractedText?.lowercase()?.contains(qLower) == true) kwScore += 0.4f
            if (item.memory.visualDescription?.lowercase()?.contains(qLower) == true) kwScore += 0.4f
            
            // Check tags
            if (item.tags.any { it.tag.lowercase() == qLower }) kwScore += 0.5f

            // Hybrid Scoring Formula
            // Semantic match is heavily weighted, but exact keyword hits boost the score
            val hybridScore = (semScore * 0.7f) + (kwScore * 0.3f)
            
            if (hybridScore > 0.3f) { // Arbitrary relevance threshold
                val reason = if (kwScore > 0f) {
                    "Matched: Keyword + Semantic Similarity (Score: ${(hybridScore * 100).toInt()}%)"
                } else {
                    "Matched: Semantic Similarity (Score: ${(hybridScore * 100).toInt()}%)"
                }
                
                scoredResults.add(SearchResult(item, hybridScore, reason))
            }
        }
        
        // Rank results by highest score
        return scoredResults.sortedByDescending { it.score }
    }
    
    private fun parseEmbedding(jsonStr: String): FloatArray {
        return try {
            val jsonArray = JSONArray(jsonStr)
            FloatArray(jsonArray.length()) { i -> jsonArray.getDouble(i).toFloat() }
        } catch (e: Exception) {
            FloatArray(0)
        }
    }

    private fun cosineSimilarity(vec1: FloatArray, vec2: FloatArray): Float {
        if (vec1.isEmpty() || vec2.isEmpty() || vec1.size != vec2.size) return 0f
        
        var dotProduct = 0f
        var norm1 = 0f
        var norm2 = 0f
        
        for (i in vec1.indices) {
            dotProduct += vec1[i] * vec2[i]
            norm1 += vec1[i] * vec1[i]
            norm2 += vec2[i] * vec2[i]
        }
        
        if (norm1 == 0f || norm2 == 0f) return 0f
        return dotProduct / (sqrt(norm1.toDouble()) * sqrt(norm2.toDouble())).toFloat()
    }
}
