package com.iqoo.insideme.domain.core

enum class MemoryType {
    IMAGE, AUDIO, TEXT, DOCUMENT, OBSERVATION, EVENT, ACTION
}

enum class ProcessingState {
    CAPTURED,
    PROCESSING,
    VALIDATED,
    INDEXED,
    READY,
    PARTIAL,
    FAILED
}

data class Memory(
    val id: String,
    val type: MemoryType,
    val contentUri: String, // Stored in private app storage, not Room
    val capturedAt: Long,
    val processingState: ProcessingState,
    val entityIds: List<String>,
    val metadata: Map<String, String>
)

sealed class MemoryEvent {
    data class Captured(val memoryId: String) : MemoryEvent()
    data class Processed(val memoryId: String) : MemoryEvent()
    data class Indexed(val memoryId: String) : MemoryEvent()
    data class RelationshipCreated(val relationshipId: String) : MemoryEvent()
    data class FindingCreated(val findingId: String) : MemoryEvent()
    data class ActionCreated(val actionId: String) : MemoryEvent()
}
