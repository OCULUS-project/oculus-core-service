package pl.poznan.put.oculus.oculuscoreservice.rest.model

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import pl.poznan.put.oculus.oculuscoreservice.model.Job
import pl.poznan.put.oculus.oculuscoreservice.model.JobStatus
import pl.poznan.put.oculus.oculuscoreservice.rest.JobsController

class JobModel (content: Job) : EntityModel<Job>(content)

fun Job.toModel() = JobModel(this).apply {
    add(linkTo(methodOn(JobsController::class.java).getJob(id!!)).withSelfRel())
    add(linkTo(methodOn(JobsController::class.java).updateStatus(JobStatusUpdateRequest("", JobStatus.NEW))).withRel("updateStatus"))
}
