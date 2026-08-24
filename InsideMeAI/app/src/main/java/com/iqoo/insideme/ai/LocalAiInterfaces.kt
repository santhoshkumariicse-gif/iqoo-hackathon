package com.iqoo.insideme.ai

/**
 * Phase 2 - Recall Engine Core AI Abstractions
 */

data class OCRResult(val text: String, val confidence: Float)

interface LocalOCR {
    suspend fun extractText(imageUri: String): OCRResult
}

data class VisionResult(
    val description: String,
    val objects: List<String>,
    val entities: List<String>,
    val tags: List<String>,
    val confidence: Float
)

interface LocalVisionModel {
    suspend fun analyzeImage(imageUri: String): VisionResult
}

interface LocalEmbeddingModel {
    suspend fun embedText(text: String): FloatArray
}

// ==========================================
// MOCKS FOR RECALL ENGINE DEVELOPMENT
// ==========================================

class MockOCR : LocalOCR {
    override suspend fun extractText(imageUri: String): OCRResult {
        return OCRResult("Mock Extracted Text: PostgreSQL database schema...", 0.95f)
    }
}

class MockVisionModel : LocalVisionModel {
    override suspend fun analyzeImage(imageUri: String): VisionResult {
        return VisionResult(
            description = "Database schema drawn on a whiteboard",
            objects = listOf("Whiteboard", "Diagram"),
            entities = listOf("PostgreSQL", "users", "orders"),
            tags = listOf("database", "postgresql", "schema", "whiteboard"),
            confidence = 0.92f
        )
    }
}

class MockEmbeddingModel : LocalEmbeddingModel {
    override suspend fun embedText(text: String): FloatArray {
        // Return a mock 384-dimensional vector
        val mockVector = FloatArray(384) { 0f }
        // Simple mock: if text contains 'postgresql', flip a bit to simulate semantic meaning
        if (text.lowercase().contains("postgresql")) {
            mockVector[0] = 1.0f
        }
        return mockVector
    }
}
