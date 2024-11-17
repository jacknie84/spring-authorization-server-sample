package com.jacknie.sample.resource

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class Author(
    @Id
    val username: String,
    val displayName: String,
)
