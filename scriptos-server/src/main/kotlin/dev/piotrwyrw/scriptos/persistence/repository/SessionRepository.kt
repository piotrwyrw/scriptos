package dev.piotrwyrw.scriptos.persistence.repository

import dev.piotrwyrw.scriptos.persistence.model.SessionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
interface SessionRepository : JpaRepository<SessionEntity, UUID> {

    @Query("select e from SessionEntity e where e.token = :token and e.accessedAt >= :lastAccessed and e.createdAt >= :hardExpire")
    fun findByToken(token: String, lastAccessed: Instant, hardExpire: Instant): Optional<SessionEntity>

    @Query("select e from SessionEntity e where e.accessedAt < :lastAccessed and e.createdAt < :hardExpire")
    fun findExpired(lastAccessed: Instant, hardExpire: Instant): List<SessionEntity>

}