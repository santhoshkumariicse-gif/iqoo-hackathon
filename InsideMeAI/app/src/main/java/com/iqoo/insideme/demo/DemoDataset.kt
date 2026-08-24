package com.iqoo.insideme.demo

import com.iqoo.insideme.domain.core.Memory
import com.iqoo.insideme.domain.core.MemoryType
import com.iqoo.insideme.domain.core.ProcessingState
import com.iqoo.insideme.repositories.MemoryRepository
import com.iqoo.insideme.domain.graph.KnowledgeGraph

object DemoDataset {
    suspend fun preloadBaseline(repo: MemoryRepository, graph: KnowledgeGraph) {
        val initialMemory = Memory(
            id = "mem_baseline_aug18",
            type = MemoryType.IMAGE,
            contentUri = "file:///android_asset/demo/aug18_panelA17.jpg",
            capturedAt = 1692345600000, // Aug 18
            processingState = ProcessingState.READY,
            entityIds = listOf("ent_a17"),
            metadata = mapOf("demo" to "true")
        )
        repo.save(initialMemory)
        
        // Graph is mock-populated in KnowledgeGraph.kt for this demo
        println("DEMO DATASET: Preloaded Aug 18 Baseline Memory.")
    }
}
