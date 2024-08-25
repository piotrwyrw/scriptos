package dev.piotrwyrw.scriptos.service

import dev.piotrwyrw.scriptos.config.StorageConfig
import dev.piotrwyrw.scriptos.exception.ScriptosException
import dev.piotrwyrw.scriptos.persistence.model.DocumentEntity
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpStatus
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypes
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import kotlin.math.max
import kotlin.math.round

@Service
@Transactional
class StorageService(
    val storageConfig: StorageConfig,
    val groupService: GroupService,
) {

    val logger = LoggerFactory.getLogger(javaClass)

    fun store(resource: ByteArrayResource, document: DocumentEntity, fileSize: Long, documentService: DocumentService) {

        logger.info("Starting upload for document ${document.id}")

        val group = groupService.byId(document.groupId) ?: throw ScriptosException(
            "Could not find the requested group",
            HttpStatus.NOT_FOUND
        )

        val dir = File(storageConfig.directory, group.name)
        dir.mkdirs()

        val targetFile = File(storageConfig.directory, "${group.name}/${document.id}")

        val reader = InputStreamReader(resource.inputStream)

        val outputStream = FileOutputStream(targetFile)

        var byte: Int

        var bytesUploaded: Long = 100
        var uploadPercentage: Double
        val computePercentage: () -> Unit = {
            uploadPercentage = round((100.0 / fileSize.toDouble()) * bytesUploaded)
            documentService.updateMonitor(document, "${uploadPercentage.toInt()}%")
        }
        val computationFrequency = max(fileSize / 50, 100)

        while (reader.read().also { byte = it } >= 0) {
            outputStream.write(byte)
            bytesUploaded++
            if (bytesUploaded % computationFrequency == (0).toLong())
                computePercentage()
        }

        documentService.updateMonitor(document, "100%", done = true)

        outputStream.close()

        logger.info("Upload finished for ${document.id}")
    }

}