package com.iqoo.insideme.domain.graph

enum class EntityType { PERSON, OBJECT, PLACE, PROJECT, DOCUMENT }
enum class EventType { INSPECTION, MEETING, PURCHASE, REPAIR, VISIT, TASK_COMPLETION }
enum class ActionStatus { OPEN, IN_PROGRESS, COMPLETED, CANCELLED }

data class Entity(
    val id: String,
    val type: EntityType,
    val name: String?,
    val createdAt: Long,
    val metadata: Map<String, String>
)

data class Observation(
    val id: String,
    val entityId: String,
    val timestamp: Long,
    val attributes: Map<String, String>,
    val sourceMemoryId: String,
    val confidence: Float
)

data class Event(
    val id: String,
    val type: EventType,
    val timestamp: Long,
    val entityIds: List<String>,
    val description: String?
)

data class Action(
    val id: String,
    val type: com.iqoo.insideme.ai.core.ActionType,
    val createdAt: Long,
    val entityId: String?,
    val status: ActionStatus,
    val evidenceIds: List<String>
)

// The Temporal Edges that turn this from a flat graph into a Temporal Database
enum class TemporalRelationshipType {
    OBSERVED,
    BELONGS_TO,
    LOCATED_AT,
    RELATED_TO,
    MENTIONED_IN,
    CHANGED_FROM,
    CHANGED_TO,
    FOLLOWED_BY,
    REQUIRES,
    PRECEDED_BY,
    VALID_DURING
}

data class TemporalRelationship(
    val id: String,
    val sourceId: String,
    val type: TemporalRelationshipType,
    val targetId: String,
    val createdAt: Long,
    val confidence: Float
)
