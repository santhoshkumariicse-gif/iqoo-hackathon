package com.iqoo.insideme.integration

import com.iqoo.insideme.ai.GroundedResponse
import com.iqoo.insideme.ai.Claim

// ─── Session / Context ────────────────────────────────────────────────────────

data class SessionContext(
    val activeEntityId: String?,
    val activeProjectId: String?,
    val lastQuery: String?,
    val lastMemoryIds: List<String>,
    val updatedAt: Long
)

// ─── Orchestrator interface ───────────────────────────────────────────────────

interface InsideMeOrchestrator {
    suspend fun answerQuery(query: String, context: SessionContext): GroundedResponse
}

// ─── Query Parser ─────────────────────────────────────────────────────────────

data class ParsedQuery(
    val intent: String,      // RECALL, COMPARE, REPORT, STATUS
    val entityHint: String?,
    val timeWindow: String?, // e.g. "last 7 days"
    val rawText: String
)

class QueryParser {
    fun parse(rawQuery: String): ParsedQuery {
        val lower = rawQuery.lowercase()
        val intent = when {
            lower.contains("what changed") || lower.contains("diff") -> "COMPARE"
            lower.contains("report") || lower.contains("summary") -> "REPORT"
            lower.contains("status") || lower.contains("state") -> "STATUS"
            else -> "RECALL"
        }
        return ParsedQuery(
            intent = intent,
            entityHint = null,
            timeWindow = null,
            rawText = rawQuery
        )
    }
}

// ─── Temporal Logic Engine ────────────────────────────────────────────────────

data class TemporalDelta(
    val attribute: String,
    val before: String?,
    val after: String?,
    val confidence: Float
)

class TemporalLogicEngine {
    /**
     * Compares two attribute maps deterministically.
     * Pure Kotlin — no LLM, no hallucination risk.
     */
    fun delta(
        previous: Map<String, String>,
        current: Map<String, String>
    ): List<TemporalDelta> {
        val changes = mutableListOf<TemporalDelta>()
        val allKeys = previous.keys + current.keys
        for (key in allKeys.toSet()) {
            val prev = previous[key]
            val curr = current[key]
            if (prev != curr) {
                changes.add(TemporalDelta(attribute = key, before = prev, after = curr, confidence = 1.0f))
            }
        }
        return changes
    }
}

// ─── Action Validator ─────────────────────────────────────────────────────────

class ActionValidator {
    /**
     * Ensures no destructive actions are taken without explicit evidence IDs.
     * Returns null if valid, or an error message if not.
     */
    fun validate(actionType: String, evidenceIds: List<String>): String? {
        val destructive = setOf("DELETE", "OVERWRITE", "RESET", "WIPE")
        if (actionType.uppercase() in destructive && evidenceIds.isEmpty()) {
            return "BLOCKED: Destructive action '$actionType' requires at least one evidence ID."
        }
        return null
    }
}

// ─── Orchestrator Implementation ──────────────────────────────────────────────

class InsideMeOrchestratorImpl(
    private val queryParser: QueryParser,
    private val searchEngine: com.iqoo.insideme.domain.RecallSearchEngine,
    private val temporalEngine: TemporalLogicEngine,
    private val actionValidator: ActionValidator
) : InsideMeOrchestrator {

    override suspend fun answerQuery(query: String, context: SessionContext): GroundedResponse {
        val parsed = queryParser.parse(query)

        // Real keyword+semantic search against Room DB
        val results = searchEngine.search(query)

        if (results.isEmpty()) {
            return GroundedResponse(
                answer = "No memories found for: \"$query\".",
                confidence = "Low",
                evidenceIds = emptyList(),
                claims = emptyList(),
                limitations = "No relevant stored memories matched this query."
            )
        }

        val evidenceIds = results.map { it.memory.memory.id }
        val contextSummary = results.take(3).joinToString("\n") { r ->
            val m = r.memory.memory
            "• ${m.entityName}: ${m.visualDescription.take(120)} [${m.timestamp}]"
        }

        return GroundedResponse(
            answer = "Based on ${results.size} stored memory(ies):\n$contextSummary",
            confidence = if (results.first().score > 0.7f) "High" else "Moderate",
            evidenceIds = evidenceIds,
            claims = results.take(3).map { r ->
                Claim(
                    text = r.memory.memory.visualDescription.take(100),
                    type = "OBSERVED",
                    evidenceId = r.memory.memory.id
                )
            },
            limitations = if (parsed.intent == "COMPARE") "Change detection not yet available for this query." else null
        )
    }
}
