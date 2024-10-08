package dev.piotrwyrw.scriptos.controller

import dev.piotrwyrw.scriptos.api.UserApi
import dev.piotrwyrw.scriptos.api.model.*
import dev.piotrwyrw.scriptos.exception.ScriptosException
import dev.piotrwyrw.scriptos.service.AuthenticationService
import dev.piotrwyrw.scriptos.service.GroupService
import dev.piotrwyrw.scriptos.service.SessionService
import dev.piotrwyrw.scriptos.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    val authenticationService: AuthenticationService,
    val userService: UserService,
    val groupService: GroupService,
    private val sessionService: SessionService
) : UserApi {

    override fun userLogin(loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        val token = authenticationService.login(loginRequest)
            ?: throw ScriptosException("Could not find a user with the given credentials", HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(LoginResponse(token, loginRequest.username))
    }

    override fun userRegister(registerRequest: RegisterRequest): ResponseEntity<Unit> {
        userService.register(registerRequest)
        groupService.handleNewUser(registerRequest.username)
        return ResponseEntity.ok().build()
    }

    override fun dropSession(): ResponseEntity<Unit> {
        sessionService.invalidateSession()
        return ResponseEntity.ok().build()
    }

    override fun editUser(userEditRequest: UserEditRequest): ResponseEntity<Unit> {
        userService.editUser(userEditRequest)
        return ResponseEntity.ok().build()
    }
}