package com.iqoo.insideme.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.iqoo.insideme.inference.InferenceMetrics

@Composable
fun BenchmarkScreen(
    metricsHistory: List<InferenceMetrics>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
            .padding(16.dp)
    ) {
        Text("LOCAL AI BENCHMARK", style = MaterialTheme.typography.headlineMedium, color = Color.Green)
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Telemetry History:", color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(metricsHistory) { metrics ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Model: ${metrics.modelId}", color = Color.Cyan)
                        Text("Runtime: ${metrics.runtimeUsed} ${if(metrics.isFallback) "(FALLBACK)" else ""}", color = if(metrics.isFallback) Color.Yellow else Color.LightGray)
                        Text("Load Time: ${metrics.loadTimeMs} ms", color = Color.White)
                        Text("Inference Time: ${metrics.inferenceTimeMs} ms", color = Color.White)
                        if (metrics.tokensPerSecond != null) {
                            Text("Tokens/sec: ${String.format("%.1f", metrics.tokensPerSecond)}", color = Color.White)
                        }
                        Text("Peak RAM: ${metrics.peakMemoryMb} MB", color = Color.Red)
                    }
                }
            }
        }
    }
}
