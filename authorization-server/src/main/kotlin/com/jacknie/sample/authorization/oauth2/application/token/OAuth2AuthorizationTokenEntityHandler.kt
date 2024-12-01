package com.jacknie.sample.authorization.oauth2.application.token

import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationEntity
import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationTokenEntity
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType

interface OAuth2AuthorizationTokenEntityHandler {
    val type: OAuth2TokenType
    fun getOAuth2AuthorizationTokenEntity(
        authorization: OAuth2Authorization,
        authorizationEntity: OAuth2AuthorizationEntity
    ): OAuth2AuthorizationTokenEntity?
    fun setOAuth2AuthorizationTokenEntity(
        builder: OAuth2Authorization.Builder,
        entity: OAuth2AuthorizationTokenEntity
    )
}