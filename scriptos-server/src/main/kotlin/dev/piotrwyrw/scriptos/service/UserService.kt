package dev.piotrwyrw.scriptos.service

import dev.piotrwyrw.scriptos.api.model.RegisterRequest
import dev.piotrwyrw.scriptos.config.AdminUserConfig
import dev.piotrwyrw.scriptos.exception.ScriptosException
import dev.piotrwyrw.scriptos.persistence.model.UserEntity
import dev.piotrwyrw.scriptos.persistence.repository.UserRepository
import dev.piotrwyrw.scriptos.util.UtilService
import dev.piotrwyrw.scriptos.util.isValidPassword
import dev.piotrwyrw.scriptos.util.isValidUsername
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class UserService(
    val userRepository: UserRepository,
    val utilService: UtilService,
    val adminUserConfig: AdminUserConfig
) {

    fun systemAdministrator(): UserEntity = byUsername(adminUserConfig.username)!!

    fun findUser(id: UUID): UserEntity? = userRepository.findById(id).getOrNull()

    fun byUsername(username: String): UserEntity? = userRepository.findByUsername(username).getOrNull()

    fun usernameTaken(username: String): Boolean = byUsername(username) != null

    fun createUser(username: String, password: String): UserEntity {
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

        if (!username.isValidUsername())
            throw ScriptosException("The username does not match the required pattern", HttpStatus.UNPROCESSABLE_ENTITY)

        if (!password.isValidPassword())
            throw ScriptosException(
                "The password must not contain leading or trailing whitespaces and be at least 8 characters long",
                HttpStatus.UNPROCESSABLE_ENTITY
            )

        createUser(username, password)
    }

    fun register(request: RegisterRequest) = register(request.username, request.password)

}