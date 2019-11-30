package pl.poznan.put.oculus.oculuscoreservice.repository

import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import pl.poznan.put.oculus.oculuscoreservice.model.Job
import pl.poznan.put.oculus.oculuscoreservice.model.JobStatus
import java.time.Instant

interface JobsRepository : MongoRepository<Job, String>, JobsRepositoryCustom

interface JobsRepositoryCustom {
    fun updateStatus(jobId: String, newStatus: JobStatus): Job
}

@Repository
class JobsRepositoryCustomImpl (
        private val mongoTemplate: MongoTemplate
) : JobsRepositoryCustom {
    override fun updateStatus(jobId: String, newStatus: JobStatus) = mongoTemplate.findAndModify(
            Query.query(Criteria.where("id").`is`(jobId)),
            Update().set("status", newStatus).set("updated", Instant.now()),
            FindAndModifyOptions.options().returnNew(true),
            Job::class.java
    )!!

}
