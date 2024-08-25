package dev.piotrwyrw.scriptos.persistence.model

import jakarta.persistence.*
import java.time.Instant
import java.util.*

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

    @Column(name = "file_type")
    lateinit var fileType: String

    @Column(name = "author_id")
    lateinit var authorId: UUID

    @Column(name = "byte_size")
    var byteSize: Long = 0

    @Column(name = "status_monitor")
    lateinit var statusMonitor: UUID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", insertable = false, updatable = false)
    lateinit var group: GroupEntity

}