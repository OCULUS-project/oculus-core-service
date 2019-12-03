package pl.poznan.put.oculus.oculuscoreservice.model

data class JobEvent (
        val type: JobStatus,
        val jobId: String
)
