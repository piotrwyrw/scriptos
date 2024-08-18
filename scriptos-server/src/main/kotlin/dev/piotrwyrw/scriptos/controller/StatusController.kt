package dev.piotrwyrw.scriptos.controller

import dev.piotrwyrw.scriptos.api.StatusApi
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class StatusController : StatusApi {

    override fun scriptosStatus(): ResponseEntity<String> {
        return ResponseEntity.ok("Scriptos server is up and running.");
    }

}