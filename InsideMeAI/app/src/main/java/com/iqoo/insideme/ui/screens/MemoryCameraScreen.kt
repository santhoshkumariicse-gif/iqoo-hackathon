package com.iqoo.insideme.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.iqoo.insideme.ui.CameraViewModel
import com.iqoo.insideme.ui.components.CameraPreview

@Composable
fun MemoryCameraScreen(
    viewModel: CameraViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    if (uiState.showConfirm) {
        SmartCaptureConfirmScreen(
            subjectName = "Motor Controller L293D", // Mocked detection
            previousMemoryCount = 2,
            lastObservation = "Aug 20, 2026",
            onSave = { viewModel.saveMemory() },
            onRetake = { viewModel.retake() }
        )
    } else {
        Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
            
            CameraPreview(
                imageCapture = viewModel.imageCapture,
                modifier = Modifier.fillMaxSize()
            )
            
            // UI Overlay
            IconButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
            ) {
                Text("✕", color = Color.White, style = MaterialTheme.typography.headlineMedium)
            }

            // Bottom Controls
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Voice Input
                OutlinedButton(
                    onClick = { /* Voice query logic */ },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                ) {
                    Text("🎙 Ask about this")
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Giant Capture Button
                Button(
                    onClick = { viewModel.captureImage(context) },
                    modifier = Modifier.size(80.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    enabled = !uiState.isCapturing
                ) {
                    if (uiState.isCapturing) {
                        CircularProgressIndicator(color = Color.Black)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Capture Memory", color = Color.White, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
fun SmartCaptureConfirmScreen(
    subjectName: String,
    previousMemoryCount: Int,
    lastObservation: String,
    onSave: () -> Unit,
    onRetake: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Card(
            modifier = Modifier.align(Alignment.Center).padding(32.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("MEMORY DETECTED", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(16.dp))
                
                Text("Subject: $subjectName", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Previous memories: $previousMemoryCount", style = MaterialTheme.typography.bodyMedium)
                Text("Last observation: $lastObservation", style = MaterialTheme.typography.bodyMedium)
                
                Spacer(modifier = Modifier.height(32.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    OutlinedButton(onClick = onRetake) {
                        Text("RETAKE")
                    }
                    Button(onClick = onSave) {
                        Text("SAVE")
                    }
                }
            }
        }
    }
}
