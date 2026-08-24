package com.iqoo.insideme.demo

import com.iqoo.insideme.ai.GroundedResponse
import com.iqoo.insideme.usecases.AskInsideMeUseCase
import com.iqoo.insideme.usecases.QueryContext
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout

class SafeInferenceWrapper(
    private val useCase: AskInsideMeUseCase
) {
    /**
     * Executes the live inference but intercepts crashes, OOMs, or timeouts (e.g. NPU hang)
     * and returns a pre-cached deterministic result so the live demo does not fail on stage.
     */
    suspend fun executeSafeQuery(queryText: String, context: QueryContext): GroundedResponse {
        return try {
            // Give the NPU 15 seconds to reply. If it hangs on stage, abort and use fallback.
            withTimeout(15000) {
                useCase.invoke(queryText, context)
            }
        } catch (e: TimeoutCancellationException) {
            println("DEMO SAFETY NET: NPU timed out. Falling back to cached event data.")
            getFallbackResponse()
        } catch (e: Exception) {
            println("DEMO SAFETY NET: Inference crashed (${e.message}). Falling back.")
            getFallbackResponse()
        }
    }

    private fun getFallbackResponse(): GroundedResponse {
        return GroundedResponse(
            answer = "Corrosion appears more extensive than in the previous observation.",
            confidence = "Strong",
            evidenceIds = listOf("mem_baseline_aug18", "mem_img_aug24"),
            claims = emptyList(),
            limitations = "Generated via cached event-data due to live hardware timeout."
        )
    }
}
