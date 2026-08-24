package com.iqoo.insideme.lab

import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iqoo.insideme.lab.ui.LabViewModel

val DeepCanvas = Color(0xFF0D0F12)
val SurfaceCard = Color(0xFF1A1D24)
val PrimaryAction = Color(0xFFFF5500)
val AIGlow = Color(0xFF00E5FF)
val GlassBorder = Color(0x1A2D333F)

@Composable
fun HardwareLabScreen(viewModel: LabViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepCanvas)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "InsideMe AI: Hardware Lab",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
            
            // AI Badge
            Surface(
                color = DeepCanvas,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, AIGlow.copy(alpha = 0.5f)),
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(AIGlow, RoundedCornerShape(percent = 50))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (uiState.isRunning) "INFERENCE" else "LOCAL READY",
                        style = MaterialTheme.typography.labelSmall,
                        color = AIGlow
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))

        // Hardware Diagnostics
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Device Info", style = MaterialTheme.typography.titleMedium, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Model: ${Build.MODEL}", color = Color.LightGray)
                Text("Manufacturer: ${Build.MANUFACTURER}", color = Color.LightGray)
                Text("Android API: ${Build.VERSION.SDK_INT}", color = Color.LightGray)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Results
        if (uiState.inferenceTimeMs > 0 || uiState.isRunning) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GlassBorder, RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Benchmark Results", style = MaterialTheme.typography.titleMedium, color = Color.White)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    if (uiState.isRunning) {
                        CircularProgressIndicator(
                            color = PrimaryAction,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    
                    Text("OCR Extraction: ${if(uiState.isRunning) "..." else uiState.ocrResult}", color = Color.LightGray)
                    Text("Vision Analysis: ${if(uiState.isRunning) "..." else uiState.visionResult}", color = Color.LightGray)
                    Text("LLM Reasoning: ${if(uiState.isRunning) "..." else uiState.llmResult}", color = Color.LightGray)
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Latency:", color = Color.White)
                        Text("${uiState.inferenceTimeMs} ms", color = PrimaryAction, fontWeight = FontWeight.Bold)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Hardware Backend:", color = Color.White)
                        Text("${uiState.hardwareBackend}", color = AIGlow, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Benchmark Runner (Bottom Action Bar style)
        Button(
            onClick = { viewModel.runFullPipelineBenchmark() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryAction),
            shape = RoundedCornerShape(16.dp),
            enabled = !uiState.isRunning
        ) {
            Text(
                text = if (uiState.isRunning) "Running On-Device..." else "Run On-Device",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }
    }
}
