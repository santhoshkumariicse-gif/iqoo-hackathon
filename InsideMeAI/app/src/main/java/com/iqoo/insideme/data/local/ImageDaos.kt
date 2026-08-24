package com.iqoo.insideme.data.local

import androidx.room.*

// ─── Image DAO ────────────────────────────────────────────────────────────────

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(image: ImageEntity): Long

    @Query("SELECT * FROM images ORDER BY timestamp DESC")
    suspend fun getAll(): List<ImageEntity>

    @Query("DELETE FROM images WHERE id = :id")
    suspend fun deleteById(id: Long)
}

// ─── Image Metadata DAO ───────────────────────────────────────────────────────

@Dao
interface ImageMetadataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(metadata: ImageMetadata)

    @Query("SELECT * FROM image_metadata WHERE imageId = :imageId")
    suspend fun getForImage(imageId: Long): List<ImageMetadata>

    @Delete
    suspend fun delete(metadata: ImageMetadata)
}
