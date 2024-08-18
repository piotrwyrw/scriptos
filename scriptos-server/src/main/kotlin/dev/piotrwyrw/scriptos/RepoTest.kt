package dev.piotrwyrw.scriptos

import dev.piotrwyrw.scriptos.persistence.model.UserEntity
import dev.piotrwyrw.scriptos.persistence.repository.UserRepository
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class RepoTest(
    val repo: UserRepository
) {

    @PostConstruct
    fun stuff() {
        val e = UserEntity()
        e.username = "piotrwyrw"
        e.passwordHash = "idrhtg98wehg3485u3v4985"
        e.createdAt = Instant.now()
        repo.save(e)
    }

}