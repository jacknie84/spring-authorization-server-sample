package com.jacknie.sample.authorization.oauth2.application

import com.jacknie.sample.authorization.oauth2.application.oidc.OidcStandardClaimSet
import com.jacknie.sample.authorization.oauth2.application.oidc.impl.*
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Service

@Service
class OAuth2OidcService {

    fun getOidcStandardClaimSet(token: OAuth2AuthenticationToken): OidcStandardClaimSet {
        return when (token.authorizedClientRegistrationId) {
            "github" -> GithubClaimSet(token.principal)
            "naver" -> NaverClaimSet(token.principal)
            "okta" -> OktaClaimSet(token.principal)
            "kakao" -> KakaoClaimSet(token.principal)
            "google" -> GoogleClaimSet(token.principal)
            else -> NoopClaimSet()
        }
    }
}