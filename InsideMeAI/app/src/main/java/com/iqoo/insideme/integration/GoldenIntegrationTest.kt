package com.iqoo.insideme.integration

import com.iqoo.insideme.ai.core.*
import kotlinx.coroutines.runBlocking

/**
 * 15.34 The Golden End-to-End Integration Test
 * Proves the system works without manual backend intervention.
 */
class GoldenIntegrationTest {

    fun executeGoldenDemoLoop() = runBlocking {
        // Setup Dependencies
        val visionAi = object : VisionAI {
            override suspend fun analyzeImage(imageBytes: ByteArray): Observation {
                return Observation("obs_mock", "ent_a17", System.currentTimeMillis(), mapOf("corrosion" to "visible"), "mem_mock", 0.9f)
            }
        }
        val orchestrator = OrchestratorImpl(visionAi, TemporalLogicEngine(), ActionValidator(), QueryParser())
        val context = SessionContext("ent_a17", null, null, emptyList(), System.currentTimeMillis())

        println("=== STARTING GOLDEN DEMO LOOP ===")

        // 1-7. Capture & Process
        println("1. Pointing camera at Panel A17...")
        val captureInput = CaptureInput(CaptureType.IMAGE, ByteArray(0), System.currentTimeMillis(), emptyMap())
        val captureResult = orchestrator.processCapture(captureInput)
        assert(captureResult.entityIds.contains("ent_a17"))
        println("SUCCESS: Captured and identified Entity: ${captureResult.entityIds.first()}")

        // 8-12. Ask "What changed?" and retrieve reasoning
        println("\n2. User asks: 'What changed?'")
        val answer = orchestrator.answerQuery("What changed?", context)
        assert(answer.changes.isNotEmpty())
        println("SUCCESS: Change detected: ${answer.changes.first().attribute} (${answer.changes.first().previousValue} -> ${answer.changes.first().currentValue})")
        println("AI REASONING: ${answer.answer}")

        // 13-16. Action Proposal & Confirmation
        println("\n3. AI Proposes Action...")
        val proposal = answer.actionProposal
        assert(proposal != null && proposal.type == ActionType.CREATE_TASK)
        println("PROPOSED: ${proposal?.type} - Reason: ${proposal?.reason}")
        
        println("4. User Confirms Action...")
        // User clicks "Approve" in UI, routing back to Orchestrator
        val actionResult = orchestrator.createAction(proposal!!)
        assert(actionResult.status == com.iqoo.insideme.domain.graph.ActionStatus.OPEN)
        println("SUCCESS: Task created and stored in memory. ID: ${actionResult.id}")
        
        println("=== GOLDEN LOOP COMPLETED ===")
    }
}

// Simple test runner for demo purposes
fun main() {
    GoldenIntegrationTest().executeGoldenDemoLoop()
}
