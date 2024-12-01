package com.jacknie.sample.authorization.oauth2.authorization.adapter.persistence

import com.jacknie.sample.authorization.oauth2.authorization.domain.OAuth2AuthorizationTokenEntity
import com.jacknie.sample.authorization.oauth2.authorization.domain.OAuth2AuthorizationTokenId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import java.util.*

interface OAuth2AuthorizationTokenRepository: JpaRepository<OAuth2AuthorizationTokenEntity, OAuth2AuthorizationTokenId> {
    fun findAllByAuthorizationId(authorizationId: String): List<OAuth2AuthorizationTokenEntity>
    fun findByValue(token: String): Optional<OAuth2AuthorizationTokenEntity>
    fun findByValueAndType(token: String, type: OAuth2TokenType): Optional<OAuth2AuthorizationTokenEntity>
}