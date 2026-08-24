package com.iqoo.insideme.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.iqoo.insideme.domain.ChangeSeverity
import com.iqoo.insideme.domain.DetectedChange

@Composable
fun ComparisonScreen(
    previousDate: String,
    currentDate: String,
    detectedChanges: List<DetectedChange>,
    llmExplanation: String?
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("WHAT CHANGED?", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Side-by-side Visual Comparison
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("PREVIOUS", style = MaterialTheme.typography.labelMedium)
                Card(modifier = Modifier.fillMaxWidth().height(150.dp).padding(4.dp)) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text("[ IMAGE ]")
                    }
                }
                Text(previousDate, style = MaterialTheme.typography.labelSmall)
            }
            
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("CURRENT", style = MaterialTheme.typography.labelMedium)
                Card(modifier = Modifier.fillMaxWidth().height(150.dp).padding(4.dp)) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text("[ IMAGE ]")
                    }
                }
                Text(currentDate, style = MaterialTheme.typography.labelSmall)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Deterministic Changes
        Text("Changes detected: ${detectedChanges.size}", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(detectedChanges) { change ->
                val severityColor = when (change.severity) {
                    ChangeSeverity.HIGH -> Color.Red
                    ChangeSeverity.MEDIUM -> Color.Yellow
                    ChangeSeverity.LOW -> Color.Green
                    ChangeSeverity.UNKNOWN -> Color.Gray
                }
                
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(12.dp).background(severityColor, shape = MaterialTheme.shapes.small))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(change.description, style = MaterialTheme.typography.bodyMedium)
                        Text("Category: ${change.category}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // LLM Grounded Explanation
        if (llmExplanation != null) {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("AI Explanation", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(llmExplanation, style = MaterialTheme.typography.bodySmall)
                }
            }
        } else {
            if (detectedChanges.isEmpty()) {
                Text("No reliable previous memory was found for comparison.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
