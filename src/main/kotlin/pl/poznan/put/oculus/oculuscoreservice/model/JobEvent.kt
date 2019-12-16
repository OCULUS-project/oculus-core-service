package pl.poznan.put.oculus.oculuscoreservice.model

data class JobEvent (
        val type: JobEventType,
        val jobId: String
)

enum class JobEventType {
    NEW, IMAGE_INFERENCE_ENDED, INFERENCE_STARTED, INFERENCE_ENDED, DONE
}
