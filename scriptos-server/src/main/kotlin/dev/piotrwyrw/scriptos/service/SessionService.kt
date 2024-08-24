package dev.piotrwyrw.scriptos.service

import dev.piotrwyrw.scriptos.config.AuthConfiguration
import dev.piotrwyrw.scriptos.persistence.model.SessionEntity
import dev.piotrwyrw.scriptos.persistence.model.UserEntity
import dev.piotrwyrw.scriptos.persistence.repository.SessionRepository
import dev.piotrwyrw.scriptos.util.UtilService
import dev.piotrwyrw.scriptos.util.currentSession
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import kotlin.jvm.optionals.getOrNull

@Transactional
@Service
class SessionService(
    val authConfig: AuthConfiguration,
    val sessionRepository: SessionRepository,
    val utilService: UtilService
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun createSession(user: UserEntity): SessionEntity {
        val session = SessionEntity()
        session.userId = user.id
        session.token = utilService.randomSessionTokenString()
        session.createdAt = Instant.now()
        session.accessedAt = Instant.now()
        sessionRepository.save(session)
        return session
    }

    fun updateLastAccessed(session: SessionEntity) {
        session.accessedAt = Instant.now()
        sessionRepository.save(session)
    }

    private fun lastAccessedExpiration(): Instant {
        return Instant.now().minus(authConfig.unusedTokenExpiration, ChronoUnit.MINUTES)
    }

    fun invalidateSession() {
        val session = sessionRepository.findByToken(currentSession()!!.token).getOrNull() ?: return
        session.flagged = true
        sessionRepository.save(session)
    }

    fun findSession(token: String): SessionEntity? {
        val expiry = lastAccessedExpiration()
        return sessionRepository.findByTokenWithExpiration(token, expiry).getOrNull()
    }

    @Scheduled(fixedRate = 20, timeUnit = TimeUnit.SECONDS)
    fun deleteExpiredSessions() {
        val expiry = lastAccessedExpiration()
        var length: Int
        sessionRepository.findExpired(expiry).let { _it ->
            length = _it.size
            _it.forEach {
                sessionRepository.deleteById(it.id)
            }
        }
        if (length == 0)
            logger.info("No expired tokens to remove.")
        else
            logger.info("Removed $length expired tokens.")
    }
}