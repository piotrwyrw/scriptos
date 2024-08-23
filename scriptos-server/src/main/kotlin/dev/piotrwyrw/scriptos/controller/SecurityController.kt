package dev.piotrwyrw.scriptos.controller

import dev.piotrwyrw.scriptos.api.SecurityApi
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class SecurityController : SecurityApi {

    override fun securedRoute(): ResponseEntity<Unit> {
        return ResponseEntity.ok().build()
    }

}