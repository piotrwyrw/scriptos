package dev.piotrwyrw.scriptos.persistence.repository

import dev.piotrwyrw.scriptos.persistence.model.SessionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
interface SessionRepository : JpaRepository<SessionEntity, UUID> {

    @Query("select e from SessionEntity e where e.token = :token and e.accessedAt >= :lastAccessed and not e.flagged")
    fun findByTokenWithExpiration(token: String, lastAccessed: Instant): Optional<SessionEntity>

    @Query("select e from SessionEntity e where e.accessedAt < :lastAccessed or e.flagged")
    fun findExpired(lastAccessed: Instant): List<SessionEntity>

    fun findByToken(token: String): Optional<SessionEntity>

}