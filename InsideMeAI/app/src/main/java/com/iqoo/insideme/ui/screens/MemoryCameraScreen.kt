package com.iqoo.insideme.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MemoryCameraScreen(
    onCaptureClick: () -> Unit,
    onVoiceQueryClick: () -> Unit,
    activeSubjectContext: String? = null,
    previousMemoryCount: Int = 0,
    lastObservationDate: String? = null,
    onCompareClick: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        
        // Simulated Camera Viewfinder
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("[ CAMERA VIEWFINDER ]", color = Color.DarkGray)
        }
        
        // AR-style Context Overlay (Appears when camera recognizes a subject)
        if (activeSubjectContext != null) {
            Card(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 48.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.7f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Possible match: $activeSubjectContext", style = MaterialTheme.typography.titleMedium, color = Color.White)
                    Text("$previousMemoryCount previous memories", style = MaterialTheme.typography.bodySmall, color = Color.LightGray)
                    if (lastObservationDate != null) {
                        Text("Last seen: $lastObservationDate", style = MaterialTheme.typography.bodySmall, color = Color.LightGray)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onCompareClick, modifier = Modifier.fillMaxWidth()) {
                        Text("Compare")
                    }
                }
            }
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
                onClick = onVoiceQueryClick,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
            ) {
                Text("🎙 Ask about this")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Giant Capture Button
            Button(
                onClick = onCaptureClick,
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {}
            Spacer(modifier = Modifier.height(8.dp))
            Text("Capture Memory", color = Color.White, style = MaterialTheme.typography.labelMedium)
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
