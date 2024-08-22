package dev.piotrwyrw.scriptos.config

import dev.piotrwyrw.scriptos.util.isValidPassword
import dev.piotrwyrw.scriptos.util.isValidUsername
import jakarta.annotation.PostConstruct
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "scriptos.admin")
class AdminUserConfig(
    var username: String = "admin",
    var password: String = "VGhpcyBpcyB0b3RhbGx5IGEgdmVyeSBjb21wbGV4IHBhc3N3b3JkIGZvciBhIFNjcmlwdG9zIGFkbWluIHVzZXI"
) {

    @PostConstruct
    fun validateEntries() {
        if (!username.isValidUsername() || !password.isValidPassword())
            throw RuntimeException("The username or password set for the admin user is not valid.")
    }

}