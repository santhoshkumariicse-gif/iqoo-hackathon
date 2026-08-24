package com.iqoo.insideme.data.local

import androidx.room.*

data class MemoryWithRelations(
    @Embedded val memory: MemoryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "memoryId"
    )
    val tags: List<MemoryTagEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "memoryId"
    )
    val embedding: MemoryEmbeddingEntity?
)

@Dao
interface MemoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemory(memory: MemoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTags(tags: List<MemoryTagEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmbedding(embedding: MemoryEmbeddingEntity)

    @Transaction
    @Query("SELECT * FROM memories ORDER BY createdAt DESC")
    suspend fun getAllMemories(): List<MemoryWithRelations>

    @Transaction
    @Query("SELECT * FROM memories WHERE extractedText LIKE '%' || :query || '%' OR visualDescription LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    suspend fun searchMemoriesByKeyword(query: String): List<MemoryWithRelations>

    @Transaction
    @Query("SELECT * FROM memories WHERE id = :id")
    suspend fun getMemoryById(id: String): MemoryWithRelations?
}

@Database(
    entities = [MemoryEntity::class, MemoryTagEntity::class, MemoryEmbeddingEntity::class, SubjectEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RecallDatabase : RoomDatabase() {
    abstract fun memoryDao(): MemoryDao
}
