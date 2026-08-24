package com.iqoo.insideme.data.local

import androidx.room.*

@Entity(
    tableName = "graph_entities",
    indices = [Index("name"), Index("type")]
)
data class GraphNodeEntity(
    @PrimaryKey val id: String,
    val type: String, // PERSON, PROJECT, PLACE, OBJECT, DOCUMENT, MEMORY, EVENT, FINDING, TASK
    val name: String,
    val metadataJson: String,
    val createdAt: Long,
    val updatedAt: Long
)

@Entity(
    tableName = "graph_aliases",
    foreignKeys = [
        ForeignKey(
            entity = GraphNodeEntity::class,
            parentColumns = ["id"],
            childColumns = ["entityId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("alias"), Index("entityId")]
)
data class GraphAliasEntity(
    @PrimaryKey val id: String,
    val entityId: String,
    val alias: String,
    val sourceMemoryId: String
)

@Entity(
    tableName = "graph_relationships",
    foreignKeys = [
        ForeignKey(
            entity = GraphNodeEntity::class,
            parentColumns = ["id"],
            childColumns = ["sourceId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = GraphNodeEntity::class,
            parentColumns = ["id"],
            childColumns = ["targetId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("sourceId"), Index("targetId"), Index("type")]
)
data class GraphRelationshipEntity(
    @PrimaryKey val id: String,
    val sourceId: String,
    val targetId: String,
    val type: String, // RELATED_TO, PART_OF, LOCATED_AT, OBSERVED_IN, DOCUMENTED_BY, CAUSED...
    val confidence: String, // STRONG, MODERATE, WEAK
    val evidenceIdsJson: String,
    val createdAt: Long
)

@Entity(
    tableName = "graph_events",
    indices = [Index("timestamp")]
)
data class GraphEventEntity(
    @PrimaryKey val id: String,
    val timestamp: Long,
    val type: String, // OBSERVED, CREATED, MODIFIED...
    val description: String,
    val entityIdsJson: String,
    val memoryIdsJson: String
)
