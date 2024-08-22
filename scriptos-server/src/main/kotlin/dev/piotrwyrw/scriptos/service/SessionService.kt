package dev.piotrwyrw.scriptos.service

import dev.piotrwyrw.scriptos.config.AuthConfiguration
import dev.piotrwyrw.scriptos.persistence.model.SessionEntity
import dev.piotrwyrw.scriptos.persistence.model.UserEntity
import dev.piotrwyrw.scriptos.persistence.repository.SessionRepository
import dev.piotrwyrw.scriptos.util.UtilService
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

    private fun expiryValues(): Pair<Instant, Instant> {
        val lastAccessed = Instant.now().minus(authConfig.unusedTokenExpiration, ChronoUnit.MINUTES)
        val hardExpire = Instant.now().minus(authConfig.hardTokenExpiration, ChronoUnit.MINUTES)
        return lastAccessed to hardExpire
    }

    fun findSession(token: String): SessionEntity? {
        val expiry = expiryValues()
        return sessionRepository.findByToken(token, expiry.first, expiry.second).getOrNull()
    }

    @Scheduled(fixedRate = 20, timeUnit = TimeUnit.SECONDS)
    fun deleteExpiredSessions() {
        val expiry = expiryValues()
        var length = 0
        sessionRepository.findExpired(expiry.first, expiry.second).let { _it ->
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