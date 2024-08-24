package dev.piotrwyrw.scriptos.service

import dev.piotrwyrw.scriptos.api.model.RegisterRequest
import dev.piotrwyrw.scriptos.api.model.UserEditRequest
import dev.piotrwyrw.scriptos.config.AdminUserConfig
import dev.piotrwyrw.scriptos.exception.ScriptosException
import dev.piotrwyrw.scriptos.persistence.model.UserEntity
import dev.piotrwyrw.scriptos.persistence.repository.UserRepository
import dev.piotrwyrw.scriptos.util.UtilService
import dev.piotrwyrw.scriptos.util.currentUser
import dev.piotrwyrw.scriptos.util.isValidPassword
import dev.piotrwyrw.scriptos.util.isValidUsername
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Transactional
@Service
class UserService(
    val userRepository: UserRepository,
    val utilService: UtilService,
    val adminUserConfig: AdminUserConfig,
    val bCryptPasswordEncoder: BCryptPasswordEncoder
) {

    val logger = LoggerFactory.getLogger(javaClass)

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

    fun editUser(req: UserEditRequest) {
        val user = currentUser()!!

        if (req.newPassword != null) {
            if (user.id == systemAdministrator().id)
                throw ScriptosException(
                    "The password of the admin user can only be changed in the Scriptos config",
                    HttpStatus.BAD_REQUEST
                )
            if (!req.newPassword.isValidPassword())
                throw ScriptosException(
                    "The password does not match the required format",
                    HttpStatus.UNPROCESSABLE_ENTITY
                )

            user.passwordHash = bCryptPasswordEncoder.encode(req.newPassword)
        }

        logger.info("Updated password of user \"${user.username}\"")

        userRepository.save(user)
    }

}