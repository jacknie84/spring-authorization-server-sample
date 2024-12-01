package com.jacknie.sample.authorization.oauth2.application.token.impl

import com.jacknie.sample.authorization.oauth2.application.token.OAuth2TokenConverter
import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationEntity
import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationTokenEntity
import org.springframework.security.oauth2.core.OAuth2Token
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization

abstract class AbstractOAuth2TokenConverter<T>:
    OAuth2TokenConverter<T, OAuth2Authorization.Token<out OAuth2Token>> {
    override fun convertToOAuth2AuthorizationTokenEntity(
        token: OAuth2Authorization.Token<out OAuth2Token>,
        authorizationEntity: OAuth2AuthorizationEntity
    ) = token.run {
        OAuth2AuthorizationTokenEntity(
            authorizationId = authorizationEntity.id,
            type = type,
            value = this.token.tokenValue,
            issuedAt = this.token.issuedAt,
            expiredAt = this.token.expiresAt,
            metadata = this.metadata.toMutableMap(),
            authorization = authorizationEntity,
        )
    }
}