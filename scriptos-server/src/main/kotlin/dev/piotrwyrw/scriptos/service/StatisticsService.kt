package dev.piotrwyrw.scriptos.service

import dev.piotrwyrw.scriptos.persistence.repository.DocumentRepository
import org.springframework.stereotype.Service

@Service
class StatisticsService(
    val documentRepository: DocumentRepository
) {

    fun diskUsage(): Long {
        return documentRepository.quantifyUsedDiskBytes() ?: 0
    }

}