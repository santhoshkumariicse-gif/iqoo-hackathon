package com.iqoo.insideme.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_metadata")
data class ImageMetadata(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val imageId: Long,
    val width: Int,
    val height: Int,
    val dominantColorHex: String
)
