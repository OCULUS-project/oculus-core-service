package pl.poznan.put.oculus.oculuscoreservice.service

import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pl.poznan.put.oculus.oculuscoreservice.model.Job
import pl.poznan.put.oculus.oculuscoreservice.model.JobStatus
import pl.poznan.put.oculus.oculuscoreservice.repository.JobsRepository

@Service
class JobsService (
        private val repository: JobsRepository
) {
    fun createJob(job: Job) = repository.insert(job)
            .also { logger.info("created new job with id ${it.id}") }

    fun getJob(id: String) = repository.findByIdOrNull(id)
            .also { logger.info("getting job with id $id") }

    fun updateJobStatus(jobId: String, newStatus: JobStatus) = repository.updateStatus(jobId, newStatus)
            .also { logger.info("updated status of job $jobId to $newStatus") }

    companion object {
        private val logger = LoggerFactory.getLogger(JobsService::class.java)
    }
}