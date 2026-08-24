package com.iqoo.insideme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.iqoo.insideme.ui.MainViewModel
import com.iqoo.insideme.ui.screens.AiSearchScreen
import com.iqoo.insideme.integration.OrchestratorImpl
import com.iqoo.insideme.ai.core.*
import com.iqoo.insideme.domain.graph.*
import com.iqoo.insideme.integration.QueryParser

val DeepCanvas = Color(0xFF0D0F12)
val SurfaceCard = Color(0xFF1A1D24)
val PrimaryAction = Color(0xFFFF5500)
val AIGlow = Color(0xFF00E5FF)

class MainActivity : ComponentActivity() {
    
    // In a real app, use Hilt or another DI framework
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Manual DI for the hackathon baseline
        val orchestrator = OrchestratorImpl(
            visionAi = object : VisionAI {
                override suspend fun analyzeImage(data: ByteArray) = Observation(
                    id = "obs_1",
                    entityId = "ent_a17",
                    timestamp = System.currentTimeMillis(),
                    attributes = mapOf("corrosion" to "visible"),
                    memoryId = "mem_1",
                    confidence = 0.95f
                )
            },
            temporalEngine = TemporalLogicEngine(),
            actionValidator = ActionValidator(),
            queryParser = QueryParser()
        )
        viewModel = MainViewModel(orchestrator)

        setContent {
            val customColorScheme = darkColorScheme(
                background = DeepCanvas,
                surface = SurfaceCard,
                primary = PrimaryAction,
                secondary = AIGlow
            )
            MaterialTheme(colorScheme = customColorScheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentScreen by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("home") }
                    val uiState by viewModel.uiState.collectAsState()
                    val labViewModel = androidx.compose.runtime.remember { com.iqoo.insideme.lab.ui.LabViewModel() }

                    when (currentScreen) {
                        "home" -> {
                            com.iqoo.insideme.ui.screens.HomeScreen(
                                onNavigateToCapture = { },
                                onNavigateToSearch = { currentScreen = "search" },
                                onNavigateToLab = { currentScreen = "lab" }
                            )
                        }
                        "search" -> {
                            AiSearchScreen(
                                onSearch = { viewModel.onSearch(it) },
                                onMicClick = { /* STT integration */ },
                                isLoading = uiState.isLoading,
                                response = uiState.response
                            )
                        }
                        "lab" -> {
                            com.iqoo.insideme.lab.HardwareLabScreen(viewModel = labViewModel)
                        }
                    }
                }
            }
        }
    }
}
