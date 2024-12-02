package com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.token.impl

import com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.FrameworkOAuth2AuthorizationToken
import com.jacknie.sample.authorization.oauth2.application.authorization.OAuth2Authorization
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType

class OAuth2AccessTokenConverter: AbstractOAuth2TokenConverter<OAuth2AccessToken>() {
    private val tokenTypeHandler = OAuth2AccessTokenTypeHandler()
    private val tokenScopesHandler = OAuth2AccessTokenScopesHandler()

    override val type: OAuth2TokenType get() = OAuth2TokenType.ACCESS_TOKEN
    override val javaClass: Class<OAuth2AccessToken> get() = OAuth2AccessToken::class.java

    override fun convertToOAuth2Token(token: OAuth2Authorization.Token) = token.run {
        val tokenType = tokenTypeHandler.getOAuth2TokenFieldValue(this)
        val scopes = tokenScopesHandler.getOAuth2TokenFieldValue(this)
        OAuth2AccessToken(tokenType, value, issuedAt, expiredAt, scopes)
    }

    override fun convertToOAuth2AuthorizationToken(token: FrameworkOAuth2AuthorizationToken) = token.run {
        val localToken = super.convertToOAuth2AuthorizationToken(token)
        val accessToken = token.token as OAuth2AccessToken
        tokenTypeHandler.setOAuth2AuthorizationTokenEntityMetadata(localToken, accessToken.tokenType)
        tokenScopesHandler.setOAuth2AuthorizationTokenEntityMetadata(localToken, accessToken.scopes)
        localToken
    }
}