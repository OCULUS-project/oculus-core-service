package pl.poznan.put.oculus.oculuscoreservice.model

import io.swagger.annotations.ApiModel
import java.time.Instant

@ApiModel(description = "entity of inference job")
data class Job (
        val id: String?,
        val doctor: String,
        val patient: String,
        val status: JobStatus,
        val patientMetrics: String,
        val imageFile: String,
        val created: Instant,
        val updated: Instant
)

@ApiModel(description = "status of inference job")
enum class JobStatus { NEW, IN_PROGRESS, DONE }
