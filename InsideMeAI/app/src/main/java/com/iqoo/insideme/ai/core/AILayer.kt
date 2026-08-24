package com.iqoo.insideme.ai.core

import com.iqoo.insideme.domain.graph.Observation

data class DetectedChange(
    val attribute: String,
    val previousValue: String?,
    val currentValue: String?,
    val confidence: Float
)

data class ProposedAction(
    val type: ActionType,
    val targetEntityId: String,
    val description: String,
    val requiresApproval: Boolean = true
)

enum class ActionType {
    SCHEDULE_MAINTENANCE, CREATE_REPORT, FLAG_FOR_REVIEW, ESCALATE, ARCHIVE
}

/**
 * Vision Component – Extracts structured facts from raw image bytes.
 * Does NOT write natural language. Does NOT claim NPU/GPU acceleration.
 */
interface VisionAI {
    suspend fun analyzeImage(imageBytes: ByteArray): Observation
}

/**
 * Embedding Component – Converts text to a float vector.
 */
interface EmbeddingEngine {
    suspend fun embed(text: String): FloatArray
}

/**
 * Deterministic Temporal Engine.
 * Pure Kotlin: computes attribute deltas between observations.
 * The LLM is NEVER asked to hallucinate what changed from raw text.
 */
class TemporalEngine {
    fun compareObservations(
        previous: Observation,
        current: Observation
    ): List<DetectedChange> {
        val changes = mutableListOf<DetectedChange>()
        current.attributes.forEach { (key, currVal) ->
            val prevVal = previous.attributes[key]
            if (prevVal != currVal) {
                changes.add(
                    DetectedChange(
                        attribute = key,
                        previousValue = prevVal,
                        currentValue = currVal,
                        confidence = 1.0f
                    )
                )
            }
        }
        return changes
    }
}
