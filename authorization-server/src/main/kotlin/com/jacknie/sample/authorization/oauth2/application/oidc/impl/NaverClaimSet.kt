package com.jacknie.sample.authorization.oauth2.application.oidc.impl

import com.jacknie.sample.authorization.oauth2.application.oidc.OidcStandardClaimSet
import org.springframework.security.oauth2.core.user.OAuth2User

class NaverClaimSet(private val user: OAuth2User): OidcStandardClaimSet {
    override val picture: String? get() = user.attributes["profile_image"] as String?
    override val phoneNumber: String? get() = user.attributes["mobile_e164"] as String?
    override val name: String? get() = user.attributes["name"] as String?
    override val email: String? get() = user.attributes["email"] as String?
}