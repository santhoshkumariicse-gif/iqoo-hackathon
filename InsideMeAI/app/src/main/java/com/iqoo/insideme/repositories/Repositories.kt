package com.iqoo.insideme.repositories

import com.iqoo.insideme.domain.core.Memory
import com.iqoo.insideme.domain.core.ProcessingState
import com.iqoo.insideme.workers.JobStatus
import com.iqoo.insideme.workers.ProcessingJob

interface MemoryRepository {
    suspend fun save(memory: Memory)
    suspend fun get(id: String): Memory?
    suspend fun updateState(id: String, state: ProcessingState)
}

interface ProcessingJobRepository {
    suspend fun save(job: ProcessingJob)
    suspend fun getPendingJobs(): List<ProcessingJob>
    suspend fun recoverStaleJobs()
}


