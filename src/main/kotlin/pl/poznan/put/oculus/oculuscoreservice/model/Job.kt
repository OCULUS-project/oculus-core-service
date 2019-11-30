package pl.poznan.put.oculus.oculuscoreservice.model

import io.swagger.annotations.ApiModel
import java.time.Instant

@ApiModel
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

enum class JobStatus { NEW, IN_PROGRESS, DONE }
