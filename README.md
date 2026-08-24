# InsideMe AI

**A private multimodal memory engine that sees, remembers, understands change, explains its evidence, and turns context into action.**

InsideMe AI is a phone-first personal memory intelligence system designed to help users remember and understand the world around them. Instead of treating every interaction as an isolated AI conversation, InsideMe continuously builds contextual memory from camera, voice, and user input.

It identifies recurring entities, connects observations across time, retrieves relevant memories, detects changes, and explains conclusions using traceable evidence. Users can then convert insights into actions such as reminders or inspection tasks, with those actions becoming part of the memory.

The system prioritizes local/open-source AI for privacy and offline operation, while Office Kit connects the iQOO phone to a laptop-based Evidence Desk for deeper investigation and visualization.

The core experience is simple: **SEE → REMEMBER → RECALL → UNDERSTAND CHANGE → VERIFY EVIDENCE → ACT**.

## Architecture Overview
```text
 Camera / Voice
       ↓
 Multimodal AI (Vision/OCR/STT)
       ↓
 Entity Resolution
       ↓
 Memory Engine (Vector + Graph + Timeline)
       ↓
 Temporal Reasoning
       ↓
 Evidence Engine
       ↓
 Local LLM
       ↓
 Action Engine
```

## Why Local AI?
The data InsideMe handles can be highly personal and contextual. Local inference reduces dependence on network connectivity and keeps sensitive memory closer to the device. InsideMe is built with a dynamic **InferenceRuntime** that automatically cascades across the Snapdragon NPU, GPU, or CPU based on device availability, remaining fully functional even in Airplane Mode.

## The Office Kit Strategy
InsideMe is designed around the iQOO phone as the primary sensing and interaction surface. Camera, microphone, local inference, and the Office Kit workflow allow the phone to capture the world while the laptop becomes an optional investigation workspace (**The Evidence Desk**). 

## Known Limitations
* **Thermal Throttling**: Local model latency may vary by device temperature during heavy continuous inference.
* **Entity Matching**: Entity recognition is strongest for visually distinct objects or those with clear OCR text (e.g., "Panel A17").
* **Fallback Executions**: If the requested open-source model exhausts RAM, the app will silently fall back to a smaller quantized CPU model to prevent out-of-memory crashes.

---
*Built for the iQOO Hackathon 2026*
