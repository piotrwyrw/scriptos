package dev.piotrwyrw.scriptos.security

import dev.piotrwyrw.scriptos.service.AuthenticationService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.web.filter.OncePerRequestFilter

class TokenAuthenticationFilter(
    private val authenticationService: AuthenticationService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        var token: String? = request.getHeader("Authorization")

        if (token == null) {
            sendForbidden(response)
            return
        }

        // Prepare the token
        if (token.startsWith("Bearer"))
            token = token.split(" ").let {
                if (it.size != 2) {
                    sendForbidden(response)
                    return
                }

                return@let it[1]
            }

        if (!authenticationService.authenticateRequest(token)) {
            sendForbidden(response)
            return
        }

        filterChain.doFilter(request, response)
    }

    private fun sendForbidden(resp: HttpServletResponse) {
        resp.contentType = "text/plain"
        resp.status = HttpStatus.FORBIDDEN.value()
        resp.writer.write("Access Denied")
    }

}