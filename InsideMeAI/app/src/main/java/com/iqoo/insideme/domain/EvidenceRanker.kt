package com.iqoo.insideme.domain

class EvidenceRanker {
    fun rankAndDeduplicate(results: List<SearchResult>, topK: Int = 3): List<SearchResult> {
        // 1. Remove duplicates (Memories with exact same ID)
        val uniqueResults = results.distinctBy { it.memory.memory.id }
        
        // 2. Already ranked by Hybrid Score from Recall Engine
        // Here we just enforce the topK limit to ensure the context window doesn't overflow.
        return uniqueResults.take(topK)
    }
}
