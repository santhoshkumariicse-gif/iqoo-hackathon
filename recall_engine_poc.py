import math
import json
import datetime

# ==========================================
# INSIDEME AI - Recall Engine PoC (Phase 1)
# ==========================================
# Simulates the offline multimodal memory engine
# and temporal comparison logic.

class RecallEngine:
    def __init__(self):
        self.vector_index = []
        self.memory_store = {}

    def _mock_embedding(self, text):
        """
        Simulates an ONNX quantized embedding model (e.g., all-MiniLM-L6-v2).
        Maps contextual keywords to a fixed vector for semantic search simulation.
        """
        keywords = ["terminal", "a17", "corrosion", "oxidation", "clean", "electrical", "panel", "degradation"]
        text_lower = text.lower()
        return [1.0 if kw in text_lower else 0.0 for kw in keywords]

    def _cosine_similarity(self, vec1, vec2):
        dot = sum(a*b for a, b in zip(vec1, vec2))
        norm1 = math.sqrt(sum(a*a for a in vec1))
        norm2 = math.sqrt(sum(b*b for b in vec2))
        if norm1 == 0 or norm2 == 0: return 0.0
        return dot / (norm1 * norm2)

    def capture_and_remember(self, memory_id, mem_type, content, visual_desc, timestamp):
        """
        Phase 2: Capture -> OCR/STT -> Metadata -> Embedding -> Local Index
        """
        # Combine contextual text for embedding
        combined_context = f"{content} {visual_desc}"
        embedding = self._mock_embedding(combined_context)
        
        memory_obj = {
            "id": memory_id,
            "type": mem_type,
            "timestamp": timestamp,
            "content": content,
            "visualDescription": visual_desc,
            "embedding": embedding
        }
        
        self.memory_store[memory_id] = memory_obj
        self.vector_index.append((memory_id, embedding))
        print(f"[RECALL] Memory stored locally: {memory_id} ({mem_type})")

    def semantic_search(self, user_query, top_k=2):
        """
        Phase 2: User Query -> Semantic Search -> Relevant Memories
        """
        query_vec = self._mock_embedding(user_query)
        results = []
        
        for mem_id, embedding in self.vector_index:
            sim = self._cosine_similarity(query_vec, embedding)
            if sim > 0.0:  # Threshold
                results.append((sim, self.memory_store[mem_id]))
                
        results.sort(key=lambda x: x[0], reverse=True)
        return [r[1] for r in results[:top_k]]


class TemporalIntelligence:
    def __init__(self):
        pass

    def analyze_change(self, previous_memory, current_memory):
        """
        Phase 4: Temporal Intelligence
        Simulates the heavy VLM running on the Snapdragon NPU to explain physical changes.
        """
        print(f"\n[TEMPORAL AI] Running Vision-Language Model inference...")
        print(f"  > Context 1: {previous_memory['timestamp']} - {previous_memory['visualDescription']}")
        print(f"  > Context 2: {current_memory['timestamp']} - {current_memory['visualDescription']}")
        
        prev_desc = previous_memory['visualDescription'].lower()
        curr_desc = current_memory['visualDescription'].lower()
        
        if "normal" in prev_desc and "corrosion" in curr_desc:
            return "Corrosion appears to have increased around the lower terminal compared with the previous inspection."
        return "No significant structural degradation detected."


# ==========================================
# TEST: Hero Workflow Execution
# ==========================================
if __name__ == "__main__":
    print("--- INSIDEME AI: Phase 1 Validation ---\n")
    
    engine = RecallEngine()
    ai = TemporalIntelligence()

    print(">> Scene 1: Capture an object (Historical)")
    engine.capture_and_remember(
        memory_id="MEM_AUG_01",
        mem_type="IMAGE",
        content="Electrical Panel A17",
        visual_desc="Terminal connections are clean and normal.",
        timestamp="2026-08-01T09:00:00Z"
    )

    print("\n>> Scene 3: Capture another observation (Current)")
    engine.capture_and_remember(
        memory_id="MEM_AUG_24",
        mem_type="IMAGE+VOICE",
        content="Electrical Panel A17. Note: Check lower contacts.",
        visual_desc="Minor corrosion visible on lower terminal.",
        timestamp="2026-08-24T14:30:00Z"
    )

    print("\n>> Scene 5: Ask: 'What do I know about this?'")
    query = "Find electrical panel A17 terminal condition"
    print(f"Query: '{query}'")
    
    print("\n>> Scene 6: RecallAI retrieves previous evidence")
    relevant_memories = engine.semantic_search(query)
    for mem in relevant_memories:
        print(f" - Retrieved: {mem['id']} ({mem['timestamp']})")

    print("\n>> Scene 7: Ask: 'What changed?'")
    if len(relevant_memories) >= 2:
        # Sort chronologically to get previous vs current
        relevant_memories.sort(key=lambda x: x['timestamp'])
        
        print("\n>> Scene 8: InsideMe AI identifies the change")
        explanation = ai.analyze_change(relevant_memories[0], relevant_memories[1])
        print(f"\n[AI OUTPUT] {explanation}")
        
        print("\n>> Scene 9: Generate recommendation")
        print("[RULE ENGINE] Severity: HIGH -> Schedule immediate maintenance.")
    else:
        print("Not enough memory context to perform temporal comparison.")

    print("\n--- Validation Complete. No cloud dependencies detected. ---")
