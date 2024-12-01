package com.jacknie.sample.resource

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
class JpaConfiguration(private val authorRepository: AuthorRepository) {

    @Bean
    fun auditorAware() = AuditorAware {
        val context = SecurityContextHolder.getContext()
        val username = context.authentication.name
        authorRepository.findById(username).or { Optional.of(authorRepository.save(AuthorEntity(username, username))) }
    }
}