package com.iqoo.insideme.usecases

import com.iqoo.insideme.ai.*
import com.iqoo.insideme.domain.core.*
import com.iqoo.insideme.domain.graph.KnowledgeGraph
import com.iqoo.insideme.repositories.MemoryRepository
import com.iqoo.insideme.workers.*

class CaptureMemoryUseCase(
    private val memoryRepo: MemoryRepository,
    private val workerFacade: BackgroundWorkerFacade
) {
    suspend operator fun invoke(uri: String, type: MemoryType): String {
        val memory = Memory(
            id = "mem_${System.currentTimeMillis()}",
            type = type,
            contentUri = uri,
            capturedAt = System.currentTimeMillis(),
            processingState = ProcessingState.CAPTURED,
            entityIds = emptyList(),
            metadata = emptyMap()
        )
        memoryRepo.save(memory)

        // Queue Background Analysis Pipeline
        workerFacade.enqueue(ProcessingJob("job_img_${memory.id}", memory.id, WorkType.IMAGE_ANALYSIS, priority = 1))
        workerFacade.enqueue(ProcessingJob("job_ocr_${memory.id}", memory.id, WorkType.OCR, priority = 1))
        workerFacade.enqueue(ProcessingJob("job_idx_${memory.id}", memory.id, WorkType.MEMORY_INDEX, priority = 2))
        workerFacade.enqueue(ProcessingJob("job_grf_${memory.id}", memory.id, WorkType.GRAPH_UPDATE, priority = 3))

        return memory.id
    }
}

data class QueryContext(
    val currentMemoryId: String?,
    val currentEntityId: String?,
    val recentMemoryIds: List<String>
)

class AskInsideMeUseCase(
    private val aiEngine: LocalAIEngine,
    private val graph: KnowledgeGraph,
    private val outputValidator: com.iqoo.insideme.inference.AIOutputValidator
) {
    /**
     * The unified Query Pipeline. (Phase 9 Critical Requirement)
     */
    suspend operator fun invoke(queryText: String, context: QueryContext): GroundedResponse {
        // 1. Context Resolution
        val targetEntityId = context.currentEntityId ?: "ent_a17" // Default to demo context if null
        
        // 2. Graph Traversal
        val graphContext = graph.searchConnected(targetEntityId)
        
        // 3. Evidence Ranking (Simulated Merge)
        val evidenceBundle = StringBuilder()
        evidenceBundle.append("Subject: ${graphContext.primaryEntity.name}\n")
        graphContext.relationships.forEach { rel ->
            val targetName = graphContext.relatedEntities.find { it.id == rel.targetId }?.name ?: "Unknown"
            evidenceBundle.append("- ${rel.type}: $targetName\n")
        }

        // 4. Prompt Sandbox Defense
        val safePrompt = outputValidator.buildSandboxedPrompt(
            systemInstructions = "You are InsideMe AI. Only use the supplied evidence below. Explain what changed.",
            untrustedContent = evidenceBundle.toString()
        )

        // 5. Local AI Reasoning
        val rawJson = aiEngine.understand(queryText, safePrompt)
        
        // 6. Validation
        return outputValidator.validateAndParse(rawJson.answer, validEvidenceIds = setOf("mem_doc_1", "mem_img_aug24", "user_approval_1"))
    }
}
