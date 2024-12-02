package com.jacknie.sample.authorization.oauth2.adapter.framework

import com.jacknie.sample.authorization.oauth2.application.OAuth2OidcService
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer
import org.springframework.stereotype.Component

@Component
class JwtEncodingContextCustomizer(private val oidcService: OAuth2OidcService): OAuth2TokenCustomizer<JwtEncodingContext> {

    override fun customize(context: JwtEncodingContext) {
        if (OidcParameterNames.ID_TOKEN == context.tokenType.value) {
            val token = context.getPrincipal<OAuth2AuthenticationToken>()
            val claimSet = oidcService.getOidcStandardClaimSet(token)
            claimSet.configure(context.claims)
        }
    }
}