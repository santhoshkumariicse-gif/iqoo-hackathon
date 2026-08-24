package com.iqoo.insideme.domain.graph

enum class ResolutionResult {
    MERGE_EXACT,
    ASK_USER,
    CREATE_NEW
}

class EntityResolver {
    
    /**
     * 14.16 Entity Resolution
     * Determines whether a newly captured object is a known entity or a brand new one.
     */
    fun resolveEntity(
        newName: String, 
        newMetadata: Map<String, String>, 
        existingEntities: List<Entity>
    ): Pair<ResolutionResult, Entity?> {
        
        var bestMatch: Entity? = null
        var highestScore = 0f
        
        for (entity in existingEntities) {
            var score = 0f
            // 1. Name Similarity
            if (entity.name.equals(newName, ignoreCase = true)) {
                score += 0.8f
            }
            
            // 2. Metadata Overlap (e.g., location, serial numbers)
            val commonKeys = entity.metadata.keys.intersect(newMetadata.keys)
            if (commonKeys.isNotEmpty()) {
                score += (commonKeys.size * 0.1f)
            }
            
            if (score > highestScore) {
                highestScore = score
                bestMatch = entity
            }
        }
        
        return when {
            highestScore >= 0.8f -> Pair(ResolutionResult.MERGE_EXACT, bestMatch)
            highestScore >= 0.4f -> Pair(ResolutionResult.ASK_USER, bestMatch)
            else -> Pair(ResolutionResult.CREATE_NEW, null)
        }
    }
}
