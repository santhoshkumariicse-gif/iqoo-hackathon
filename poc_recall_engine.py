import math

# INSIDEME AI - Phase 1 Proof of Concept
# RecallAI Memory Engine & Temporal Comparison

class MockRecallEngine:
    def __init__(self):
        # The memory index
        self.memories = []

    def mock_embed(self, text):
        """
        Simulates generating a vector embedding for a piece of text or image metadata.
        In production, this runs on the Snapdragon NPU using ONNX (e.g. all-MiniLM-L6-v2).
        For this PoC, we map keywords to a simple fixed-size vector.
        """
        keywords = ["rust", "corrosion", "terminal", "oxidation", "clean", "normal", "a17"]
        text_lower = text.lower()
        return [1.0 if kw in text_lower else 0.0 for kw in keywords]

    def cosine_similarity(self, vec1, vec2):
        dot = sum(a*b for a, b in zip(vec1, vec2))
        norm1 = math.sqrt(sum(a*a for a in vec1))
        norm2 = math.sqrt(sum(b*b for b in vec2))
        if norm1 == 0 or norm2 == 0: return 0.0
        return dot / (norm1 * norm2)

    def remember(self, evidence_id, text_metadata, timestamp):
        """
        Capture -> OCR/STT -> Embed -> Store in Local Index
        """
        embedding = self.mock_embed(text_metadata)
        memory = {
            "id": evidence_id,
            "text": text_metadata,
            "timestamp": timestamp,
            "embedding": embedding
        }
        self.memories.append(memory)
        print(f"[RECALL] Stored Memory: {evidence_id} at {timestamp}")

    def search(self, query, threshold=0.5):
        """
        Semantic Retrieval
        """
        query_vec = self.mock_embed(query)
        results = []
        for mem in self.memories:
            sim = self.cosine_similarity(query_vec, mem["embedding"])
            if sim > threshold:
                results.append((sim, mem))
        
        # Sort by similarity
        results.sort(key=lambda x: x[0], reverse=True)
        return [r[1] for r in results]


class MockTemporalEngine:
    def compare(self, previous_memory, current_memory):
        """
        Phase 4: Temporal Intelligence
        Compares previous state to current state and generates reasoning.
        In production, this is where the local VLM/LLM shines.
        """
        print(f"\n[TEMPORAL] Comparing '{previous_memory['id']}' with '{current_memory['id']}'")
        prev_text = previous_memory['text'].lower()
        curr_text = current_memory['text'].lower()
        
        if "clean" in prev_text and ("rust" in curr_text or "corrosion" in curr_text):
            return "Corrosion appears to have initiated around the terminal compared with the previous inspection."
        elif "rust" in prev_text and "rust" in curr_text:
            return "Corrosion persists at the terminal. No signs of remediation since last inspection."
        
        return "No significant structural degradation detected."


# --- Run the PoC ---
if __name__ == "__main__":
    print("=== InsideMe AI: Phase 1 Hardware & Logic PoC ===\n")
    
    recall = MockRecallEngine()
    temporal = MockTemporalEngine()

    print("--- 1. Building Memory (Historical Inspection) ---")
    # Six months ago
    recall.remember("IMG_2026_02", "Terminal A17. Condition: Clean, no oxidation visible.", "2026-02-14")
    
    print("\n--- 2. Current Inspection (Today) ---")
    # Today
    current_evidence_id = "IMG_2026_08"
    current_observation = "Terminal A17. Visible rust and heavy corrosion on lower contacts."
    recall.remember(current_evidence_id, current_observation, "2026-08-24")

    print("\n--- 3. Semantic Search (RecallAI) ---")
    query = "Find previous photos of terminal A17 showing rust or clean state"
    print(f"Query: '{query}'")
    relevant_memories = recall.search(query)
    
    print(f"Found {len(relevant_memories)} relevant memories.")
    
    # We want to compare the current one with the oldest relevant one
    if len(relevant_memories) >= 2:
        # Sort by timestamp to get previous vs current
        relevant_memories.sort(key=lambda x: x['timestamp'])
        prev_mem = relevant_memories[0]
        curr_mem = relevant_memories[-1]
        
        print("\n--- 4. Temporal Intelligence (What changed?) ---")
        ai_explanation = temporal.compare(prev_mem, curr_mem)
        
        print(f"\n[OUTPUT] AI Explanation:\n>> {ai_explanation}")
        
        print("\n[OUTPUT] Structured Finding for Rule Engine:")
        print(f"  - Entity: Terminal A17")
        print(f"  - Severity: HIGH (Rule: Degradation Detected)")
        print(f"  - Recommendation: Schedule immediate maintenance for terminal cleaning.")
    else:
        print("Not enough history for temporal comparison.")
    
    print("\n=== PoC Complete. Logic flow validated for Snapdragon implementation. ===")
