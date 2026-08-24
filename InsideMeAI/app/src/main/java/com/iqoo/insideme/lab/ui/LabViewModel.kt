package com.iqoo.insideme.lab.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iqoo.insideme.lab.data.ai.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class BenchmarkState(
    val ocrResult: String = "",
    val visionResult: String = "",
    val llmResult: String = "",
    val isRunning: Boolean = false,
    val inferenceTimeMs: Long = 0L,
    val hardwareBackend: String = "Unknown"
)

class LabViewModel : ViewModel() {

    // Using Mock Implementations until hardware deployment
    private val localOcr = MockLocalOCR()
    private val localVision = MockLocalVisionModel()
    private val localLlm = MockLocalLLM()

    private val _uiState = MutableStateFlow(BenchmarkState())
    val uiState: StateFlow<BenchmarkState> = _uiState

    fun runFullPipelineBenchmark() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRunning = true)
            
            val startTime = System.currentTimeMillis()
            
            // 1. OCR (Simulated delay)
            delay(500) 
            val text = localOcr.extractText("dummy_path")
            
            // 2. Vision (Simulated delay)
            delay(1500)
            val visualDesc = localVision.analyzeImage("dummy_path")
            
            // 3. LLM Reasoning (Simulated delay)
            delay(2500)
            val reasoning = localLlm.generateReasoning("Context: $text | $visualDesc")
            
            val endTime = System.currentTimeMillis()

            _uiState.value = BenchmarkState(
                ocrResult = text,
                visionResult = visualDesc,
                llmResult = reasoning,
                isRunning = false,
                inferenceTimeMs = endTime - startTime,
                hardwareBackend = localLlm.getHardwareBackend()
            )
        }
    }
}
