package com.jacknie.sample.resource

import org.springframework.data.jpa.repository.JpaRepository

interface AuthorRepository: JpaRepository<AuthorEntity, String> {
}