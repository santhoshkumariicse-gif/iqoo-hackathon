package com.iqoo.insideme.ui.screens

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.iqoo.insideme.ai.GroundedResponse
import com.iqoo.insideme.ai.Claim

@Preview(name = "Search Screen - Grounded Answer", showBackground = true, backgroundColor = 0xFF0D0F12)
@Composable
fun SearchScreenPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        AiSearchScreen(
            onSearch = {},
            onMicClick = {},
            onSyncClick = {},
            isLoading = false,
            response = GroundedResponse(
                answer = "The motor controller is an L293D dual H-bridge. It's used to control the speed and direction of two DC motors.",
                confidence = "High",
                evidenceIds = listOf("mem_001", "mem_002"),
                claims = listOf(
                    Claim("L293D text is visible on the IC", "OBSERVED", "mem_001"),
                    Claim("Commonly used in robotics for motor control", "INFERRED", null)
                )
            )
        )
    }
}

@Preview(name = "Search Screen - Loading", showBackground = true, backgroundColor = 0xFF0D0F12)
@Composable
fun SearchLoadingPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        AiSearchScreen(
            onSearch = {},
            onMicClick = {},
            onSyncClick = {},
            isLoading = true,
            response = null
        )
    }
}
