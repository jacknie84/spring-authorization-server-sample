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
@EntityListeners(value = [AuditingEntityListener::class])
data class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var title: String,

    @Column(columnDefinition = "text")
    var content: String? = null,

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "createdUsername", referencedColumnName = "username", insertable = true, updatable = false)
    var createdBy: Author? = null,

    @LastModifiedBy
    @ManyToOne
    @JoinColumn(name = "lastModifiedUsername", referencedColumnName = "username", insertable = true, updatable = true)
    var lastModifiedBy: Author? = null,

    @CreatedDate
    @Column(nullable = false, insertable = true, updatable = false)
    @ColumnDefault(value = "current_timestamp")
    var createdDate: Instant? = null,

    @LastModifiedDate
    @Column(nullable = false, insertable = true, updatable = true)
    @ColumnDefault(value = "current_timestamp")
    var lastModifiedDate: Instant? = null,
)
