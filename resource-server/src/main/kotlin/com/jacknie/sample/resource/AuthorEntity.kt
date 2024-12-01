package com.jacknie.sample.resource

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "author")
data class AuthorEntity(
    @Id
    val username: String,
    val displayName: String,
)
