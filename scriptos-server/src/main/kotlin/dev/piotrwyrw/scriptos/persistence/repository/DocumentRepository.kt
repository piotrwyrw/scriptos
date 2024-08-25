package dev.piotrwyrw.scriptos.persistence.repository

import dev.piotrwyrw.scriptos.persistence.model.DocumentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface DocumentRepository : JpaRepository<DocumentEntity, UUID> {

    fun findByTitleAndGroupId(title: String, groupId: UUID): Optional<DocumentEntity>

    @Query("""select sum(d.byteSize) from DocumentEntity d""")
    fun quantifyUsedDiskBytes(): Long?

}