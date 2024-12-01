package com.jacknie.sample.authorization.oauth2.application.token.impl

import com.jacknie.sample.authorization.oauth2.application.token.OAuth2TokenMetadataHandler
import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationTokenEntity
import org.springframework.util.StringUtils

class OAuth2AccessTokenScopesHandler: OAuth2TokenMetadataHandler<Set<String>> {
    override fun setOAuth2AuthorizationTokenEntityMetadata(
        entity: OAuth2AuthorizationTokenEntity,
        value: Set<String>
    ) {
        entity.metadata["access_token_scopes"] = StringUtils.collectionToCommaDelimitedString(value)
    }

    override fun getOAuth2TokenFieldValue(entity: OAuth2AuthorizationTokenEntity): Set<String> {
        return entity.metadata["access_token_scopes"]
            ?.let { StringUtils.commaDelimitedListToSet(it.toString()) }
            ?: emptySet()
    }
}