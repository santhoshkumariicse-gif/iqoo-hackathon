package com.iqoo.insideme.domain.graph

import com.iqoo.insideme.ai.LocalAIEngine

class EntityExtractor(private val aiEngine: LocalAIEngine) {

    /**
     * Pipeline: NEW MEMORY -> LOCAL AI -> ENTITY EXTRACTION -> ALIAS RESOLUTION
     */
    suspend fun extractAndResolve(memoryText: String, visualTags: List<String>): List<GraphEntity> {
        // In a real implementation, the LocalAIEngine would run Named Entity Recognition (NER)
        // For now, we simulate extraction.
        
        val entities = mutableListOf<GraphEntity>()
        
        if (memoryText.contains("Panel A17") || memoryText.contains("A17")) {
            // Resolve aliases to the canonical entity
            entities.add(GraphEntity(
                id = "ent_a17",
                type = "OBJECT",
                name = "Panel A17",
                aliases = listOf("A17", "Electrical Panel A17")
            ))
        }
        
        return entities
    }
}
