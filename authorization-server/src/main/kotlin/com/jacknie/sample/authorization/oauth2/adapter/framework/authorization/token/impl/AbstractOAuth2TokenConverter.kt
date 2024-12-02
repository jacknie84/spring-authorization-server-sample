package com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.token.impl

import com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.FrameworkOAuth2AuthorizationToken
import com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.token.OAuth2TokenConverter
import com.jacknie.sample.authorization.oauth2.application.authorization.OAuth2Authorization

abstract class AbstractOAuth2TokenConverter<T>:
    OAuth2TokenConverter<T, FrameworkOAuth2AuthorizationToken> {
    override fun convertToOAuth2AuthorizationToken(token: FrameworkOAuth2AuthorizationToken) = token.run {
        OAuth2Authorization.Token(
            type = type.value,
            value = this.token.tokenValue,
            issuedAt = this.token.issuedAt,
            expiredAt = this.token.expiresAt,
            metadata = this.metadata.toMutableMap(),
        )
    }
}