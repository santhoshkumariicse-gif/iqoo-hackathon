package com.iqoo.insideme.security

import java.io.File

class FileValidator {
    
    companion object {
        const val MAX_IMAGE_SIZE_BYTES = 20 * 1024 * 1024 // 20 MB
        const val MAX_DOC_SIZE_BYTES = 25 * 1024 * 1024 // 25 MB
    }

    /**
     * Prevents Office Kit or manual uploads from causing OOM crashes by rejecting massive files.
     */
    fun validateFileForIngestion(file: File, expectedType: String): Boolean {
        if (!file.exists()) return false
        
        val size = file.length()
        
        return when (expectedType.lowercase()) {
            "image" -> size <= MAX_IMAGE_SIZE_BYTES
            "document" -> size <= MAX_DOC_SIZE_BYTES
            else -> false // Block unknown executables/types
        }
    }
}

class HardwareFallbackManager {

    /**
     * Simulates the app's resiliency to hardware failure by providing graceful UI 
     * messages rather than crashing the process.
     */
    fun checkPermissionsAndStorage(
        hasCameraPermission: Boolean,
        storageRemainingMb: Long
    ): HardwareStatus {
        if (!hasCameraPermission) {
            return HardwareStatus.Error("Camera permission required to capture a memory.")
        }
        
        if (storageRemainingMb < 100) { // Less than 100MB remaining
            return HardwareStatus.Error("Insufficient storage. Cannot process new captures.")
        }
        
        return HardwareStatus.Ok
    }
}

sealed class HardwareStatus {
    object Ok : HardwareStatus()
    data class Error(val message: String) : HardwareStatus()
}
