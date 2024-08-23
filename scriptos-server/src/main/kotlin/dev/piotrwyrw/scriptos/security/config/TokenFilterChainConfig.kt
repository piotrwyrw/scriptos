package dev.piotrwyrw.scriptos.security.config

import dev.piotrwyrw.scriptos.security.filter.CorsFilter
import dev.piotrwyrw.scriptos.security.filter.TokenAuthenticationFilter
import dev.piotrwyrw.scriptos.service.AuthenticationService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered.LOWEST_PRECEDENCE
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class TokenFilterChainConfig(
    val authenticationService: AuthenticationService
) {

    @Bean
    @Order(LOWEST_PRECEDENCE)
    fun tokenFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            securityMatcher("/**")
            csrf { disable() }
            cors { disable() }
            authorizeRequests {
                authorize(anyRequest, permitAll)
            }
            addFilterBefore<UsernamePasswordAuthenticationFilter>(CorsFilter())
            addFilterAfter<CorsFilter>(TokenAuthenticationFilter(authenticationService))
        }
        return http.build()
    }

}