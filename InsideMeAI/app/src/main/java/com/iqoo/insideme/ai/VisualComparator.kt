package com.iqoo.insideme.ai

import com.iqoo.insideme.domain.ChangeCategory
import com.iqoo.insideme.domain.ChangeSeverity
import com.iqoo.insideme.domain.DetectedChange

data class VisualComparisonResult(
    val similarityScore: Float,
    val visualDifferences: List<DetectedChange>,
    val confidence: Float
)

interface LocalVisualComparator {
    suspend fun compareImages(previousImageUri: String, currentImageUri: String): VisualComparisonResult
}

// ==========================================
// PHASE 4 MOCK FOR UI/UX TESTING
// ==========================================

class MockLocalVisualComparator : LocalVisualComparator {
    override suspend fun compareImages(previousImageUri: String, currentImageUri: String): VisualComparisonResult {
        // Deterministic mock to simulate the "Corrosion increased" demo scenario
        val diff1 = DetectedChange(
            id = "vis_diff_1",
            category = ChangeCategory.VISUAL_CHANGE,
            description = "Corrosion region expanded on the lower terminal contacts.",
            confidence = 0.94f,
            previousEvidenceId = previousImageUri,
            currentEvidenceId = currentImageUri,
            region = "[x:10, y:50, w:30, h:30]",
            severity = ChangeSeverity.HIGH
        )
        
        val diff2 = DetectedChange(
            id = "vis_diff_2",
            category = ChangeCategory.POSITION_CHANGED,
            description = "Component position unchanged.",
            confidence = 0.99f,
            previousEvidenceId = previousImageUri,
            currentEvidenceId = currentImageUri,
            region = null,
            severity = ChangeSeverity.LOW
        )

        return VisualComparisonResult(
            similarityScore = 0.85f,
            visualDifferences = listOf(diff1, diff2),
            confidence = 0.92f
        )
    }
}
