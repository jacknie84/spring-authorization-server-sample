package com.jacknie.sample.authorization.oauth2.application.token.impl

import com.jacknie.sample.authorization.oauth2.application.token.OAuth2TokenMetadataHandler
import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationTokenEntity
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization

class OidcIdTokenClaimsHandler: OAuth2TokenMetadataHandler<Map<String, Any>> {
    override fun setOAuth2AuthorizationTokenEntityMetadata(
        entity: OAuth2AuthorizationTokenEntity,
        value: Map<String, Any>
    ) {
        entity.metadata[OAuth2Authorization.Token.CLAIMS_METADATA_NAME] = value
    }

    override fun getOAuth2TokenFieldValue(entity: OAuth2AuthorizationTokenEntity): Map<String, Any> {
        return entity.metadata[OAuth2Authorization.Token.CLAIMS_METADATA_NAME]
            ?.let { it as Map<*, *> }
            ?.filter { (k, v) -> k != null && v != null }?.map { (k, v) -> k.toString() to v!! }
            ?.toMap()
            ?: emptyMap()
    }
}