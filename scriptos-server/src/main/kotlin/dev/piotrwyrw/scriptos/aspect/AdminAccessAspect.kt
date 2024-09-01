package dev.piotrwyrw.scriptos.aspect

import dev.piotrwyrw.scriptos.exception.ScriptosException
import dev.piotrwyrw.scriptos.service.UserService
import dev.piotrwyrw.scriptos.util.currentUser
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Aspect
@Component
class AdminAccessAspect {

    @Autowired
    lateinit var userService: UserService

    @Around("@annotation(AdminOnly)")
    fun ensureAdminAuth(join: ProceedingJoinPoint) {
        if ((currentUser() ?: return).id != userService.systemAdministrator().id)
            throw ScriptosException("Access denied", HttpStatus.FORBIDDEN)

        join.proceed()
    }

}