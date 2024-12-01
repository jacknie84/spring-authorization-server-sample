package com.jacknie.sample.authorization.oauth2.application.token.impl

import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationTokenEntity
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType

class OidcIdTokenConverter: AbstractOAuth2TokenConverter<OidcIdToken>() {
    private val idTokenClaimsHandler = OidcIdTokenClaimsHandler()

    override val type: OAuth2TokenType get() = OAuth2TokenType(OidcParameterNames.ID_TOKEN)
    override val javaClass: Class<OidcIdToken> get() = OidcIdToken::class.java

    override fun convertToOAuth2Token(entity: OAuth2AuthorizationTokenEntity) = entity.run {
        val claims = idTokenClaimsHandler.getOAuth2TokenFieldValue(this)
        OidcIdToken(value, issuedAt, expiredAt, claims)
    }
}