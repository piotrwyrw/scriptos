package dev.piotrwyrw.scriptos

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ScriptosServerApplication

fun main(args: Array<String>) {
    runApplication<ScriptosServerApplication>(*args)
}
