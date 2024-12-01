package com.jacknie.sample.authorization.oauth2.application.oidc.impl

import com.jacknie.sample.authorization.oauth2.application.oidc.OidcStandardClaimSet
import org.springframework.security.oauth2.core.user.OAuth2User
import java.time.Instant
import java.util.*

class OktaClaimSet(private val user: OAuth2User): OidcStandardClaimSet {
    override val emailVerified: Boolean? get() = user.attributes["email_verified"] as Boolean?
    override val givenName: String? get() = user.attributes["given_name"] as String?
    override val picture: String? get() = user.attributes["picture"] as String?
    override val updatedAt: Date? get() = user.attributes["updated_at"]?.let { Date.from(it as Instant) }
    override val nickname: String? get() = user.attributes["nickname"] as String?
    override val name: String? get() = user.attributes["name"] as String?
    override val familyName: String? get() = user.attributes["family_name"] as String?
    override val email: String? get() = user.attributes["email"] as String?
}