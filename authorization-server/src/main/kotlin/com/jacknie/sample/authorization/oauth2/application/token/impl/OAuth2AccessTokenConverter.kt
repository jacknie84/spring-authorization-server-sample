package com.jacknie.sample.authorization.oauth2.application.token.impl

import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationEntity
import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationTokenEntity
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.oauth2.core.OAuth2Token
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType

class OAuth2AccessTokenConverter: AbstractOAuth2TokenConverter<OAuth2AccessToken>() {
    private val tokenTypeHandler = OAuth2AccessTokenTypeHandler()
    private val tokenScopesHandler = OAuth2AccessTokenScopesHandler()

    override val type: OAuth2TokenType get() = OAuth2TokenType.ACCESS_TOKEN
    override val javaClass: Class<OAuth2AccessToken> get() = OAuth2AccessToken::class.java

    override fun convertToOAuth2Token(entity: OAuth2AuthorizationTokenEntity) = entity.run {
        val tokenType = tokenTypeHandler.getOAuth2TokenFieldValue(this)
        val scopes = tokenScopesHandler.getOAuth2TokenFieldValue(this)
        OAuth2AccessToken(tokenType, value, issuedAt, expiredAt, scopes)
    }

    override fun convertToOAuth2AuthorizationTokenEntity(
        token: OAuth2Authorization.Token<out OAuth2Token>,
        authorizationEntity: OAuth2AuthorizationEntity
    ) = token.run {
        val entity = super.convertToOAuth2AuthorizationTokenEntity(token, authorizationEntity)
        val accessToken = token.token as OAuth2AccessToken
        tokenTypeHandler.setOAuth2AuthorizationTokenEntityMetadata(entity, accessToken.tokenType)
        tokenScopesHandler.setOAuth2AuthorizationTokenEntityMetadata(entity, accessToken.scopes)
        entity
    }
}