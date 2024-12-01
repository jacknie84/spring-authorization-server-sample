package com.jacknie.sample.authorization.oauth2.application.oidc.impl

import com.jacknie.sample.authorization.oauth2.application.oidc.OidcStandardClaimSet
import org.springframework.security.oauth2.core.user.OAuth2User

class KakaoClaimSet(private val user: OAuth2User): OidcStandardClaimSet {
    override val nickname: String? get() = user.attributes["nickname"] as String?
    override val picture: String? get() = user.attributes["picture"] as String?
}