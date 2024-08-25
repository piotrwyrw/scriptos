package dev.piotrwyrw.scriptos.controller

import dev.piotrwyrw.scriptos.api.MonitoringApi
import dev.piotrwyrw.scriptos.api.model.MonitorResponse
import dev.piotrwyrw.scriptos.service.MonitorService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class MonitoringController(
    private val monitorService: MonitorService
) : MonitoringApi {

    override fun retrieveMonitorContent(id: UUID): ResponseEntity<MonitorResponse> {
        return ResponseEntity.ok(monitorService.retrieve(id))
    }

}