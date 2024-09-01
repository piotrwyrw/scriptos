package dev.piotrwyrw.scriptos.controller

import dev.piotrwyrw.scriptos.api.SecurityApi
import dev.piotrwyrw.scriptos.api.model.BasicUserDataResponse
import dev.piotrwyrw.scriptos.aspect.AdminOnly
import dev.piotrwyrw.scriptos.util.currentUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class SecurityController : SecurityApi {

    override fun securedRoute(): ResponseEntity<BasicUserDataResponse> {
        return ResponseEntity.ok(BasicUserDataResponse(currentUser()!!.username))
    }

    @AdminOnly
    override fun securedAdminRoute(): ResponseEntity<Unit> {
        return super.securedAdminRoute()
    }

}