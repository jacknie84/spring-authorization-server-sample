package com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.token

import com.jacknie.sample.authorization.oauth2.application.authorization.OAuth2Authorization
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType

interface OAuth2TokenConverter<T, K> {
    val type: OAuth2TokenType
    val javaClass: Class<T>
    fun convertToOAuth2Token(token: OAuth2Authorization.Token): T
    fun convertToOAuth2AuthorizationToken(token: K): OAuth2Authorization.Token
}