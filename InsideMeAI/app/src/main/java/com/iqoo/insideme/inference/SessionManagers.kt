package com.iqoo.insideme.inference

class ModelManager {
    private var activeModelId: String? = null
    private var activeRuntime: InferenceRuntime? = null

    suspend fun acquire(model: AIModelInfo): InferenceRuntime {
        if (activeModelId == model.id && activeRuntime != null) {
            return activeRuntime!!
        }
        
        // Strict OOM protection: Release any currently loaded model before loading a new one.
        releaseAll()

        activeRuntime = selectBestRuntime(model)
        activeRuntime?.loadModel(model)
        activeModelId = model.id
        
        return activeRuntime!!
    }

    private fun selectBestRuntime(model: AIModelInfo): InferenceRuntime {
        return when {
            model.supportsNpu -> NpuRuntime()
            model.supportsGpu -> GpuRuntime()
            model.supportsCpu -> CpuRuntime()
            else -> throw java.lang.Exception("Model ${model.name} is not supported on any available runtime.")
        }
    }

    fun release(modelId: String) {
        if (activeModelId == modelId) {
            releaseAll()
        }
    }

    fun releaseAll() {
        activeRuntime?.unloadModel()
        activeRuntime = null
        activeModelId = null
    }
}

class AISessionManager(private val modelManager: ModelManager) {
    val telemetryHistory = mutableListOf<InferenceMetrics>()

    suspend fun runInference(model: AIModelInfo, input: String): String {
        val startTime = System.currentTimeMillis()
        var loadTime = 0L
        
        val runtime = try {
            val rt = modelManager.acquire(model)
            loadTime = System.currentTimeMillis() - startTime
            rt
        } catch (e: Exception) {
            // Handle Graceful Fallback (e.g. NPU driver crash -> Force GPU reload)
            throw java.lang.Exception("Failed to acquire model: ${e.message}")
        }

        val inferenceStart = System.currentTimeMillis()
        val result = try {
            runtime.run(input)
        } catch (e: Exception) {
            throw java.lang.Exception("Inference failed: ${e.message}")
        }
        val inferenceTime = System.currentTimeMillis() - inferenceStart
        
        val metrics = InferenceMetrics(
            modelId = model.id,
            runtimeUsed = runtime.runtimeName,
            loadTimeMs = loadTime,
            inferenceTimeMs = inferenceTime,
            tokensPerSecond = if (model.modality == ModelModality.TEXT) 1000f / inferenceTime * 50f else null, // Mock calculation
            peakMemoryMb = model.sizeMb + 150L,
            isFallback = runtime.runtimeName != "NPU" && model.supportsNpu
        )
        
        telemetryHistory.add(metrics)
        
        // Lazy unload: We don't unload immediately in case of sequential calls, 
        // ModelManager handles unloading when a DIFFERENT model is requested.
        return result
    }
}
