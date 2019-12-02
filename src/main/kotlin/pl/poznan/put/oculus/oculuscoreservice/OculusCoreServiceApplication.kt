package pl.poznan.put.oculus.oculuscoreservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@ComponentScan("pl.poznan.put.oculus")
@EnableScheduling
class OculusCoreServiceApplication

fun main(args: Array<String>) {
    runApplication<OculusCoreServiceApplication>(*args)
}
