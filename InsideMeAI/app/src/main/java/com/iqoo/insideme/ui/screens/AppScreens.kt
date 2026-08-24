package com.iqoo.insideme.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onNavigateToCapture: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToLab: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("INSIDEME AI", style = MaterialTheme.typography.headlineLarge)
        Text("Your private memory", style = MaterialTheme.typography.bodyLarge)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(onClick = onNavigateToCapture, modifier = Modifier.fillMaxWidth()) {
            Text("📷 Capture")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = onNavigateToSearch, modifier = Modifier.fillMaxWidth()) {
            Text("🔎 Search memories")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = onNavigateToLab, modifier = Modifier.fillMaxWidth()) {
            Text("⚙️ Hardware Lab")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedButton(onClick = onNavigateToSettings, modifier = Modifier.fillMaxWidth()) {
            Text("🛠 Configuration")
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text("Recent memories", style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.Start))
        // Placeholder for horizontal row of recent memories
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Card(modifier = Modifier.size(100.dp)) { Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) { Text("IMG") } }
            Card(modifier = Modifier.size(100.dp)) { Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) { Text("DOC") } }
            Card(modifier = Modifier.size(100.dp)) { Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) { Text("IMG") } }
        }
    }
}

@Composable
fun SearchScreen(
    onSearch: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("🔎 What do you remember?", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Find the circuit diagram I captured last week") }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onSearch(query) }, modifier = Modifier.fillMaxWidth()) {
            Text("Search")
        }
    }
}

@Composable
fun MemoryDetailScreen(
    title: String,
    date: String,
    extractedText: String,
    tags: List<String>
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Card(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text("ORIGINAL IMAGE")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Text(title, style = MaterialTheme.typography.headlineMedium)
        Text("Captured: $date", style = MaterialTheme.typography.bodyMedium)
        
        Spacer(modifier = Modifier.height(16.dp))
        Text("Extracted text:", style = MaterialTheme.typography.titleSmall)
        Text(extractedText, style = MaterialTheme.typography.bodySmall)
        
        Spacer(modifier = Modifier.height(16.dp))
        Text("Tags:", style = MaterialTheme.typography.titleSmall)
        Row {
            tags.forEach { tag ->
                AssistChip(onClick = {}, label = { Text("#$tag") }, modifier = Modifier.padding(end = 8.dp))
            }
        }
    }
}
