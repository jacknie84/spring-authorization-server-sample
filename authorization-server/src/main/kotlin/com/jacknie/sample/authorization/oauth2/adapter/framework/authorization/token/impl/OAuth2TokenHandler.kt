package com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.token.impl

import com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.FrameworkOAuth2Authorization
import com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.FrameworkOAuth2AuthorizationBuilder
import com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.FrameworkOAuth2AuthorizationToken
import com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.token.OAuth2AuthorizationTokenEntityHandler
import com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.token.OAuth2TokenConverter
import com.jacknie.sample.authorization.oauth2.application.authorization.OAuth2Authorization
import org.springframework.security.oauth2.core.OAuth2Token
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType

class OAuth2TokenHandler(
    private val converter: OAuth2TokenConverter<out OAuth2Token, FrameworkOAuth2AuthorizationToken>
): OAuth2AuthorizationTokenEntityHandler {
    override val type: OAuth2TokenType get() = converter.type

    override fun getOAuth2AuthorizationToken(authorization: FrameworkOAuth2Authorization): OAuth2Authorization.Token? {
        return authorization.getToken(converter.javaClass)
            ?.let { converter.convertToOAuth2AuthorizationToken(it) }
    }

    override fun setOAuth2AuthorizationToken(
        builder: FrameworkOAuth2AuthorizationBuilder,
        token: OAuth2Authorization.Token
    ) {
        val oauth2Token = converter.convertToOAuth2Token(token)
        builder.token(oauth2Token) { it += token.metadata }
    }
}