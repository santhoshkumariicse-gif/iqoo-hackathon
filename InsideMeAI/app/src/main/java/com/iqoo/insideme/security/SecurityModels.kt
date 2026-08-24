package com.iqoo.insideme.security

enum class MemorySensitivity {
    PUBLIC,
    NORMAL,
    SENSITIVE,
    HIGHLY_SENSITIVE
}

data class MemorySecurityMetadata(
    val sensitivity: MemorySensitivity,
    val encrypted: Boolean,
    val createdAt: Long,
    val ownerId: String
)

enum class ActionRisk {
    LOW,
    MEDIUM,
    HIGH
}

enum class ConflictSeverity {
    MINOR,
    MODERATE,
    SEVERE
}

data class MemoryConflict(
    val entityId: String,
    val conflictingMemoryIds: List<String>,
    val description: String,
    val severity: ConflictSeverity
)

/**
 * 17.20 Action Authorization Policy
 * Protects against Prompt Injection tricking the LLM into deleting the DB.
 */
class ActionPolicyEngine {
    fun requiresBiometric(risk: ActionRisk): Boolean {
        return risk == ActionRisk.HIGH
    }

    fun canExecuteWithoutConfirmation(risk: ActionRisk): Boolean {
        return false // NO LLM Action runs without user confirmation
    }
}
