package com.iqoo.insideme.domain.graph

data class AdvancedDetectedChange(
    val entityId: String,
    val attribute: String,
    val previousValue: String?,
    val currentValue: String?,
    val previousObservationId: String,
    val currentObservationId: String,
    val confidence: Float
)

class TemporalLogicEngine {
    
    /**
     * 14.11 "What changed?" First-Class Query
     */
    fun detectChanges(entityId: String, observations: List<Observation>): List<AdvancedDetectedChange> {
        val changes = mutableListOf<AdvancedDetectedChange>()
        
        if (observations.size < 2) return changes
        
        // 1. Sort chronologically
        val sorted = observations.sortedBy { it.timestamp }
        
        // 2. Compare adjacent observations
        for (i in 1 until sorted.size) {
            val previous = sorted[i - 1]
            val current = sorted[i]
            
            // 3. Detect attribute differences
            current.attributes.forEach { (key, currVal) ->
                val prevVal = previous.attributes[key]
                if (prevVal != currVal) {
                    changes.add(
                        AdvancedDetectedChange(
                            entityId = entityId,
                            attribute = key,
                            previousValue = prevVal,
                            currentValue = currVal,
                            previousObservationId = previous.id,
                            currentObservationId = current.id,
                            confidence = 0.95f
                        )
                    )
                }
            }
        }
        
        return changes
    }
}

/**
 * 14.22 Relative-Time Engine
 */
data class TimeRange(val start: Long, val end: Long)

object TimeEngine {
    fun parseRelativeTime(query: String, currentTime: Long = System.currentTimeMillis()): TimeRange? {
        val oneDayMs = 86400000L
        return when {
            query.contains("today") -> TimeRange(currentTime - oneDayMs, currentTime)
            query.contains("yesterday") -> TimeRange(currentTime - (2 * oneDayMs), currentTime - oneDayMs)
            query.contains("last 7 days") -> TimeRange(currentTime - (7 * oneDayMs), currentTime)
            else -> null // Default to full history
        }
    }
}
