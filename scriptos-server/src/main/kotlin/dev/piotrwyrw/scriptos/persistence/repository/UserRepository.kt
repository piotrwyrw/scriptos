package dev.piotrwyrw.scriptos.persistence.repository

import dev.piotrwyrw.scriptos.persistence.model.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<UserEntity, UUID> {

    fun findByUsername(username: String): Optional<UserEntity>

    fun findByUsernameAndPasswordHash(username: String, passwordHash: Optional<String>): Optional<UserEntity>

}