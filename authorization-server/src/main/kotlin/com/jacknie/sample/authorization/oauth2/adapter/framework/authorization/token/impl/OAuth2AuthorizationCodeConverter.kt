package com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.token.impl

import com.jacknie.sample.authorization.oauth2.application.authorization.OAuth2Authorization
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType

class OAuth2AuthorizationCodeConverter: AbstractOAuth2TokenConverter<OAuth2AuthorizationCode>() {
    override val type: OAuth2TokenType get() = OAuth2TokenType(OAuth2ParameterNames.CODE)
    override val javaClass: Class<OAuth2AuthorizationCode> get() = OAuth2AuthorizationCode::class.java

    override fun convertToOAuth2Token(token: OAuth2Authorization.Token) = token.run {
        OAuth2AuthorizationCode(value, issuedAt, expiredAt)
    }
}