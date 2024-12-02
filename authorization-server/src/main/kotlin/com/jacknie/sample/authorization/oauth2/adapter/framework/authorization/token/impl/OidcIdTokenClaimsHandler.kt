package com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.token.impl

import com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.FrameworkOAuth2AuthorizationToken
import com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.token.OAuth2TokenMetadataHandler
import com.jacknie.sample.authorization.oauth2.application.authorization.OAuth2Authorization

class OidcIdTokenClaimsHandler: OAuth2TokenMetadataHandler<Map<String, Any>> {
    override fun setOAuth2AuthorizationTokenEntityMetadata(
        token: OAuth2Authorization.Token,
        value: Map<String, Any>
    ) {
        token.metadata[FrameworkOAuth2AuthorizationToken.CLAIMS_METADATA_NAME] = value
    }

    override fun getOAuth2TokenFieldValue(token: OAuth2Authorization.Token): Map<String, Any> {
        return token.metadata[FrameworkOAuth2AuthorizationToken.CLAIMS_METADATA_NAME]
            ?.let { it as Map<*, *> }
            ?.filter { (k, v) -> k != null && v != null }?.map { (k, v) -> k.toString() to v!! }
            ?.toMap()
            ?: emptyMap()
    }
}