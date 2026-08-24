package com.iqoo.insideme.ai

/**
 * Unified AI Facade for the Hackathon.
 * Wraps OCR, Vision, Embedding, and Reasoning into a single injectable engine.
 */
interface LocalAIEngine {
    suspend fun extractText(imageUri: String): OCRResult
    suspend fun analyzeImage(imageUri: String): VisionResult
    suspend fun embedText(text: String): FloatArray
    suspend fun understand(query: String, context: String): GroundedResponse
}

class DefaultLocalAIEngine(
    private val ocr: LocalOCR,
    private val vision: LocalVisionModel,
    private val embedding: LocalEmbeddingModel,
    private val reasoning: LocalReasoningModel
) : LocalAIEngine {

    override suspend fun extractText(imageUri: String) = ocr.extractText(imageUri)
    override suspend fun analyzeImage(imageUri: String) = vision.analyzeImage(imageUri)
    override suspend fun embedText(text: String) = embedding.embedText(text)
    override suspend fun understand(query: String, context: String) = reasoning.reason(query, context)
}
