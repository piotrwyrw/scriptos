package dev.piotrwyrw.scriptos.persistence.repository

import dev.piotrwyrw.scriptos.persistence.model.DocumentEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface DocumentRepository : JpaRepository<DocumentEntity, UUID>