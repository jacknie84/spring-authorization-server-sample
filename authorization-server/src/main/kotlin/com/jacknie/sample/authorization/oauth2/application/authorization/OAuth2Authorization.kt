package com.jacknie.sample.authorization.oauth2.application.authorization

import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationEntity
import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationTokenEntity
import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationTokenId
import java.time.Instant

data class OAuth2Authorization(
    val id: String,
    val registeredClientId: String,
    val principalName: String,
    val authorizationGrantType: String,
    val authorizedScopes: MutableSet<String>,
    val attributes: MutableMap<String, Any>,
    val tokens: List<Token>,
) {
    data class Identifier(
        val authorizationId: String,
        val tokenIds: List<OAuth2AuthorizationTokenId>
    )
    data class Token(
        val type: String,
        val value: String,
        val issuedAt: Instant?,
        val expiredAt: Instant?,
        val metadata: MutableMap<String, Any>,
    ) {
        companion object {
            fun from(entity: OAuth2AuthorizationTokenEntity) = entity.run {
                Token(type, value, issuedAt, expiredAt, metadata)
            }
        }
    }

    companion object {
        fun from(authorizationEntity: OAuth2AuthorizationEntity, tokenEntities: List<OAuth2AuthorizationTokenEntity>): OAuth2Authorization  {
            val tokens = tokenEntities.map { Token.from(it) }
            return authorizationEntity.run {
                OAuth2Authorization(
                    id, registeredClientId, principalName, authorizationGrantType, authorizedScopes, attributes, tokens
                )
            }
        }
    }
}
