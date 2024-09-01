package dev.piotrwyrw.scriptos.exception

import dev.piotrwyrw.scriptos.api.model.ErrorResponse
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(ScriptosException::class)
    fun handle(e: ScriptosException, resp: HttpServletResponse): ResponseEntity<out Any> {
        if (e.code != HttpStatus.FORBIDDEN)
            return ResponseEntity.status(e.code).body(ErrorResponse(e.message, e.code.name.uppercase()))

        val entity = ResponseEntity<String>("Forbidden", HttpStatus.FORBIDDEN)
        return entity
    }

}