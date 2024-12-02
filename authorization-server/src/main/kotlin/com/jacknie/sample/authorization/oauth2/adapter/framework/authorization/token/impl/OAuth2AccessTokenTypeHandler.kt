package com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.token.impl

import com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.token.OAuth2TokenMetadataHandler
import com.jacknie.sample.authorization.oauth2.application.authorization.OAuth2Authorization
import org.springframework.security.oauth2.core.OAuth2AccessToken

class OAuth2AccessTokenTypeHandler: OAuth2TokenMetadataHandler<OAuth2AccessToken.TokenType?> {
    override fun setOAuth2AuthorizationTokenEntityMetadata(
        token: OAuth2Authorization.Token,
        value: OAuth2AccessToken.TokenType?
    ) {
        value?.also { token.metadata["access_token_type"] = it.value }
    }

    override fun getOAuth2TokenFieldValue(token: OAuth2Authorization.Token): OAuth2AccessToken.TokenType? {
        return if (OAuth2AccessToken.TokenType.BEARER.value == token.metadata["access_token_type"]) {
            OAuth2AccessToken.TokenType.BEARER
        } else {
            null
        }
    }
}