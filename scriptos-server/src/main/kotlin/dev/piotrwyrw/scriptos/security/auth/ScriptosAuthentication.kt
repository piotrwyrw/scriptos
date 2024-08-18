package dev.piotrwyrw.scriptos.security.auth

import dev.piotrwyrw.scriptos.persistence.model.SessionEntity
import dev.piotrwyrw.scriptos.persistence.model.UserEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class ScriptosAuthentication (
    val user: UserEntity,
    val session: SessionEntity
): Authentication {
    override fun getName(): String = user.username

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }

    override fun getCredentials(): Any {
        return ""
    }

    override fun getDetails(): Any {
        return ""
    }

    override fun getPrincipal(): Any {
        return this
    }

    override fun isAuthenticated(): Boolean {
        return true
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
    }
}