package com.iqoo.insideme.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AIStatusIndicator(isLocal: Boolean) {
    Surface(
        color = if (isLocal) Color.DarkGray else Color.Red,
        shape = MaterialTheme.shapes.small
    ) {
        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(8.dp).padding(end = 4.dp)) {
                Surface(shape = MaterialTheme.shapes.small, color = if (isLocal) Color.Green else Color.White, modifier = Modifier.fillMaxSize()) {}
            }
            Text(if (isLocal) "LOCAL AI" else "HYBRID CLOUD", style = MaterialTheme.typography.labelSmall, color = Color.White)
        }
    }
}

@Composable
fun MemoryCardWithWhyInteraction() {
    Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Panel A17", style = MaterialTheme.typography.titleMedium)
                AIStatusIndicator(isLocal = true)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Text("Condition changed", style = MaterialTheme.typography.bodyMedium)
            Text("Normal → Degraded", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
            Text("91% confidence", style = MaterialTheme.typography.labelSmall)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(onClick = { /* Expand "Why" Provenance Chain */ }) {
                    Text("Why?")
                }
                OutlinedButton(onClick = { /* Open Timeline */ }) {
                    Text("Timeline")
                }
                OutlinedButton(onClick = { /* Approve Task */ }) {
                    Text("Create Task")
                }
            }
        }
    }
}
