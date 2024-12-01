package com.jacknie.sample.authorization.oauth2.application.token.impl

import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationTokenEntity
import org.springframework.security.oauth2.core.OAuth2RefreshToken
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType

class OAuth2RefreshTokenConverter: AbstractOAuth2TokenConverter<OAuth2RefreshToken>() {
    override val type: OAuth2TokenType get() = OAuth2TokenType.REFRESH_TOKEN
    override val javaClass: Class<OAuth2RefreshToken> get() = OAuth2RefreshToken::class.java

    override fun convertToOAuth2Token(entity: OAuth2AuthorizationTokenEntity) = entity.run {
        OAuth2RefreshToken(value, issuedAt, expiredAt)
    }
}