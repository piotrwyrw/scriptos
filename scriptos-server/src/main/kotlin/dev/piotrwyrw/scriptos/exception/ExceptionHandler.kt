package dev.piotrwyrw.scriptos.exception

import dev.piotrwyrw.scriptos.api.model.ErrorResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(ScriptosException::class)
    fun handle(e: ScriptosException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(e.code).body(ErrorResponse(e.message, e.code.name.uppercase()))
    }

}