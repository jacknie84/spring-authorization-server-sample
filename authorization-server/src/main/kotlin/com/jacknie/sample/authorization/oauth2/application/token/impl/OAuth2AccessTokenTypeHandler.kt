package com.jacknie.sample.authorization.oauth2.application.token.impl

import com.jacknie.sample.authorization.oauth2.application.token.OAuth2TokenMetadataHandler
import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationTokenEntity
import org.springframework.security.oauth2.core.OAuth2AccessToken

class OAuth2AccessTokenTypeHandler: OAuth2TokenMetadataHandler<OAuth2AccessToken.TokenType?> {
    override fun setOAuth2AuthorizationTokenEntityMetadata(
        entity: OAuth2AuthorizationTokenEntity,
        value: OAuth2AccessToken.TokenType?
    ) {
        value?.also { entity.metadata["access_token_type"] = it.value }
    }

    override fun getOAuth2TokenFieldValue(entity: OAuth2AuthorizationTokenEntity): OAuth2AccessToken.TokenType? {
        return if (OAuth2AccessToken.TokenType.BEARER.value == entity.metadata["access_token_type"]) {
            OAuth2AccessToken.TokenType.BEARER
        } else {
            null
        }
    }
}