package dev.piotrwyrw.scriptos

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableConfigurationProperties
@EnableScheduling
class ScriptosServerApplication

fun main(args: Array<String>) {
    runApplication<ScriptosServerApplication>(*args)
}
