package com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.token.impl

import com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.token.OAuth2TokenMetadataHandler
import com.jacknie.sample.authorization.oauth2.application.authorization.OAuth2Authorization
import org.springframework.util.StringUtils

class OAuth2AccessTokenScopesHandler: OAuth2TokenMetadataHandler<Set<String>> {
    override fun setOAuth2AuthorizationTokenEntityMetadata(
        token: OAuth2Authorization.Token,
        value: Set<String>
    ) {
        token.metadata["access_token_scopes"] = StringUtils.collectionToCommaDelimitedString(value)
    }

    override fun getOAuth2TokenFieldValue(token: OAuth2Authorization.Token): Set<String> {
        return token.metadata["access_token_scopes"]
            ?.let { StringUtils.commaDelimitedListToSet(it.toString()) }
            ?: emptySet()
    }
}