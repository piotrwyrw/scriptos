package dev.piotrwyrw.scriptos.exception

import org.springframework.http.HttpStatus

class ScriptosException (
    override val message: String,
    val code: HttpStatus
) : RuntimeException(message)