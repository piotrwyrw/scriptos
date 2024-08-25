package dev.piotrwyrw.scriptos.controller

import dev.piotrwyrw.scriptos.api.DocumentApi
import dev.piotrwyrw.scriptos.api.model.DocumentListingResponseInner
import dev.piotrwyrw.scriptos.api.model.MonitoringIdResponse
import dev.piotrwyrw.scriptos.exception.ScriptosException
import dev.piotrwyrw.scriptos.service.DocumentService
import dev.piotrwyrw.scriptos.service.GroupService
import dev.piotrwyrw.scriptos.service.UserService
import dev.piotrwyrw.scriptos.util.currentUser
import jakarta.transaction.Transactional
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class DocumentController(
    private val documentService: DocumentService,
    private val groupService: GroupService,
    private val userService: UserService
) : DocumentApi {

    @Transactional
    override fun documentUpload(
        title: String,
        description: String,
        group: String,
        filename: Resource
    ): ResponseEntity<MonitoringIdResponse> {
        val resource = filename as ByteArrayResource
        val entity = documentService.createDocument(
            title.trim(),
            description.trim(),
            group,
            resource,
            filename.contentLength(),
            resource.filename ?: ""
        )
        return ResponseEntity.ok(MonitoringIdResponse(entity.statusMonitor))
    }

    override fun getAllDocuments(name: String): ResponseEntity<List<DocumentListingResponseInner>> {
        val group = groupService.groupByName(name) ?: throw ScriptosException(
            "Could not find group '${name}'.",
            HttpStatus.NOT_FOUND
        )

        val user = currentUser()!!

        if (group.adminUser != user.id && group.members.none { it.id == user.id })
            throw ScriptosException(
                "You do not have permissions to access the documents of group '$name'",
                HttpStatus.FORBIDDEN
            )

        val dtos = group.documents.map {
            DocumentListingResponseInner(
                it.id,
                it.title,
                it.description,
                it.fileType,
                userService.findUser(it.authorId)!!.username,
                it.byteSize
            )
        }

        return ResponseEntity.ok(dtos)
    }
}