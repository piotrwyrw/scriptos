package dev.piotrwyrw.scriptos.config

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.io.File

@Component
@ConfigurationProperties(prefix = "scriptos.storage")
class StorageConfig(
    var directory: String = "scriptos-data",
    var acceptedExtensions: Array<String> = arrayOf("PDF", "PNG", "JPG", "JPEG")
) {

    val logger = LoggerFactory.getLogger(javaClass)

    @PostConstruct
    fun prepareDirectory() {
        val file = File(directory)

        if (!file.exists()) {
            file.mkdirs()
            logger.info("Created new Scriptos data directory at ${file.absolutePath}")
        } else if (!file.isDirectory)
            throw IllegalStateException("The Scriptos data directory \"${directory}\" exists but is not a directory.\"")
        else
            logger.info("Scriptos data directory exists at ${file.absolutePath}")
    }

}