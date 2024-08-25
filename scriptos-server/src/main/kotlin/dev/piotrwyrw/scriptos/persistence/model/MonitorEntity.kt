package dev.piotrwyrw.scriptos.persistence.model

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "monitor")
class MonitorEntity {

    @GeneratedValue
    @Id
    lateinit var id: UUID

    @Column(name = "content")
    lateinit var content: String

    @Column(name = "created_at")
    lateinit var createdAt: Instant

    @Column(name = "document_id")
    lateinit var documentId: UUID

    @Column(name = "done")
    var done: Boolean = false

}