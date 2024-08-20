package dev.piotrwyrw.scriptos.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered.HIGHEST_PRECEDENCE
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.annotation.web.invoke

@Configuration
class PublicFilterChainConfig {

    @Bean
    @Order(HIGHEST_PRECEDENCE)
    fun publicFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            securityMatcher("/login", "/register")
            authorizeRequests {
                authorize(anyRequest, permitAll)
            }
        }
        return http.build()
    }

}