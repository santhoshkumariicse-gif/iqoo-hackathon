# Local AI Architecture

## Overview
InsideMe AI utilizes a highly compartmentalized Edge-AI architecture designed specifically for the Snapdragon NPU. The core philosophy is "Local First, Cloud Never". We strictly separate extraction (OCR), indexing (Embeddings), and reasoning (LLM) to prevent Out-Of-Memory (OOM) OS kills.

## Component Flow
```
[CAMERA / MICROPHONE] -> (Raw Media)
       |
       v
[PRE-PROCESSING] (Image scaling, Audio formatting)
       |
       +---> [LOCAL OCR] (Google ML Kit) -> Extracted Text
       |
       +---> [LOCAL STT] (Android Native) -> Transcribed Audio
       |
       +---> [LOCAL VISION] (MediaPipe Edge Model) -> Visual Observations
       |
       v
[MEMORY OBJECT ASSEMBLY] (Metadata + Timestamps)
       |
       v
[LOCAL EMBEDDING MODEL] (all-MiniLM-L6-v2 via ONNX)
       |
       v
[LOCAL VECTOR INDEX] (SQLite VSS / ObjectBox)
```

## The Recall Engine (Retrieval)
1. User submits natural language query.
2. Query is converted to vector via `LocalEmbeddingModel`.
3. Cosine similarity search runs against the `Local Vector Index`.
4. Top-K relevant memories are retrieved.

## The Intelligence Engine (Reasoning)
The heavy VLM (e.g., Llama-3-8B-Instruct 4-bit) is **only loaded into RAM** when the Reasoning Engine is invoked.

```
[RELEVANT MEMORIES] + [CURRENT CONTEXT]
       |
       v
[PROMPT ASSEMBLY] (Strict one-shot formatting)
       |
       v
[LOCAL LLM] (Qualcomm QNN / ExecuTorch)
       |
       v
[STRUCTURED FINDINGS] -> (Action Engine / Office Kit)
```

## Security & Privacy Boundary
All components listed above run within the Android App Sandbox. No network requests are made by any model provider. No telemetry data is sent. The only data egress point is explicitly triggered by the user via the Office Kit Laptop Bridge for reporting.
