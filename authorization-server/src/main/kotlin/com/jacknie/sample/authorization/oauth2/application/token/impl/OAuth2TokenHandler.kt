package com.jacknie.sample.authorization.oauth2.application.token.impl

import com.jacknie.sample.authorization.oauth2.application.token.OAuth2AuthorizationTokenEntityHandler
import com.jacknie.sample.authorization.oauth2.application.token.OAuth2TokenConverter
import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationEntity
import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationTokenEntity
import org.springframework.security.oauth2.core.OAuth2Token
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType

class OAuth2TokenHandler(
    private val converter: OAuth2TokenConverter<out OAuth2Token, OAuth2Authorization.Token<out OAuth2Token>>
): OAuth2AuthorizationTokenEntityHandler {
    override val type: OAuth2TokenType get() = converter.type

    override fun getOAuth2AuthorizationTokenEntity(
        authorization: OAuth2Authorization,
        authorizationEntity: OAuth2AuthorizationEntity
    ): OAuth2AuthorizationTokenEntity? {
        return authorization.getToken(converter.javaClass)
            ?.let { converter.convertToOAuth2AuthorizationTokenEntity(it, authorizationEntity) }
    }

    override fun setOAuth2AuthorizationTokenEntity(
        builder: OAuth2Authorization.Builder,
        entity: OAuth2AuthorizationTokenEntity
    ) {
        val token = converter.convertToOAuth2Token(entity)
        builder.token(token) { it += entity.metadata }
    }
}