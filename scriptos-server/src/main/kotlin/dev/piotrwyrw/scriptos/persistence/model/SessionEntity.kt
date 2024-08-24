package dev.piotrwyrw.scriptos.persistence.model

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "session")
class SessionEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue
    lateinit var id: UUID

    @Column(name = "token")
    lateinit var token: String

    @Column(name = "user_id")
    lateinit var userId: UUID

    @Column(name = "created_at")
    lateinit var createdAt: Instant

    @Column(name = "last_accessed")
    lateinit var accessedAt: Instant

    @Column(name = "flagged")
    var flagged: Boolean = false

}