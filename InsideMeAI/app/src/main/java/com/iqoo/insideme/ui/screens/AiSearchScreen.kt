package com.iqoo.insideme.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.iqoo.insideme.ai.GroundedResponse
import com.iqoo.insideme.ai.Claim

@Composable
fun AiSearchScreen(
    onSearch: (String) -> Unit,
    onMicClick: () -> Unit,
    onSyncClick: () -> Unit,
    isLoading: Boolean,
    response: GroundedResponse?
) {
    var query by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("← InsideMe AI", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Ask your memory", style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("What do I know about the motor controller?") }
            )
            IconButton(onClick = onMicClick) {
                Text("🎙️")
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onSearch(query) }, modifier = Modifier.fillMaxWidth(), enabled = !isLoading) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (response != null) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text("INSIDEME AI", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(response.answer, style = MaterialTheme.typography.bodyLarge)
                }
                
                if (response.evidenceIds.isNotEmpty()) {
                    item {
                        Text("Evidence", style = MaterialTheme.typography.titleSmall)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.horizontalScroll(androidx.compose.foundation.rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            response.evidenceIds.forEach { evId ->
                                Card(modifier = Modifier.size(80.dp, 100.dp)) {
                                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                        Text(evId, style = MaterialTheme.typography.labelSmall)
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Based on ${response.evidenceIds.size} memories", style = MaterialTheme.typography.labelSmall)
                    }
                }
                
                if (response.claims.isNotEmpty() && com.iqoo.insideme.device.runtime.AppConfig.showReasoningTraces) {
                    item {
                        Text("Reasoning Trace", style = MaterialTheme.typography.titleSmall)
                    }
                    items(response.claims) { claim ->
                        val color = if (claim.type == "OBSERVED") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                        Text("- [${claim.type}] ${claim.text}", style = MaterialTheme.typography.bodySmall, color = color)
                    }
                }
                
                if (com.iqoo.insideme.device.runtime.AppConfig.operationMode == com.iqoo.insideme.device.runtime.OperationMode.GREEN_LIGHT) {
                    item {
                        Button(
                            onClick = onSyncClick,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Text("💻 Sync to Office Kit")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D0F12)
@Composable
fun AiSearchScreenPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        AiSearchScreen(
            onSearch = {},
            onMicClick = {},
            onSyncClick = {},
            isLoading = false,
            response = GroundedResponse(
                answer = "The motor controller appears to be an L293D based on the retrieved photos.",
                confidence = "High",
                evidenceIds = listOf("mem_001", "mem_002"),
                claims = listOf(
                    Claim("L293D text visible", "OBSERVED", "mem_001"),
                    Claim("Likely safe to operate", "INFERRED", null)
                )
            )
        )
    }
}
