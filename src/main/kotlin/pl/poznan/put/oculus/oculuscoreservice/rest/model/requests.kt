package pl.poznan.put.oculus.oculuscoreservice.rest.model

import io.swagger.annotations.ApiModel
import pl.poznan.put.oculus.oculuscoreservice.model.JobStatus

@ApiModel(description = "request for creating new job")
data class JobRequest (
        val doctor: String,
        val patient: String,
        val patientMetrics: String,
        val imageFile: String
)

@ApiModel(description = "request for updating status")
data class JobStatusUpdateRequest (
        val jobId: String,
        val newStatus: JobStatus
)
