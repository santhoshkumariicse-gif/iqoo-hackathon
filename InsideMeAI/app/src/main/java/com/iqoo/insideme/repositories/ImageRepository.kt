package com.iqoo.insideme.repositories

import android.graphics.BitmapFactory
import androidx.palette.graphics.Palette
import com.iqoo.insideme.data.local.ImageDao
import com.iqoo.insideme.data.local.ImageEntity
import com.iqoo.insideme.data.local.ImageMetadataDao
import com.iqoo.insideme.data.local.ImageMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class ImageRepository(
    private val imageDao: ImageDao,
    private val imageMetadataDao: ImageMetadataDao
) {

    /** Insert just the file path (no metadata). */
    suspend fun addImage(filePath: String, timestamp: Long): Long = withContext(Dispatchers.IO) {
        imageDao.insert(ImageEntity(filePath = filePath, timestamp = timestamp))
    }

    /**
     * Insert image file path AND extract real metadata (dimensions + dominant colour)
     * from the saved JPEG using BitmapFactory — no fake values.
     */
    suspend fun addImageWithMetadata(filePath: String, timestamp: Long) = withContext(Dispatchers.IO) {
        val file = File(filePath)
        if (!file.exists()) return@withContext

        // Decode dimensions without loading pixels into memory
        val opts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(filePath, opts)
        val width = opts.outWidth
        val height = opts.outHeight

        // Decode a down‑sampled bitmap to extract dominant colour
        val sampleOpts = BitmapFactory.Options().apply {
            inSampleSize = 8
            inJustDecodeBounds = false
        }
        val bmp = BitmapFactory.decodeFile(filePath, sampleOpts)
        val dominantHex = if (bmp != null) {
            val palette = Palette.from(bmp).generate()
            val swatch = palette.dominantSwatch ?: palette.vibrantSwatch
            if (swatch != null) String.format("#%06X", 0xFFFFFF and swatch.rgb) else "#000000"
        } else "#000000"

        val imageId = imageDao.insert(ImageEntity(filePath = filePath, timestamp = timestamp))
        imageMetadataDao.insert(
            ImageMetadata(imageId = imageId, width = width, height = height, dominantColorHex = dominantHex)
        )
    }

    suspend fun getAllImages() = withContext(Dispatchers.IO) { imageDao.getAll() }

    suspend fun deleteImage(id: Long) = withContext(Dispatchers.IO) { imageDao.deleteById(id) }

    suspend fun getMetadataForImage(imageId: Long) = withContext(Dispatchers.IO) {
        imageMetadataDao.getForImage(imageId)
    }
}
