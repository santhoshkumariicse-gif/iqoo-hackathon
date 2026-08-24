package com.iqoo.insideme.ui

import android.content.Context
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iqoo.insideme.ui.components.takePicture
import com.iqoo.insideme.repositories.ImageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.Executors

data class CameraUiState(
    val isCapturing: Boolean = false,
    val lastCapturedFile: File? = null,
    val showConfirm: Boolean = false,
    val error: String? = null
)

class CameraViewModel(private val imageRepository: ImageRepository) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState = _uiState.asStateFlow()
    
    val imageCapture = ImageCapture.Builder().build()
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    fun captureImage(context: Context) {
        val photoFile = File(
            context.filesDir,
            "capture_${System.currentTimeMillis()}.jpg"
        )
        
        _uiState.value = _uiState.value.copy(isCapturing = true)
        
        takePicture(
            imageCapture = imageCapture,
            outputFile = photoFile,
            executor = cameraExecutor,
            onImageCaptured = { file ->
                _uiState.value = _uiState.value.copy(
                    isCapturing = false,
                    lastCapturedFile = file,
                    showConfirm = true
                )
            },
            onError = { exc ->
                _uiState.value = _uiState.value.copy(
                    isCapturing = false,
                    error = "Capture failed: ${exc.message}"
                )
            }
        )
    }

    fun saveMemory() {
        val file = _uiState.value.lastCapturedFile ?: return
        viewModelScope.launch {
            // Real extraction: reads JPEG, decodes dimensions & dominant colour
            imageRepository.addImageWithMetadata(
                filePath = file.absolutePath,
                timestamp = System.currentTimeMillis()
            )
            _uiState.value = _uiState.value.copy(showConfirm = false, lastCapturedFile = null)
        }
    }

    fun retake() {
        _uiState.value = _uiState.value.copy(showConfirm = false, lastCapturedFile = null)
    }

    override fun onCleared() {
        super.onCleared()
        cameraExecutor.shutdown()
    }
}
