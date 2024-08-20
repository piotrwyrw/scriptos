package dev.piotrwyrw.scriptos.persistence.repository

import dev.piotrwyrw.scriptos.persistence.model.GroupEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface GroupRepository : JpaRepository<GroupEntity, UUID> {

    fun findByName(name: String): Optional<GroupEntity>

}