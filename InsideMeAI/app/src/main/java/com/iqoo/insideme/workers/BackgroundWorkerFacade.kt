package com.iqoo.insideme.workers

enum class WorkType {
    IMAGE_ANALYSIS, OCR, EMBEDDING, MEMORY_INDEX, GRAPH_UPDATE, TEMPORAL_ANALYSIS, FINDING_GENERATION, REPORT_GENERATION
}

enum class JobStatus {
    QUEUED, RUNNING, COMPLETED, RETRYING, FAILED
}

data class ProcessingJob(
    val id: String,
    val memoryId: String,
    val type: WorkType,
    val priority: Int,
    val attempts: Int = 0,
    val status: JobStatus = JobStatus.QUEUED
)

/**
 * Simulates Android's WorkManager to allow local testing of the asynchronous pipeline.
 * Real implementation would use androidx.work.OneTimeWorkRequestBuilder.
 */
class BackgroundWorkerFacade {

    private val queue = mutableListOf<ProcessingJob>()

    fun enqueue(job: ProcessingJob) {
        queue.add(job)
        // In real app: WorkManager.getInstance(context).enqueue(workRequest)
        println("WORKER: Queued ${job.type} for Memory ${job.memoryId} [Priority: ${job.priority}]")
        executeNext()
    }

    private fun executeNext() {
        val nextJob = queue.filter { it.status == JobStatus.QUEUED || it.status == JobStatus.RETRYING }
                           .maxByOrNull { it.priority } ?: return
                           
        // Mark running
        val runningJob = nextJob.copy(status = JobStatus.RUNNING, attempts = nextJob.attempts + 1)
        updateQueue(runningJob)
        
        // Simulate background execution
        kotlinx.coroutines.GlobalScope.launch {
            try {
                // ... Mock execution time ...
                kotlinx.coroutines.delay(1000)
                updateQueue(runningJob.copy(status = JobStatus.COMPLETED))
                println("WORKER: Completed ${runningJob.type} for Memory ${runningJob.memoryId}")
            } catch (e: Exception) {
                if (runningJob.attempts >= 3) {
                    updateQueue(runningJob.copy(status = JobStatus.FAILED))
                    println("WORKER: FAILED ${runningJob.type} permanently.")
                } else {
                    updateQueue(runningJob.copy(status = JobStatus.RETRYING))
                }
            }
        }
    }
    
    private fun updateQueue(job: ProcessingJob) {
        val idx = queue.indexOfFirst { it.id == job.id }
        if (idx != -1) queue[idx] = job
    }
}
