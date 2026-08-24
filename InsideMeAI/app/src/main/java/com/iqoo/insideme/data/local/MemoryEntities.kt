package com.iqoo.insideme.data.local

import androidx.room.*

// ─── Memory (core captured entry) ────────────────────────────────────────────

@Entity(tableName = "memories")
data class MemoryEntity(
    @PrimaryKey val id: String,
    val imagePath: String,        // path in filesDir
    val entityName: String,       // e.g. "Machine_A17"
    val visualDescription: String = "",
    val extractedText: String = "",
    val timestamp: Long,
    val createdAt: Long = System.currentTimeMillis(),
    val attributesJson: String = "{}"
)

@Entity(
    tableName = "memory_tags",
    foreignKeys = [ForeignKey(
        entity = MemoryEntity::class,
        parentColumns = ["id"],
        childColumns = ["memoryId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("memoryId")]
)
data class MemoryTagEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val memoryId: String,
    val tag: String
)

@Entity(
    tableName = "memory_embeddings",
    foreignKeys = [ForeignKey(
        entity = MemoryEntity::class,
        parentColumns = ["id"],
        childColumns = ["memoryId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("memoryId", unique = true)]
)
data class MemoryEmbeddingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val memoryId: String,
    val embeddingJson: String   // JSON array of floats
)

// ─── Subject (who/what was observed) ─────────────────────────────────────────

@Entity(tableName = "subjects")
data class SubjectEntity(
    @PrimaryKey val id: String,
    val name: String,
    val type: String = "OBJECT",   // PERSON, OBJECT, PLACE, etc.
    val metadataJson: String = "{}"
)

// ─── Image (raw file record) ──────────────────────────────────────────────────

@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val filePath: String,
    val timestamp: Long
)
