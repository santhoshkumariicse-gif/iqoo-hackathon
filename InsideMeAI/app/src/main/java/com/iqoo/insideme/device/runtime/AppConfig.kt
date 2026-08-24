package com.iqoo.insideme.device.runtime

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Centralized Configuration for the InsideMe AI App.
 * Handles Office Kit settings and Hardware Backend preferences.
 */
object AppConfig {
    // Office Kit Server IP (Default 10.0.2.2 for Android Emulator to Host)
    var officeKitServerIp by mutableStateOf("10.0.2.2")
    
    // Operational Mode: RED_LIGHT (Local Only) or GREEN_LIGHT (Sync Enabled)
    var operationMode by mutableStateOf(OperationMode.RED_LIGHT)
    
    // Preferred Compute Backend
    var preferredBackend by mutableStateOf(ComputeBackend.NPU)
    
    // Whether to show reasoning traces in search results
    var showReasoningTraces by mutableStateOf(true)

    fun toggleOperationMode() {
        operationMode = if (operationMode == OperationMode.RED_LIGHT) {
            OperationMode.GREEN_LIGHT
        } else {
            OperationMode.RED_LIGHT
        }
    }
}
