package com.iqoo.insideme.domain

enum class QueryIntent {
    SEARCH,
    SUMMARIZE,
    EXTRACT,
    TIMELINE,
    RELATIONSHIP,
    COMPARISON,
    UNKNOWN
}

class QueryAnalyzer {
    fun analyze(query: String): QueryIntent {
        val qLower = query.lowercase()
        return when {
            qLower.contains("what changed") || qLower.contains("difference") -> QueryIntent.COMPARISON
            qLower.contains("summarize") || qLower.contains("summary") -> QueryIntent.SUMMARIZE
            qLower.contains("what component") || qLower.contains("extract") || qLower.contains("what is visible") -> QueryIntent.EXTRACT
            qLower.contains("yesterday") || qLower.contains("last week") || qLower.contains("timeline") -> QueryIntent.TIMELINE
            qLower.contains("related to") || qLower.contains("relationship") -> QueryIntent.RELATIONSHIP
            qLower.contains("find") || qLower.contains("show me") || qLower.contains("what do i know") -> QueryIntent.SEARCH
            else -> QueryIntent.UNKNOWN // Fallback to a basic search or prompt for clarification
        }
    }
}
