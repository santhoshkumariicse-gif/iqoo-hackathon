package com.iqoo.insideme.ui.screens

sealed class CameraState {
    data object Idle : CameraState()
    data object Capturing : CameraState()
    data object Processing : CameraState()
    data class Result(val memoryId: String) : CameraState()
    data class Error(val message: String) : CameraState()
}
