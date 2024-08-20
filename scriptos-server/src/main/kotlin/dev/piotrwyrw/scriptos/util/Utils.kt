package dev.piotrwyrw.scriptos.util

import dev.piotrwyrw.scriptos.persistence.model.SessionEntity
import dev.piotrwyrw.scriptos.persistence.model.UserEntity
import dev.piotrwyrw.scriptos.security.auth.ScriptosAuthentication
import org.springframework.security.core.context.SecurityContextHolder

fun currentlyAuthenticated(): ScriptosAuthentication? =
    SecurityContextHolder.getContext().authentication as ScriptosAuthentication?

fun currentUser(): UserEntity? = currentlyAuthenticated()?.user

fun currentSession(): SessionEntity? = currentlyAuthenticated()?.session
