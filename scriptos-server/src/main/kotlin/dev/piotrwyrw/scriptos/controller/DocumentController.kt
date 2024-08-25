package dev.piotrwyrw.scriptos.controller

import dev.piotrwyrw.scriptos.api.DocumentApi
import dev.piotrwyrw.scriptos.api.model.MonitoringIdResponse
import dev.piotrwyrw.scriptos.service.DocumentService
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class DocumentController(
    private val documentService: DocumentService
) : DocumentApi {

    override fun documentUpload(
        title: String,
        description: String,
        group: String,
        filename: Resource
    ): ResponseEntity<MonitoringIdResponse> {
        val resource = filename as ByteArrayResource
        val entity = documentService.createDocument(title.trim(), description.trim(), group, resource, filename.contentLength())
        return ResponseEntity.ok(MonitoringIdResponse(entity.statusMonitor))
    }
}