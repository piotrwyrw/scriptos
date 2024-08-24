package dev.piotrwyrw.scriptos.persistence.model

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "document")
class DocumentEntity {

    @GeneratedValue
    @Id
    lateinit var id: UUID

    @Column(name = "title")
    lateinit var title: String

    @Column(name = "description")
    lateinit var description: String

    @Column(name = "created_at")
    lateinit var createdAt: Instant

    @Column(name = "group_id")
    lateinit var groupId: UUID

    @Column(name = "author_id")
    lateinit var authorId: UUID

}