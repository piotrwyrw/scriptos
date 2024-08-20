package dev.piotrwyrw.scriptos.service

import dev.piotrwyrw.scriptos.api.model.RegisterRequest
import dev.piotrwyrw.scriptos.exception.ScriptosException
import dev.piotrwyrw.scriptos.persistence.model.UserEntity
import dev.piotrwyrw.scriptos.persistence.repository.UserRepository
import dev.piotrwyrw.scriptos.util.UtilService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class UserService(
    val userRepository: UserRepository,
    val utilService: UtilService
) {

    fun findUser(id: UUID): UserEntity? = userRepository.findById(id).getOrNull()

    fun byUsername(username: String): UserEntity? = userRepository.findByUsername(username).getOrNull()

    fun usernameTaken(username: String): Boolean = byUsername(username) != null

    private fun createUser(username: String, password: String): UserEntity {
        val entity = UserEntity()
        entity.username = username
        entity.passwordHash = utilService.encodePassword(password)
        entity.createdAt = Instant.now()

        userRepository.save(entity)

        return entity
    }

    fun register(username: String, password: String) {
        if (usernameTaken(username))
            throw ScriptosException("Username is already taken", HttpStatus.CONFLICT)

        createUser(username, password)
    }

    fun register(request: RegisterRequest) = register(request.username, request.password)

}