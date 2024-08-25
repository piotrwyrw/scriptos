package dev.piotrwyrw.scriptos.persistence.repository

import dev.piotrwyrw.scriptos.persistence.model.MonitorEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MonitorRepository : JpaRepository<MonitorEntity, UUID>