package dev.piotrwyrw.scriptos.service

import dev.piotrwyrw.scriptos.api.model.LoginRequest
import dev.piotrwyrw.scriptos.security.auth.ScriptosAuthentication
import dev.piotrwyrw.scriptos.util.UtilService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    val sessionService: SessionService,
    val userService: UserService,
    val utilService: UtilService
) {

    fun authenticateRequest(token: String): Boolean {
        val session = sessionService.findSession(token) ?: return false
        val user = userService.findUser(session.userId) ?: return false
        sessionService.updateLastAccessed(session)
        SecurityContextHolder.getContext().authentication = ScriptosAuthentication(user, session)
        return true
    }

    fun login(username: String, password: String): String? {
        val user = userService.byUsername(username) ?: return null

        if (!utilService.comparePassword(password, user.passwordHash))
            return null

        val session = sessionService.createSession(user)
        SecurityContextHolder.getContext().authentication = ScriptosAuthentication(user, session)
        return session.token
    }

    fun login(request: LoginRequest) = login(request.username, request.password)

}