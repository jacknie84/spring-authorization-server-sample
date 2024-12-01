package com.jacknie.sample.resource

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Entity
@Table(name = "post")
@EntityListeners(value = [AuditingEntityListener::class])
data class PostEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var title: String,

    @Column(columnDefinition = "text")
    var content: String? = null,

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "createdUsername", referencedColumnName = "username", insertable = true, updatable = false)
    var createdBy: AuthorEntity? = null,

    @LastModifiedBy
    @ManyToOne
    @JoinColumn(name = "lastModifiedUsername", referencedColumnName = "username", insertable = true, updatable = true)
    var lastModifiedBy: AuthorEntity? = null,

    @CreatedDate
    @Column(nullable = false, insertable = true, updatable = false)
    @ColumnDefault(value = "current_timestamp")
    var createdDate: Instant? = null,

    @LastModifiedDate
    @Column(nullable = false, insertable = true, updatable = true)
    @ColumnDefault(value = "current_timestamp")
    var lastModifiedDate: Instant? = null,
)
