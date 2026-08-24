package com.iqoo.insideme.inference

interface InferenceRuntime {
    val runtimeName: String
    suspend fun loadModel(model: AIModelInfo)
    suspend fun run(input: String): String
    fun unloadModel()
}

class NpuRuntime : InferenceRuntime {
    override val runtimeName = "NPU"
    private var isLoaded = false

    override suspend fun loadModel(model: AIModelInfo) {
        if (!model.supportsNpu) throw Exception("Model does not support NPU.")
        // Simulated loading delay
        kotlinx.coroutines.delay(1200)
        isLoaded = true
    }

    override suspend fun run(input: String): String {
        if (!isLoaded) throw Exception("Model not loaded.")
        kotlinx.coroutines.delay(350) // Fast NPU inference
        return "{ \"answer\": \"NPU Accelerated Output\", \"claims\": [], \"evidenceIds\": [], \"limitations\": null }"
    }

    override fun unloadModel() {
        isLoaded = false
    }
}

class GpuRuntime : InferenceRuntime {
    override val runtimeName = "GPU"
    private var isLoaded = false

    override suspend fun loadModel(model: AIModelInfo) {
        if (!model.supportsGpu) throw Exception("Model does not support GPU.")
        kotlinx.coroutines.delay(1500)
        isLoaded = true
    }

    override suspend fun run(input: String): String {
        if (!isLoaded) throw Exception("Model not loaded.")
        kotlinx.coroutines.delay(650)
        return "{ \"answer\": \"GPU Accelerated Output\", \"claims\": [], \"evidenceIds\": [], \"limitations\": null }"
    }

    override fun unloadModel() {
        isLoaded = false
    }
}

class CpuRuntime : InferenceRuntime {
    override val runtimeName = "CPU"
    private var isLoaded = false

    override suspend fun loadModel(model: AIModelInfo) {
        if (!model.supportsCpu) throw Exception("Model does not support CPU.")
        kotlinx.coroutines.delay(800)
        isLoaded = true
    }

    override suspend fun run(input: String): String {
        if (!isLoaded) throw Exception("Model not loaded.")
        kotlinx.coroutines.delay(2100) // Slow CPU inference
        return "{ \"answer\": \"CPU Fallback Output\", \"claims\": [], \"evidenceIds\": [], \"limitations\": null }"
    }

    override fun unloadModel() {
        isLoaded = false
    }
}
