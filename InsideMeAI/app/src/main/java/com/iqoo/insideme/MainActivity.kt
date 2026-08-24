package com.iqoo.insideme

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.iqoo.insideme.ai.BitmapVisionModel
import com.iqoo.insideme.ai.CharNgramEmbeddingModel
import com.iqoo.insideme.ai.NoOpOCR
import com.iqoo.insideme.data.local.RecallDatabase
import com.iqoo.insideme.domain.RecallSearchEngine
import com.iqoo.insideme.integration.ActionValidator
import com.iqoo.insideme.integration.InsideMeOrchestratorImpl
import com.iqoo.insideme.integration.QueryParser
import com.iqoo.insideme.integration.TemporalLogicEngine
import com.iqoo.insideme.repositories.ImageRepository
import com.iqoo.insideme.repositories.OfficeKitSyncRepository
import com.iqoo.insideme.ui.CameraViewModel
import com.iqoo.insideme.ui.MainViewModel
import com.iqoo.insideme.ui.screens.AiSearchScreen

val DeepCanvas = Color(0xFF0D0F12)
val SurfaceCard = Color(0xFF1A1D24)
val PrimaryAction = Color(0xFFFF5500)
val AIGlow = Color(0xFF00E5FF)

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var cameraViewModel: CameraViewModel

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* handled via uiState in CameraScreen */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request camera permission upfront
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        // ─── Real DI (Manual, Hackathon baseline) ────────────────────────────
        val db = androidx.room.Room.databaseBuilder(
            applicationContext,
            RecallDatabase::class.java,
            "recall-db"
        ).fallbackToDestructiveMigration().build()

        // Real on-device AI components (no fake delays, no hardcoded answers)
        val embeddingModel = CharNgramEmbeddingModel()
        val searchEngine = RecallSearchEngine(db.memoryDao(), embeddingModel)

        // Orchestrator: real Room search, real query parsing
        val orchestrator = InsideMeOrchestratorImpl(
            queryParser = QueryParser(),
            searchEngine = searchEngine,
            temporalEngine = TemporalLogicEngine(),
            actionValidator = ActionValidator()
        )

        // Image repository: real BitmapFactory metadata extraction
        val imageRepo = ImageRepository(
            imageDao = db.imageDao(),
            imageMetadataDao = db.imageMetadataDao()
        )

        viewModel = MainViewModel(orchestrator, OfficeKitSyncRepository())
        cameraViewModel = CameraViewModel(imageRepo)

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
                    var currentScreen by remember { mutableStateOf("preloader") }
                    val uiState by viewModel.uiState.collectAsState()
                    val labViewModel = remember { com.iqoo.insideme.lab.ui.LabViewModel() }

                    when (currentScreen) {
                        "preloader" -> com.iqoo.insideme.ui.screens.PreloaderScreen(
                            onComplete = { currentScreen = "home" }
                        )
                        "home" -> com.iqoo.insideme.ui.screens.HomeScreen(
                            onNavigateToCapture = { currentScreen = "capture" },
                            onNavigateToSearch = { currentScreen = "search" },
                            onNavigateToLab = { currentScreen = "lab" },
                            onNavigateToSettings = { currentScreen = "settings" }
                        )
                        "settings" -> com.iqoo.insideme.ui.screens.SettingsScreen(
                            onBack = { currentScreen = "home" }
                        )
                        "capture" -> com.iqoo.insideme.ui.screens.MemoryCameraScreen(
                            viewModel = cameraViewModel,
                            onBack = { currentScreen = "home" }
                        )
                        "search" -> AiSearchScreen(
                            onSearch = { viewModel.onSearch(it) },
                            onMicClick = { /* STT: future phase */ },
                            onSyncClick = { viewModel.onSync() },
                            isLoading = uiState.isLoading,
                            response = uiState.response
                        )
                        "lab" -> com.iqoo.insideme.lab.HardwareLabScreen(viewModel = labViewModel)
                    }
                }
            }
        }
    }
}
