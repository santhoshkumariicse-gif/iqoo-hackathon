package com.iqoo.insideme.ai.core

data class Observation(
    val entityId: String,
    val timestamp: Long,
    val attributes: Map<String, String>,
    val confidence: Float
)

data class DetectedChange(
    val attribute: String,
    val previousValue: String?,
    val currentValue: String?,
    val confidence: Float
)

/**
 * 13.2 Vision Component
 * Extracts structured facts. Does NOT write natural language.
 */
interface VisionAI {
    suspend fun analyzeImage(imageBytes: ByteArray): Observation
}

/**
 * 13.4 Embedding Component
 * Converts text into a vector for hybrid semantic search.
 */
interface EmbeddingEngine {
    suspend fun embed(text: String): FloatArray
}

/**
 * 13.7 Deterministic Temporal Engine
 * Pure Kotlin code to compute the delta between observations.
 * The LLM must NEVER be asked to hallucinate what changed from raw text.
 */
class TemporalEngine {
    fun compareObservations(previous: Observation, current: Observation): List<DetectedChange> {
        val changes = mutableListOf<DetectedChange>()
        
        current.attributes.forEach { (key, currVal) ->
            val prevVal = previous.attributes[key]
            if (prevVal != currVal) {
                changes.add(
                    DetectedChange(
                        attribute = key,
                        previousValue = prevVal,
                        currentValue = currVal,
                        confidence = 0.95f // Mocked confidence
                    )
                )
            }
        }
        return changes
    }
}
