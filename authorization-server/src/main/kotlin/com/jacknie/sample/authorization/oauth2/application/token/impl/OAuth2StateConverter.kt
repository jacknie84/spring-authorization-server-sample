package com.jacknie.sample.authorization.oauth2.application.token.impl

import com.jacknie.sample.authorization.oauth2.application.token.OAuth2TokenConverter
import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationEntity
import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationTokenEntity
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType

class OAuth2StateConverter: OAuth2TokenConverter<String, String> {
    override val type: OAuth2TokenType get() = OAuth2TokenType(OAuth2ParameterNames.STATE)
    override val javaClass: Class<String> get() = String::class.java

    override fun convertToOAuth2Token(entity: OAuth2AuthorizationTokenEntity): String {
        return entity.value
    }

    override fun convertToOAuth2AuthorizationTokenEntity(
        token: String,
        authorizationEntity: OAuth2AuthorizationEntity
    ) = OAuth2AuthorizationTokenEntity(
        authorizationId = authorizationEntity.id,
        type = type,
        value = token,
        issuedAt = null,
        expiredAt = null,
        metadata = mutableMapOf(),
        authorization = authorizationEntity
    )
}