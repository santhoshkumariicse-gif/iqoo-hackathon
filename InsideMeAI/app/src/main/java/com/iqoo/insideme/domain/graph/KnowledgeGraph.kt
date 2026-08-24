package com.iqoo.insideme.domain.graph

data class GraphEntity(
    val id: String,
    val type: String,
    val name: String,
    val aliases: List<String>
)

data class GraphRelationship(
    val id: String,
    val sourceId: String,
    val targetId: String,
    val type: String,
    val confidence: String,
    val evidenceIds: List<String>
)

data class GraphSearchResult(
    val primaryEntity: GraphEntity,
    val relatedEntities: List<GraphEntity>,
    val relationships: List<GraphRelationship>
)

interface KnowledgeGraph {
    suspend fun getEntity(entityId: String): GraphEntity?
    suspend fun getRelatedEntities(entityId: String): List<GraphEntity>
    suspend fun findPath(sourceId: String, targetId: String): List<GraphRelationship>
    suspend fun searchConnected(query: String): GraphSearchResult
}

// ==========================================
// MOCK FOR DEMO "A17" KILLER QUERY
// ==========================================

class MockKnowledgeGraph : KnowledgeGraph {
    
    private val panelA17 = GraphEntity("ent_a17", "OBJECT", "Panel A17", listOf("A17", "Electrical Panel A17"))
    
    override suspend fun getEntity(entityId: String): GraphEntity? {
        return if (entityId == "ent_a17") panelA17 else null
    }

    override suspend fun getRelatedEntities(entityId: String): List<GraphEntity> {
        return emptyList()
    }

    override suspend fun findPath(sourceId: String, targetId: String): List<GraphRelationship> {
        return emptyList()
    }

    override suspend fun searchConnected(query: String): GraphSearchResult {
        // Simulating the deterministic demo dataset requested in Phase 8
        val doc1 = GraphEntity("doc_1", "DOCUMENT", "Maintenance.pdf", emptyList())
        val finding1 = GraphEntity("find_1", "FINDING", "Increased visible corrosion", emptyList())
        val task1 = GraphEntity("task_1", "TASK", "Physical inspection", emptyList())
        val loc1 = GraphEntity("loc_1", "PLACE", "Workshop A", emptyList())

        val relationships = listOf(
            GraphRelationship("rel_1", "ent_a17", "doc_1", "MENTIONED_IN", "STRONG", listOf("mem_doc_1")),
            GraphRelationship("rel_2", "ent_a17", "find_1", "HAS_FINDING", "STRONG", listOf("mem_img_aug24")),
            GraphRelationship("rel_3", "find_1", "task_1", "GENERATED_TASK", "STRONG", listOf("user_approval_1")),
            GraphRelationship("rel_4", "ent_a17", "loc_1", "LOCATED_AT", "MODERATE", listOf("gps_log_1"))
        )

        return GraphSearchResult(
            primaryEntity = panelA17,
            relatedEntities = listOf(doc1, finding1, task1, loc1),
            relationships = relationships
        )
    }
}
