package com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.token.impl

import com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.token.OAuth2TokenConverter
import com.jacknie.sample.authorization.oauth2.application.authorization.OAuth2Authorization
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType

class OAuth2StateConverter: OAuth2TokenConverter<String, String> {
    override val type: OAuth2TokenType get() = OAuth2TokenType(OAuth2ParameterNames.STATE)
    override val javaClass: Class<String> get() = String::class.java

    override fun convertToOAuth2Token(token: OAuth2Authorization.Token): String {
        return token.value
    }

    override fun convertToOAuth2AuthorizationToken(token: String) = OAuth2Authorization.Token(
        type = type.value,
        value = token,
        issuedAt = null,
        expiredAt = null,
        metadata = mutableMapOf(),
    )
}