package com.iqoo.insideme.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DiagnosticsScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("AI SYSTEM DIAGNOSTICS", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        
        Text("MODEL STATUS", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        StatusRow("Vision Model (NPU)", "● Ready", true)
        StatusRow("Embedding Model (CPU)", "● Ready", true)
        StatusRow("Reasoning LLM (GPU)", "● Ready", true)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text("MODEL BENCHMARK", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                BenchmarkRow("Vision Inference", "1.2 sec")
                BenchmarkRow("Text Embedding", "0.18 sec")
                BenchmarkRow("LLM Generation (Warm)", "3.4 sec")
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                BenchmarkRow("Memory Retrieval", "0.11 sec")
            }
        }
    }
}

@Composable
fun StatusRow(label: String, status: String, isOk: Boolean) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label)
        Text(status, color = if (isOk) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
    }
}

@Composable
fun BenchmarkRow(label: String, time: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(time, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.secondary)
    }
}
