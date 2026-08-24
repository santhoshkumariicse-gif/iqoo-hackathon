package com.iqoo.insideme.integration

import com.iqoo.insideme.ai.*
import com.iqoo.insideme.ai.core.*
import com.iqoo.insideme.domain.graph.*

// DTOs
enum class CaptureType { IMAGE, AUDIO, TEXT, DOCUMENT }
data class CaptureInput(
    val type: CaptureType,
    val data: ByteArray,
    val timestamp: Long,
    val metadata: Map<String, String>
)

data class CaptureResult(
    val memoryId: String,
    val entityIds: List<String>,
    val observations: List<Observation>,
    val confidence: Float,
    val processingTimeMs: Long
)

enum class QueryIntent { RECALL, CHANGE, TIMELINE, EVIDENCE, ACTION, GENERAL }

data class SessionContext(
    val activeEntityId: String?,
    val activeProjectId: String?,
    val lastQuery: String?,
    val lastMemoryIds: List<String>,
    val updatedAt: Long
)

/**
 * 15.9 Rule-based intent detection to save compute
 */
class QueryParser {
    fun parseIntent(query: String): QueryIntent {
        val q = query.lowercase()
        return when {
            q.contains("what changed") -> QueryIntent.CHANGE
            q.contains("what should i do") || q.contains("action") -> QueryIntent.ACTION
            q.contains("show previous") || q.contains("recall") -> QueryIntent.RECALL
            q.contains("history") || q.contains("timeline") -> QueryIntent.TIMELINE
            q.contains("why") || q.contains("evidence") -> QueryIntent.EVIDENCE
            else -> QueryIntent.GENERAL
        }
    }
}

interface InsideMeOrchestrator {
    suspend fun processCapture(capture: CaptureInput): CaptureResult
    suspend fun answerQuery(query: String, context: SessionContext): GroundedResponse
    suspend fun createAction(proposal: ProposedAction): Action
}

class OrchestratorImpl(
    private val visionAi: VisionAI,
    private val temporalEngine: TemporalLogicEngine,
    private val actionValidator: ActionValidator,
    private val queryParser: QueryParser
) : InsideMeOrchestrator {

    override suspend fun processCapture(capture: CaptureInput): CaptureResult {
        // MOCK INTEGRATION FLOW
        val start = System.currentTimeMillis()
        
        // 1. Vision Extraction
        val obs = visionAi.analyzeImage(capture.data)
        
        // 2. Mock memory persistence & entity resolution happening here...
        
        return CaptureResult(
            memoryId = "mem_123",
            entityIds = listOf(obs.entityId),
            observations = listOf(obs),
            confidence = obs.confidence,
            processingTimeMs = System.currentTimeMillis() - start
        )
    }

    override suspend fun answerQuery(query: String, context: SessionContext): GroundedResponse {
        val intent = queryParser.parseIntent(query)
        
        // Context Resolution
        val targetEntity = context.activeEntityId ?: "ent_a17"
        
        // If "What changed?", route to Temporal Engine
        if (intent == QueryIntent.CHANGE) {
            val mockPrevious = Observation("obs_1", targetEntity, 1692345600000, mapOf("corrosion" to "none"), "mem_1", 0.9f)
            val mockCurrent = Observation("obs_2", targetEntity, 1692864000000, mapOf("corrosion" to "visible"), "mem_2", 0.95f)
            
            val changes = temporalEngine.detectChanges(targetEntity, listOf(mockPrevious, mockCurrent))
            
            // LLM formats the deterministic change
            return GroundedResponse(
                answer = "Panel A17 appears more degraded. Corrosion changed from none to visible.",
                confidence = "High",
                evidenceIds = listOf("obs_1", "obs_2", "mem_1", "mem_2"),
                changes = changes,
                actionProposal = ProposedAction(ActionType.CREATE_TASK, "Increased corrosion detected", listOf("obs_1", "obs_2"), true)
            )
        }
        
        return GroundedResponse("General answer", "Moderate", emptyList())
    }

    override suspend fun createAction(proposal: ProposedAction): Action {
        if (!actionValidator.validateAction(proposal)) {
            throw IllegalStateException("Action requires user confirmation")
        }
        
        return Action(
            id = "act_1",
            type = proposal.type,
            createdAt = System.currentTimeMillis(),
            entityId = "ent_a17",
            status = ActionStatus.OPEN,
            evidenceIds = proposal.evidenceIds
        )
    }
}
