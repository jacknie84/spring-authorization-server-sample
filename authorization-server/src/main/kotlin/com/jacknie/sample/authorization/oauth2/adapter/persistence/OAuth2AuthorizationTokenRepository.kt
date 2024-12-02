package com.jacknie.sample.authorization.oauth2.adapter.persistence

import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationTokenEntity
import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationTokenId
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface OAuth2AuthorizationTokenRepository: JpaRepository<OAuth2AuthorizationTokenEntity, OAuth2AuthorizationTokenId> {
    fun findAllByAuthorizationId(authorizationId: String): List<OAuth2AuthorizationTokenEntity>
    fun findByValue(token: String): Optional<OAuth2AuthorizationTokenEntity>
    fun findByValueAndType(token: String, type: String): Optional<OAuth2AuthorizationTokenEntity>
}