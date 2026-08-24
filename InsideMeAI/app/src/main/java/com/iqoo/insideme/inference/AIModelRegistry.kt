package com.iqoo.insideme.inference

enum class ModelModality {
    VISION,
    SPEECH,
    TEXT,
    MULTIMODAL,
    EMBEDDING
}

data class AIModelInfo(
    val id: String,
    val name: String,
    val version: String,
    val modality: ModelModality,
    val quantization: String, // FP16, INT8, INT4
    val sizeMb: Long,
    val runtime: String, // TFLITE, ONNX, QNN
    val supportsNpu: Boolean,
    val supportsGpu: Boolean,
    val supportsCpu: Boolean
)

data class InferenceMetrics(
    val modelId: String,
    val runtimeUsed: String,
    val loadTimeMs: Long,
    val inferenceTimeMs: Long,
    val tokensPerSecond: Float?,
    val peakMemoryMb: Long,
    val isFallback: Boolean
)
