package dev.piotrwyrw.scriptos.controller

import dev.piotrwyrw.scriptos.api.UserApi
import dev.piotrwyrw.scriptos.api.model.LoginRequest
import dev.piotrwyrw.scriptos.api.model.RegisterRequest
import dev.piotrwyrw.scriptos.api.model.TokenResponse
import dev.piotrwyrw.scriptos.exception.ScriptosException
import dev.piotrwyrw.scriptos.service.AuthenticationService
import dev.piotrwyrw.scriptos.service.GroupService
import dev.piotrwyrw.scriptos.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    val authenticationService: AuthenticationService,
    val userService: UserService,
    private val groupService: GroupService
) : UserApi {

    override fun userLogin(loginRequest: LoginRequest): ResponseEntity<TokenResponse> {
        val token = authenticationService.login(loginRequest)
            ?: throw ScriptosException("Could not find a user with the given credentials", HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(TokenResponse(token))
    }

    override fun userRegister(registerRequest: RegisterRequest): ResponseEntity<Unit> {
        userService.register(registerRequest)
        groupService.addUserToCommonGroup(registerRequest.username)
        return ResponseEntity.ok().build()
    }

}