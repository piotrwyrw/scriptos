package dev.piotrwyrw.scriptos.config

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "scriptos.auth")
class AuthConfiguration(
    var unusedTokenExpiration: Long = 2880,
    var hardTokenExpiration: Long = 2800 * 2,
    var authRequestDelay: Long = 1000
) {

    val logger = LoggerFactory.getLogger(javaClass)

    @PostConstruct
    fun debugLogConfigValues() {
        logger.info("Unused tokens set to expire after $unusedTokenExpiration minutes, hard expiration after $hardTokenExpiration minutes")
    }

}