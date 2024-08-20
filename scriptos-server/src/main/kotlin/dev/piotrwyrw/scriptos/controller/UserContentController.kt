package dev.piotrwyrw.scriptos.controller

import dev.piotrwyrw.scriptos.api.UserContentApi
import dev.piotrwyrw.scriptos.util.currentUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class UserContentController : UserContentApi {

    override fun userContent(): ResponseEntity<Unit> {
        println("The currently logged is username is ${currentUser()!!.username}")
        return ResponseEntity.ok().build()
    }

}