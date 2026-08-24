package com.iqoo.insideme.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.iqoo.insideme.domain.Finding
import com.iqoo.insideme.domain.FindingCategory

@Composable
fun ActionCenterScreen(
    findings: List<Finding>,
    onReviewClick: (Finding) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("ACTION CENTER", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        if (findings.isEmpty()) {
            Text("No pending actions.", style = MaterialTheme.typography.bodyLarge)
        } else {
            findings.forEach { finding ->
                val (color, icon) = when (finding.category) {
                    FindingCategory.FOLLOW_UP -> Color.Red to "🔴"
                    FindingCategory.ATTENTION -> Color.Yellow to "🟡"
                    else -> Color.Blue to "🔵"
                }
                
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(icon)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(finding.category.name, color = color, style = MaterialTheme.typography.labelMedium)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(finding.title, style = MaterialTheme.typography.titleMedium)
                        Text(finding.description, style = MaterialTheme.typography.bodySmall, maxLines = 2)
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { onReviewClick(finding) }) {
                            Text("Review")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FindingDetailScreen(
    finding: Finding,
    onCreateTask: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("FINDING", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(finding.title, style = MaterialTheme.typography.titleLarge)
        
        Spacer(modifier = Modifier.height(8.dp))
        val strength = if (finding.confidence > 0.8f) "Strong" else "Moderate"
        Text("Evidence strength: $strength", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        
        Spacer(modifier = Modifier.height(16.dp))
        Text("AI Analysis", style = MaterialTheme.typography.titleSmall)
        Text(finding.description, style = MaterialTheme.typography.bodyMedium)
        
        Spacer(modifier = Modifier.height(24.dp))
        Text("Suggested action", style = MaterialTheme.typography.titleSmall)
        finding.suggestedActions.forEach { action ->
            Text("- $action", style = MaterialTheme.typography.bodyMedium)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = onCreateTask) {
                Text("Create Task")
            }
            OutlinedButton(onClick = onDismiss) {
                Text("Dismiss")
            }
        }
    }
}

@Composable
fun ReportPreviewScreen(
    reportMarkdown: String
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Inspection Report", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(modifier = Modifier.fillMaxSize()) {
            // In reality, this would parse Markdown, but we mock simple display
            Text(reportMarkdown, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyMedium)
        }
    }
}
