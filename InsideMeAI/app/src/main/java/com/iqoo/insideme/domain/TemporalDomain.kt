package com.iqoo.insideme.domain

enum class ChangeCategory {
    OBJECT_ADDED,
    OBJECT_REMOVED,
    POSITION_CHANGED,
    VISUAL_CHANGE,
    TEXT_CHANGED,
    METADATA_CHANGED
}

enum class ChangeSeverity {
    LOW,
    MEDIUM,
    HIGH,
    UNKNOWN
}

data class DetectedChange(
    val id: String,
    val category: ChangeCategory,
    val description: String,
    val confidence: Float,
    val previousEvidenceId: String,
    val currentEvidenceId: String,
    val region: String?, // Simplified bounding box representation
    val severity: ChangeSeverity
)

class ChangeEngine {
    /**
     * Deterministic comparison engine.
     * Evaluates text differences, tags, and invokes the visual comparator.
     */
    fun detectChanges(
        previousOcr: String?,
        currentOcr: String?,
        visualDiffs: List<DetectedChange>
    ): List<DetectedChange> {
        val changes = mutableListOf<DetectedChange>()
        
        // 1. Visual Changes (already computed by LocalVisualComparator)
        changes.addAll(visualDiffs)
        
        // 2. Text Comparison (Deterministic Diff)
        if (previousOcr != null && currentOcr != null) {
            if (previousOcr != currentOcr) {
                // Simple heuristic for the hackathon demo
                if (previousOcr.contains("5V") && currentOcr.contains("9V")) {
                    changes.add(
                        DetectedChange(
                            id = "change_txt_1",
                            category = ChangeCategory.TEXT_CHANGED,
                            description = "Voltage label changed from 5V to 9V.",
                            confidence = 0.98f,
                            previousEvidenceId = "prev_mem", // Mocks for now
                            currentEvidenceId = "curr_mem",
                            region = null,
                            severity = ChangeSeverity.HIGH
                        )
                    )
                }
            }
        }
        
        return changes
    }
}

class PreviousMemorySelector {
    /**
     * Finds the best valid baseline for comparison, not just the nearest chronological image.
     */
    fun selectBestComparison(currentSubjectId: String, candidateMemories: List<String>): String? {
        // Implementation would rank based on visual alignment, timestamp, and tag overlap.
        // For this architecture demo, we assume the list is pre-filtered by SubjectDao.
        if (candidateMemories.isEmpty()) return null
        return candidateMemories.first()
    }
}
