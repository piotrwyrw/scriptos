package dev.piotrwyrw.scriptos.persistence.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "\"group\"")
class GroupEntity {

    @Id
    @GeneratedValue
    lateinit var id: UUID

    @Column(name = "name")
    lateinit var name: String

    @Column(name = "admin_user")
    lateinit var adminUser: UUID

    @ManyToMany(mappedBy = "groups")
    var members: MutableSet<UserEntity> = mutableSetOf()

}