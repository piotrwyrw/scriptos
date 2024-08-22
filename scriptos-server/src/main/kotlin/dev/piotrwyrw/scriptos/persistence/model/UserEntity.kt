package dev.piotrwyrw.scriptos.persistence.model

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "\"user\"")
class UserEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue
    lateinit var id: UUID

    @Column(name = "created_at")
    lateinit var createdAt: Instant

    @Column(name = "username")
    lateinit var username: String

    @Column(name = "password_hash")
    lateinit var passwordHash: String

    @ManyToMany
    @JoinTable(
        name = "group_membership",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "group_id")]
    )
    var groups: MutableSet<GroupEntity> = mutableSetOf()

}