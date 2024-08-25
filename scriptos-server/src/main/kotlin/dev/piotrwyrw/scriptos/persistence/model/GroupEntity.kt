package dev.piotrwyrw.scriptos.persistence.model

import jakarta.persistence.*
import java.util.*
import javax.print.Doc

@Entity
@Table(name = "\"group\"")
class GroupEntity {

    @Id
    @GeneratedValue
    lateinit var id: UUID

    @Column(name = "name")
    lateinit var name: String

    @Column(name = "description")
    lateinit var description: String

    @Column(name = "admin_user")
    lateinit var adminUser: UUID

    @ManyToMany(mappedBy = "groups")
    var members: MutableSet<UserEntity> = mutableSetOf()

    @OneToMany
    @JoinTable(
        name = "document",
        joinColumns = [JoinColumn(name = "group_id")],
        inverseJoinColumns = [JoinColumn(name = "id")]
    )
    var documents: MutableSet<DocumentEntity> = mutableSetOf()

}