package com.iqoo.insideme.data.local

import androidx.room.*

@Entity(tableName = "findings")
data class FindingEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val category: String, // INFORMATIONAL, ATTENTION, FOLLOW_UP, UNKNOWN
    val status: String,   // NEW, REVIEWED, ACTIONED, DISMISSED
    val confidence: Float,
    val evidenceIdsJson: String,
    val suggestedActionsJson: String,
    val createdAt: Long
)

@Entity(
    tableName = "action_tasks",
    foreignKeys = [
        ForeignKey(
            entity = FindingEntity::class,
            parentColumns = ["id"],
            childColumns = ["sourceFindingId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("sourceFindingId")]
)
data class ActionTaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val sourceFindingId: String,
    val status: String, // OPEN, COMPLETED, DISMISSED
    val createdAt: Long,
    val dueAt: Long?
)
