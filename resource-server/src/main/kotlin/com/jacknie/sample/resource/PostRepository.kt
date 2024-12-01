package com.jacknie.sample.resource

import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository: JpaRepository<PostEntity, Long> {
}