package com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.token

import com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.FrameworkOAuth2Authorization
import com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.FrameworkOAuth2AuthorizationBuilder
import com.jacknie.sample.authorization.oauth2.application.authorization.OAuth2Authorization
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType

interface OAuth2AuthorizationTokenEntityHandler {
    val type: OAuth2TokenType
    fun getOAuth2AuthorizationToken(authorization: FrameworkOAuth2Authorization): OAuth2Authorization.Token?
    fun setOAuth2AuthorizationToken(builder: FrameworkOAuth2AuthorizationBuilder, token: OAuth2Authorization.Token)
}