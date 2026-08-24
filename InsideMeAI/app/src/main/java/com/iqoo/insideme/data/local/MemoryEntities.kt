package com.iqoo.insideme.data.local

import androidx.room.*

@Entity(tableName = "subjects")
data class SubjectEntity(
    @PrimaryKey val id: String,
    val name: String,
    val entityType: String,
    val createdAt: Long,
    val representativeEmbeddingJson: String?
)

@Entity(
    tableName = "memories",
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("subjectId")]
)
data class MemoryEntity(
    @PrimaryKey val id: String,
    val subjectId: String?, // Nullable for unassigned memories
    val type: String, // IMAGE, DOCUMENT, VOICE, TEXT
    val uri: String,
    val createdAt: Long,
    val modifiedAt: Long,
    val extractedText: String?,
    val visualDescription: String?,
    val title: String?,
    val source: String,
    val confidence: Float,
    val location: String?
)

@Entity(
    tableName = "memory_tags",
    primaryKeys = ["memoryId", "tag"],
    foreignKeys = [
        ForeignKey(
            entity = MemoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["memoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("memoryId")]
)
data class MemoryTagEntity(
    val memoryId: String,
    val tag: String
)

@Entity(
    tableName = "memory_embeddings",
    foreignKeys = [
        ForeignKey(
            entity = MemoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["memoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("memoryId")]
)
data class MemoryEmbeddingEntity(
    @PrimaryKey val memoryId: String,
    val embeddingJson: String 
)
