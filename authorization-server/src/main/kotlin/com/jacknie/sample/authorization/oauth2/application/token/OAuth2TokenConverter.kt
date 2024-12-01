package com.jacknie.sample.authorization.oauth2.application.token

import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationEntity
import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationTokenEntity
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType

interface OAuth2TokenConverter<T, K> {
    val type: OAuth2TokenType
    val javaClass: Class<T>
    fun convertToOAuth2Token(entity: OAuth2AuthorizationTokenEntity): T
    fun convertToOAuth2AuthorizationTokenEntity(
        token: K,
        authorizationEntity: OAuth2AuthorizationEntity
    ): OAuth2AuthorizationTokenEntity
}