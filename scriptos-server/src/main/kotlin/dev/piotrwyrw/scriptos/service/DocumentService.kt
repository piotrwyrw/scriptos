package dev.piotrwyrw.scriptos.service

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
import kotlin.concurrent.thread

@Service
@Transactional
class DocumentService(
    val documentRepository: DocumentRepository,
    val groupService: GroupService,
    val monitorService: MonitorService,
    val storageService: StorageService
) {

    val logger = LoggerFactory.getLogger(javaClass)

    fun createDocument(
        title: String,
        description: String,
        group: String,
        resource: ByteArrayResource,
        fileSize: Long
    ): DocumentEntity {
        val groupEntity = groupService.groupByName(group) ?: throw ScriptosException(
            "Could not find the requested group",
            HttpStatus.NOT_FOUND
        )

        if (documentRepository.findByTitleAndGroupId(title, groupEntity.id).isPresent)
            throw ScriptosException("A document with this name already exists in this group.", HttpStatus.CONFLICT)

        val author = currentUser()!!

        val entity = DocumentEntity()
        entity.title = title
        entity.description = description
        entity.groupId = groupEntity.id
        entity.createdAt = Instant.now()
        entity.authorId = author.id
        entity.byteSize = fileSize;

        documentRepository.save(entity)

        entity.statusMonitor = monitorService.newMonitor(entity.id)

        logger.info("The user \"${author.username}\" created a new document \"${title}\" in group \"${groupEntity.name}\"")

        thread {
            storageService.store(resource, entity, fileSize, this)
        }

        return entity
    }

    fun updateMonitor(document: DocumentEntity, content: String, done: Boolean = false) {
        monitorService.updateMonitor(document.statusMonitor, content, done = done)
        logger.info("Monitor ${document.statusMonitor}: \"${content}\"")
    }
}