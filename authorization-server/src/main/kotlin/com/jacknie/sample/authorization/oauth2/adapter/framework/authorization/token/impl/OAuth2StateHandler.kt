package com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.token.impl

import com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.FrameworkOAuth2Authorization
import com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.FrameworkOAuth2AuthorizationBuilder
import com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.token.OAuth2AuthorizationTokenEntityHandler
import com.jacknie.sample.authorization.oauth2.application.authorization.OAuth2Authorization
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType

class OAuth2StateHandler: OAuth2AuthorizationTokenEntityHandler {
    private val converter = OAuth2StateConverter()

    override val type: OAuth2TokenType get() = converter.type

    override fun getOAuth2AuthorizationToken(authorization: FrameworkOAuth2Authorization): OAuth2Authorization.Token? {
        return authorization.getAttribute<String>(converter.type.value)
            ?.let { converter.convertToOAuth2AuthorizationToken(it) }
    }

    override fun setOAuth2AuthorizationToken(
        builder: FrameworkOAuth2AuthorizationBuilder,
        token: OAuth2Authorization.Token
    ) {
        val state = converter.convertToOAuth2Token(token)
        builder.attribute(converter.type.value, state)
    }
}