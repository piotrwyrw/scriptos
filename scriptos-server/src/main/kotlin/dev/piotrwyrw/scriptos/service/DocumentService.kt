package dev.piotrwyrw.scriptos.service

import com.fasterxml.jackson.databind.ObjectMapper
import dev.piotrwyrw.scriptos.config.StorageConfig
import dev.piotrwyrw.scriptos.exception.ScriptosException
import dev.piotrwyrw.scriptos.persistence.model.DocumentEntity
import dev.piotrwyrw.scriptos.persistence.repository.DocumentRepository
import dev.piotrwyrw.scriptos.util.currentUser
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class DocumentService(
    val documentRepository: DocumentRepository,
    val groupService: GroupService,
    val monitorService: MonitorService,
    val storageService: StorageService,
    val storageConfig: StorageConfig,
    private val jacksonObjectMapper: ObjectMapper
) {

    val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun createDocument(
        title: String,
        description: String,
        group: String,
        resource: ByteArrayResource,
        fileSize: Long,
        filename: String
    ): DocumentEntity {
        val groupEntity = groupService.groupByName(group) ?: throw ScriptosException(
            "Could not find the requested group",
            HttpStatus.NOT_FOUND
        )

        if (documentRepository.findByTitleAndGroupId(title, groupEntity.id).isPresent)
            throw ScriptosException("A document with this name already exists in this group.", HttpStatus.CONFLICT)

        val author = currentUser()!!

        val extension: String

        filename.split(".").let {
            if (it.size <= 1)
                throw ScriptosException(
                    "Could not extract file extension from the given file",
                    HttpStatus.UNPROCESSABLE_ENTITY
                )

            extension = it.last().lowercase()
        }

        if (!storageConfig.acceptedExtensions.contains(extension.uppercase()))
            throw ScriptosException(
                "The extension '$extension' is not allowed. Acceptable extensions are ${
                    jacksonObjectMapper.writeValueAsString(
                        storageConfig.acceptedExtensions
                    ).replace("[\"\\[\\]]".toRegex(), "").replace(",", ", ")
                }",
                HttpStatus.UNPROCESSABLE_ENTITY
            )

        val entity = DocumentEntity()
        entity.title = title
        entity.description = description
        entity.groupId = groupEntity.id
        entity.createdAt = Instant.now()
        entity.fileType = extension
        entity.authorId = author.id
        entity.byteSize = fileSize;

        documentRepository.save(entity)

        entity.statusMonitor = monitorService.newMonitor(entity.id)

        logger.info("The user \"${author.username}\" created a new document \"${title}\" in group \"${groupEntity.name}\"")

        storageService.store(resource, entity, fileSize, this, filename, extension)

        return entity
    }

    @Transactional
    fun updateMonitor(document: DocumentEntity, content: String, done: Boolean = false) {
        monitorService.updateMonitor(document.statusMonitor, content, done = done)
        logger.info("Monitor ${document.statusMonitor}: \"${content}\"")
    }
}