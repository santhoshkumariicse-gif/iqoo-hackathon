package com.iqoo.insideme.ai

import com.iqoo.insideme.domain.graph.AdvancedDetectedChange
import com.iqoo.insideme.ai.core.ProposedAction

data class Claim(
    val text: String,
    val type: String, // "OBSERVED" or "INFERRED"
    val evidenceId: String?
)

data class GroundedResponse(
    val answer: String,
    val confidence: String, // "High", "Moderate", "Low"
    val evidenceIds: List<String>,
    val claims: List<Claim> = emptyList(),
    val changes: List<AdvancedDetectedChange> = emptyList(),
    val actionProposal: ProposedAction? = null,
    val limitations: String? = null
)

interface LocalReasoningModel {
    suspend fun reason(query: String, context: String): GroundedResponse
}

interface LocalSpeechToText {
    suspend fun transcribeAudio(audioByteArray: ByteArray): String
}

// ==========================================
// PHASE 3 MOCKS
// ==========================================

class MockLocalReasoningModel : LocalReasoningModel {
    override suspend fun reason(query: String, context: String): GroundedResponse {
        // Simulating the Hallucination Protection logic
        if (context.contains("--- RETRIEVED MEMORIES ---\n\n")) {
            return GroundedResponse(
                answer = "I couldn't find enough evidence in your memories to answer that.",
                confidence = "Low",
                evidenceIds = emptyList(),
                claims = emptyList(),
                limitations = "No relevant evidence found."
            )
        }

        // Simulating an Evidence-Grounded response
        return GroundedResponse(
            answer = "The motor controller appears to be an L293D based on the retrieved photos and notes.",
            confidence = "High",
            evidenceIds = listOf("mem_001", "mem_002"),
            claims = listOf(
                Claim(text = "L293D text is visible on the board.", type = "OBSERVED", evidenceId = "mem_001"),
                Claim(text = "The component is likely safe to operate.", type = "INFERRED", evidenceId = null)
            ),
            limitations = null
        )
    }
}
