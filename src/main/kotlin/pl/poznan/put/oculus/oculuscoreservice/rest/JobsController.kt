package pl.poznan.put.oculus.oculuscoreservice.rest

import org.springframework.hateoas.IanaLinkRelations
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.poznan.put.oculus.boot.config.PublicAPI
import pl.poznan.put.oculus.oculuscoreservice.model.Job
import pl.poznan.put.oculus.oculuscoreservice.model.JobStatus
import pl.poznan.put.oculus.oculuscoreservice.rest.model.JobModel
import pl.poznan.put.oculus.oculuscoreservice.rest.model.JobRequest
import pl.poznan.put.oculus.oculuscoreservice.rest.model.JobStatusUpdateRequest
import pl.poznan.put.oculus.oculuscoreservice.rest.model.toModel
import pl.poznan.put.oculus.oculuscoreservice.service.JobsService
import java.net.URI
import java.time.Instant

@PublicAPI
@RestController
@RequestMapping("/jobs")
class JobsController (
        private val service: JobsService
) {
    @GetMapping("/{id}")
    fun getJob(@PathVariable id: String): ResponseEntity<JobModel> {
        val job = service.getJob(id)
        return if (job != null) ResponseEntity.ok(job.toModel())
        else ResponseEntity.noContent().build()
    }

    @PostMapping
    fun postJob(@RequestBody request: JobRequest): ResponseEntity<JobModel> {
        val now = Instant.now()
        val created = service.createJob(Job(
                null,
                request.doctor,
                request.patient,
                JobStatus.NEW,
                request.patientMetrics,
                request.imageFile,
                now,
                now
        )).toModel()
        return ResponseEntity.created(
                URI(created.links.getRequiredLink(IanaLinkRelations.SELF).href)
        ).body(created)
    }

    @PutMapping("/status")
    fun updateStatus(@RequestBody request: JobStatusUpdateRequest): ResponseEntity<JobModel> {
        val updated = service.updateJobStatus(request.jobId, request.newStatus)
        return ResponseEntity.ok(updated.toModel())
    }
}
