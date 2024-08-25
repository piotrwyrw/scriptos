package dev.piotrwyrw.scriptos.service

import dev.piotrwyrw.scriptos.api.model.MonitorResponse
import dev.piotrwyrw.scriptos.exception.ScriptosException
import dev.piotrwyrw.scriptos.persistence.model.MonitorEntity
import dev.piotrwyrw.scriptos.persistence.repository.MonitorRepository
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
@Transactional
class MonitorService(
    val monitorRepository: MonitorRepository
) {

    fun newMonitor(document: UUID): UUID {
        val monitor = MonitorEntity()
        monitor.createdAt = Instant.now()
        monitor.content = "0%"
        monitor.documentId = document
        monitorRepository.save(monitor)
        return monitor.id
    }

    private fun getMonitor(id: UUID, perms: Boolean = false): MonitorEntity {
        val monitor = monitorRepository.findById(id).getOrNull()
            ?: throw ScriptosException("Could not find the requested monitor '$id'", HttpStatus.NOT_FOUND)

        // TODO Validate permissions

        return monitor
    }

    fun updateMonitor(id: UUID, content: String, done: Boolean = false) {
        val monitor = getMonitor(id)

        monitor.content = content
        monitor.done = done

        monitorRepository.save(monitor)
    }

    fun content(id: UUID): String {
        val monitor = getMonitor(id)
        return monitor.content
    }

    fun retrieve(id: UUID): MonitorResponse {
        val monitor = getMonitor(id)
        return MonitorResponse(monitor.id, monitor.content, monitor.done)
    }

}