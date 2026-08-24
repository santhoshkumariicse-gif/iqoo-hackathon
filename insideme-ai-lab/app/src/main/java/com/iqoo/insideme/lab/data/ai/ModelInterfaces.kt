package com.iqoo.insideme.lab.data.ai

/**
 * PHASE 1 - CORE AI ABSTRACTIONS
 * These interfaces enforce the strict separation of local AI responsibilities.
 */

interface LocalOCR {
    suspend fun extractText(imagePath: String): String
}

interface LocalVisionModel {
    suspend fun analyzeImage(imagePath: String): String
}

interface LocalEmbeddingModel {
    suspend fun generateEmbedding(text: String): FloatArray
}

interface LocalSpeechToText {
    suspend fun transcribeAudio(audioPath: String): String
}

interface LocalLLM {
    suspend fun generateReasoning(prompt: String): String
    fun getHardwareBackend(): String // e.g., "CPU", "GPU", "QNN/Snapdragon NPU"
}

// ==========================================
// MOCK IMPLEMENTATIONS FOR UI TESTING ONLY
// ==========================================

class MockLocalOCR : LocalOCR {
    override suspend fun extractText(imagePath: String): String {
        return "Terminal A17. Voltage: 220V."
    }
}

class MockLocalVisionModel : LocalVisionModel {
    override suspend fun analyzeImage(imagePath: String): String {
        return "Minor corrosion visible on lower contacts."
    }
}

class MockLocalEmbeddingModel : LocalEmbeddingModel {
    override suspend fun generateEmbedding(text: String): FloatArray {
        return FloatArray(384) { 0.1f } // Dummy 384-d vector
    }
}

class MockLocalLLM : LocalLLM {
    override suspend fun generateReasoning(prompt: String): String {
        return "Based on the provided evidence, corrosion has increased. Recommend maintenance."
    }
    
    override fun getHardwareBackend(): String {
        return "MOCK_BACKEND (Awaiting iQOO Device)"
    }
}
