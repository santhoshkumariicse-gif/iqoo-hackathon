package com.iqoo.insideme.demo

import com.iqoo.insideme.repositories.MemoryRepository
import com.iqoo.insideme.domain.graph.KnowledgeGraph
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

enum class DemoState {
    READY,
    CAPTURE,
    RECALL,
    COMPARE,
    REASON,
    ACTION,
    OFFICE_KIT
}

class DemoController(
    private val memoryRepo: MemoryRepository,
    private val graph: KnowledgeGraph
) {
    private val _currentState = MutableStateFlow(DemoState.READY)
    val currentState: StateFlow<DemoState> = _currentState

    fun startDemo() {
        _currentState.value = DemoState.READY
    }

    /**
     * The Panic Button. Resets the database to the exact state required for the 
     * 3-minute hero demo to execute flawlessly.
     */
    suspend fun resetDemo() {
        // 1. Clear volatile tables
        // db.clearAllTables()
        
        // 2. Preload Event-created Baseline Data
        DemoDataset.preloadBaseline(memoryRepo, graph)
        
        _currentState.value = DemoState.READY
        println("DEMO CONTROLLER: System reset. Ready for live capture.")
    }

    fun advanceTo(state: DemoState) {
        _currentState.value = state
        println("DEMO CONTROLLER: Advancing to $state")
    }
}
