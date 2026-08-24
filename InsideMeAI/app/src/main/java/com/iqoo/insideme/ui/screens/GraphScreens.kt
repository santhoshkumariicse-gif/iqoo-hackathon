package com.iqoo.insideme.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iqoo.insideme.domain.graph.GraphSearchResult

@Composable
fun EntityDetailScreen(
    searchResult: GraphSearchResult,
    onViewTimeline: () -> Unit,
    onViewConnections: () -> Unit
) {
    val entity = searchResult.primaryEntity
    val documents = searchResult.relatedEntities.filter { it.type == "DOCUMENT" }
    val findings = searchResult.relatedEntities.filter { it.type == "FINDING" }
    val tasks = searchResult.relatedEntities.filter { it.type == "TASK" }
    val locations = searchResult.relatedEntities.filter { it.type == "PLACE" }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(entity.name.uppercase(), style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Observations: 3", style = MaterialTheme.typography.bodyLarge) // Hardcoded for Demo
                Text("Documents: ${documents.size}", style = MaterialTheme.typography.bodyLarge)
                Text("Findings: ${findings.size}", style = MaterialTheme.typography.bodyLarge)
                Text("Tasks: ${tasks.size}", style = MaterialTheme.typography.bodyLarge)
                Text("Locations: ${locations.size}", style = MaterialTheme.typography.bodyLarge)
                
                Spacer(modifier = Modifier.height(16.dp))
                Text("Latest change", style = MaterialTheme.typography.titleMedium)
                Text(findings.firstOrNull()?.name ?: "None", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                
                Spacer(modifier = Modifier.height(24.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onViewTimeline, modifier = Modifier.weight(1f)) {
                        Text("View Timeline")
                    }
                    OutlinedButton(onClick = onViewConnections, modifier = Modifier.weight(1f)) {
                        Text("View Connections")
                    }
                }
            }
        }
    }
}

@Composable
fun ConnectionExplorerScreen(
    searchResult: GraphSearchResult
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("CONNECTIONS", style = MaterialTheme.typography.headlineMedium)
        Text(searchResult.primaryEntity.name, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn {
            items(searchResult.relationships) { rel ->
                val target = searchResult.relatedEntities.find { it.id == rel.targetId }
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("→ ${rel.type}", style = MaterialTheme.typography.labelSmall)
                        Text(target?.name ?: "Unknown", style = MaterialTheme.typography.bodyLarge)
                        Text("Confidence: ${rel.confidence}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
        }
    }
}
