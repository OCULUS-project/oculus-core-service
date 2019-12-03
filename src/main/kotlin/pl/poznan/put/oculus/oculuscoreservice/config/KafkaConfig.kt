package pl.poznan.put.oculus.oculuscoreservice.config

import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaAdmin
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import pl.poznan.put.oculus.oculuscoreservice.model.JobEvent

@EnableKafka
@Configuration
class KafkaTopicConfig (
        @Value("\${kafka.bootstrapAddress}")
        private val bootstrapAddress: String
) {


    @Bean
    fun kafkaAdmin(): KafkaAdmin = KafkaAdmin(
            mapOf(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress)
    )

    @Bean
    fun jobsTopic(): NewTopic = NewTopic("jobs", 1, 1.toShort())

    @Bean
    fun consumerFactory(): ConsumerFactory<String, String> = DefaultKafkaConsumerFactory(
            mapOf(
                    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress,
                    ConsumerConfig.GROUP_ID_CONFIG to "1",
                    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class.java,
                    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class.java
        )
    )

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String>
            = ConcurrentKafkaListenerContainerFactory<String, String>()
                .apply { consumerFactory = consumerFactory() }

    private fun <A, B> producerFactory() =  DefaultKafkaProducerFactory<A, B>(
            mapOf(
                    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress,
                    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java,
                    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java
            )
    )

    @Bean
    fun newJobProducer(): ProducerFactory<String, JobEvent> = producerFactory()

    @Bean
    fun kafkaTemplateNewJob(): KafkaTemplate<String, JobEvent> = KafkaTemplate(newJobProducer())
}
