package com.iqoo.insideme.device.runtime

enum class AIExecutionMode { LOCAL, CLOUD, HYBRID }
enum class ComputeBackend { NPU, GPU, CPU }

/**
 * 16.19 Runtime Abstraction
 * Agnostic wrapper ensuring the UI can transparently display whether 
 * the model is running on the NPU, GPU, or CPU.
 */
interface InferenceRuntime {
    fun initialize()
    suspend fun infer(input: ByteArray): ByteArray
    fun backend(): ComputeBackend
    fun executionMode(): AIExecutionMode
    fun release()
}

/**
 * 16.29 Office Kit State Manager
 * Tracks the explicit "Red Light" (Phone-Only) vs "Green Light" (Phone + Laptop)
 * operational modes as required by the hackathon.
 */
enum class OperationMode { RED_LIGHT, GREEN_LIGHT }

class OfficeKitManager {
    var currentMode: OperationMode = OperationMode.RED_LIGHT
        private set

    fun enableGreenLightMode() {
        println("OFFICE KIT: Green Light Enabled. Linking Phone Evidence Desk to Laptop Workspace.")
        currentMode = OperationMode.GREEN_LIGHT
    }

    fun enableRedLightMode() {
        println("OFFICE KIT: Red Light Enabled. Phone-only operation secured.")
        currentMode = OperationMode.RED_LIGHT
    }
}
