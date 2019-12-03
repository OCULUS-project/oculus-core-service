package pl.poznan.put.oculus.oculuscoreservice.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import pl.poznan.put.oculus.boot.exception.OculusException
import pl.poznan.put.oculus.oculuscoreservice.model.Job
import pl.poznan.put.oculus.oculuscoreservice.model.JobEvent
import pl.poznan.put.oculus.oculuscoreservice.model.JobStatus
import pl.poznan.put.oculus.oculuscoreservice.repository.JobsRepository

@Service
class JobsService (
        private val repository: JobsRepository,
        private val restTemplate: RestTemplate,
        @Value("\${oculus.facts-service}")
        private val factsServiceHost: String,
        private val kafkaTemplate: KafkaTemplate<String, JobEvent>
) {
    fun createJob(job: Job) = repository.insert(job)
            .also { logger.info("created new job ${it.id}") }
            .apply { generateFacts() }

    private fun Job.generateFacts() {
        generateFactsFromMetrics()
        sendNewJobEvent()
    }

    private fun Job.generateFactsFromMetrics() {
        restTemplate.postForEntity(
                "http://$factsServiceHost/facts/fromMetrics",
                FactsFromMetricsRequest(patientMetrics, id!!),
                Any::class.java
        ).let {
            when(it.statusCode) {
                HttpStatus.CREATED -> Unit
                else -> throw FactsFromMetricsCreationException()
            }
        }

        logger.info("generated metrics facts for job $id")
    }

    private fun Job.sendNewJobEvent() {
        kafkaTemplate.send("jobs", JobEvent(JobStatus.NEW, id!!))
        logger.info("sent new job event for job $id")
    }

    fun getJob(id: String) = repository.findByIdOrNull(id)
            .also { logger.info("getting job with id $id") }

    fun updateJobStatus(jobId: String, newStatus: JobStatus) = repository.updateStatus(jobId, newStatus)
            .also { logger.info("updated status of job $jobId to $newStatus") }

    companion object {

        private data class FactsFromMetricsRequest (
                val patientMetricsId: String,
                val jobId: String
        )

        private class FactsFromMetricsCreationException : OculusException("Error creating facts from metrics")
        
        private val logger = LoggerFactory.getLogger(JobsService::class.java)
    }
}
