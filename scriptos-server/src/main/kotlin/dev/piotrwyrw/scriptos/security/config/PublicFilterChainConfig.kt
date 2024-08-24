package dev.piotrwyrw.scriptos.security.config

import dev.piotrwyrw.scriptos.config.AuthConfiguration
import dev.piotrwyrw.scriptos.security.filter.CorsFilter
import dev.piotrwyrw.scriptos.security.filter.RateLimitFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered.HIGHEST_PRECEDENCE
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class PublicFilterChainConfig(
    val authConfiguration: AuthConfiguration
) {

    @Bean
    @Order(HIGHEST_PRECEDENCE)
    fun publicFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            securityMatcher("/login", "/register", "/swagger-ui/**", "/v3/**", "/api-docs")
            csrf { disable() }
            cors { disable() }
            authorizeRequests {
                authorize(anyRequest, permitAll)
            }
            addFilterBefore<UsernamePasswordAuthenticationFilter>(CorsFilter())
            addFilterAfter<CorsFilter>(RateLimitFilter(authConfiguration.authRequestDelay))
        }
        return http.build()
    }

}