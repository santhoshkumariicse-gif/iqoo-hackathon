package com.iqoo.insideme.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iqoo.insideme.device.runtime.AppConfig
import com.iqoo.insideme.device.runtime.OperationMode

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Configuration", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        // Office Kit Server IP
        Text("Office Kit Server IP", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = AppConfig.officeKitServerIp,
            onValueChange = { AppConfig.officeKitServerIp = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g. 10.0.2.2") }
        )
        Text(
            "Default: 10.0.2.2 for Android Emulator.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Operational Mode Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Operational Mode", style = MaterialTheme.typography.titleMedium)
                Text(
                    if (AppConfig.operationMode == OperationMode.GREEN_LIGHT) "GREEN LIGHT: Sync Enabled" else "RED LIGHT: Local Only",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (AppConfig.operationMode == OperationMode.GREEN_LIGHT) 
                        MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }
            Switch(
                checked = AppConfig.operationMode == OperationMode.GREEN_LIGHT,
                onCheckedChange = { AppConfig.toggleOperationMode() }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Show Reasoning Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Show Reasoning Traces", style = MaterialTheme.typography.titleMedium)
            Checkbox(
                checked = AppConfig.showReasoningTraces,
                onCheckedChange = { AppConfig.showReasoningTraces = it }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Apply & Back")
        }
    }
}
