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

// ==========================================
// MOCK IMPLEMENTATIONS FOR DEMO
// ==========================================

class MockMemoryRepository : MemoryRepository {
    private val db = mutableMapOf<String, Memory>()
    
    override suspend fun save(memory: Memory) {
        db[memory.id] = memory
    }
    
    override suspend fun get(id: String) = db[id]
    
    override suspend fun updateState(id: String, state: ProcessingState) {
        db[id] = db[id]?.copy(processingState = state) ?: return
    }
}

class MockProcessingJobRepository : ProcessingJobRepository {
    private val jobs = mutableListOf<ProcessingJob>()
    
    override suspend fun save(job: ProcessingJob) {
        jobs.removeIf { it.id == job.id }
        jobs.add(job)
    }
    
    override suspend fun getPendingJobs() = jobs.filter { it.status == JobStatus.QUEUED }
    
    override suspend fun recoverStaleJobs() {
        val stale = jobs.filter { it.status == JobStatus.RUNNING }
        stale.forEach { job ->
            save(job.copy(status = JobStatus.RETRYING))
            println("SYSTEM: Recovered stale job ${job.id} after crash.")
        }
    }
}
