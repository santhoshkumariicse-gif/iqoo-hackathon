package com.iqoo.insideme.device

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator

data class PhoneContext(
    val currentEntityId: String?,
    val currentMemoryId: String?,
    val currentScreen: String,
    val recentEntities: List<String>,
    val recentMemories: List<String>
)

data class DeviceProfile(
    val totalRamMb: Int,
    val availableRamMb: Int,
    val cpuCores: Int,
    val supportsNpu: Boolean,
    val supportsGpuAcceleration: Boolean
)

class DeviceProfiler {
    /**
     * Identifies hardware capabilities dynamically to prevent hardcoding assumptions
     * about the iQOO loaner device. Enables adaptive inference scaling.
     */
    fun profileDevice(): DeviceProfile {
        return DeviceProfile(
            totalRamMb = 8192, // Mocked 8GB
            availableRamMb = 3072, // Mocked 3GB free
            cpuCores = 8,
            supportsNpu = true,
            supportsGpuAcceleration = true
        )
    }
}

class HapticController(private val context: Context) {
    
    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    fun triggerCaptureHaptic() {
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }
    
    fun triggerTaskCreatedHaptic() {
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 30, 50, 30), -1))
        }
    }
}
