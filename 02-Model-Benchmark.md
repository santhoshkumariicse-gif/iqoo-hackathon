# Local AI Model Benchmark

*Use the `insideme-ai-lab` app to benchmark candidate models on the iQOO loaner device.*

## 1. OCR Models
| Model / Framework | Load Time | Inference (Text) | Offline Reliability | Status |
| :--- | :--- | :--- | :--- | :--- |
| Google ML Kit (Text Recog v2) | | | | |
| Tesseract (Android port) | | | | |

## 2. Embedding Models (RecallAI)
*Requirement: Generate dense vectors for semantic search.*
| Model | Framework | Precision | Latency | Memory Footprint | Status |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `all-MiniLM-L6-v2` | ONNX Runtime | INT8 | | | |
| `paraphrase-MiniLM-L3-v2` | TFLite | INT8 | | | |

## 3. Vision & Reasoning (InsideMe LLM)
*Requirement: Reason over evidence, temporal comparison. Heavy lifting.*
| Model | Quantization | First Token | Tokens/Sec | Peak RAM | Target Hardware | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| Llama-3-8B-Instruct | 4-bit (INT4) | | | | CPU + NPU (QNN) | |
| Phi-3-Mini-4K-Instruct | 4-bit (INT4) | | | | CPU + NPU | |
| MobileNetV3 (Vision only) | FP16 / INT8 | | | | Edge TPU / NPU | |

## 4. Final Stack Decision
* Frozen OCR: 
* Frozen Embedding: 
* Frozen LLM/VLM: 
* Frozen STT: 
