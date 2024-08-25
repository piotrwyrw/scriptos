package dev.piotrwyrw.scriptos.service

import com.fasterxml.jackson.databind.ObjectMapper
import dev.piotrwyrw.scriptos.config.StorageConfig
import dev.piotrwyrw.scriptos.exception.ScriptosException
import dev.piotrwyrw.scriptos.persistence.model.DocumentEntity
import jakarta.transaction.Transactional
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.util.concurrent.CompletableFuture
import kotlin.math.max
import kotlin.math.round

@Service
class StorageService(
    val storageConfig: StorageConfig,
    val groupService: GroupService,
    private val jacksonObjectMapper: ObjectMapper,
) {

    val logger: Logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun store(
        resource: ByteArrayResource,
        document: DocumentEntity,
        fileSize: Long,
        documentService: DocumentService,
        filename: String
    ) {

        logger.info("Starting upload for document ${document.id} [${filename}]")

        val group = groupService.byId(document.groupId) ?: throw ScriptosException(
            "Could not find the requested group",
            HttpStatus.NOT_FOUND
        )

        val dir = File(storageConfig.directory, group.name)
        dir.mkdirs()

        val extension: String

        filename.split(".").let {
            if (it.size <= 1)
                throw ScriptosException(
                    "Could not extract file extension from the given file",
                    HttpStatus.UNPROCESSABLE_ENTITY
                )

            extension = it.subList(1, it.size).reduce(String::plus)
        }

        if (!storageConfig.acceptedExtensions.contains(extension.uppercase()))
            throw ScriptosException(
                "The extension '$extension' is not allowed. Acceptable extensions are ${
                    jacksonObjectMapper.writeValueAsString(
                        storageConfig.acceptedExtensions
                    )
                }",
                HttpStatus.UNPROCESSABLE_ENTITY
            )

        val targetFile = File(storageConfig.directory, "${group.name}/${document.id}.$extension")

        val reader = InputStreamReader(resource.inputStream)

        val outputStream = FileOutputStream(targetFile)

        var bytesUploaded: Long = 100
        var uploadPercentage: Double
        val computePercentage: () -> Unit = {
            uploadPercentage = round((100.0 / fileSize.toDouble()) * bytesUploaded)
            documentService.updateMonitor(document, "${uploadPercentage.toInt()}%")
        }
        val computationFrequency = max(fileSize / 50, 100)

        val maxRetries = 10

        CompletableFuture.supplyAsync {
            // The monitor can sometimes not be found for reasons(?) Retry till the problem goes away.
            var retries = 0
            while (documentService.monitorService.monitorRepository.findById(document.statusMonitor).isEmpty) {
                if (retries >= maxRetries)
                    throw RuntimeException("Could not find monitor after $retries retries. Aborting upload.")
                retries++
            }

            while (true) {
                val byte = reader.read()
                if (byte == -1)
                    break
                outputStream.write(byte)
                bytesUploaded++
                if (bytesUploaded % computationFrequency == (0).toLong())
                    computePercentage()
            }

            documentService.updateMonitor(document, "100%", done = true)
            outputStream.close()
            logger.info("Upload finished for ${document.id}")
        }.exceptionally {
            if (it.cause == null) {
                logger.info("An error occurred while trying to upload the document.")
                return@exceptionally
            }
            logger.info("Error occurred while uploading document: ${it.cause!!.javaClass} - ${it.cause!!.message}")
        }
    }

}