package com.iqoo.insideme.integration

import kotlinx.coroutines.runBlocking

/**
 * Golden End-to-End Integration Test (Phase 04+)
 * Validates the SEE → REMEMBER → CHANGE → RECALL pipeline using
 * only real, deterministic logic — no fake AI calls, no hardcoded answers.
 */
class GoldenIntegrationTest {

    fun executeQueryPipelineTest() = runBlocking {
        val queryParser = QueryParser()
        val temporalEngine = TemporalLogicEngine()
        val actionValidator = ActionValidator()

        println("=== GOLDEN QUERY PARSER TEST ===")

        val q1 = queryParser.parse("What changed on the motor since last week?")
        println("Intent: ${q1.intent}") // COMPARE
        assert(q1.intent == "COMPARE") { "Expected COMPARE intent" }

        val q2 = queryParser.parse("Show me a summary report")
        println("Intent: ${q2.intent}") // REPORT
        assert(q2.intent == "REPORT") { "Expected REPORT intent" }

        println("=== GOLDEN TEMPORAL ENGINE TEST ===")
        val prev = mapOf("corrosion" to "none", "temperature" to "35C")
        val curr = mapOf("corrosion" to "visible", "temperature" to "35C", "status" to "degraded")
        val deltas = temporalEngine.delta(prev, curr)
        println("Detected ${deltas.size} changes:")
        deltas.forEach { println("  ${it.attribute}: '${it.before}' -> '${it.after}'") }
        assert(deltas.size == 2) { "Expected 2 changes (corrosion + status)" }

        println("=== GOLDEN ACTION VALIDATOR TEST ===")
        val blockMsg = actionValidator.validate("DELETE", emptyList())
        assert(blockMsg != null) { "Destructive action without evidence should be blocked" }
        println("Blocked: $blockMsg")

        val allowMsg = actionValidator.validate("CREATE_REPORT", emptyList())
        assert(allowMsg == null) { "Non-destructive action should be allowed" }
        println("Allowed: CREATE_REPORT")

        println("=== ALL GOLDEN TESTS PASSED ===")
    }
}

fun main() {
    GoldenIntegrationTest().executeQueryPipelineTest()
}
