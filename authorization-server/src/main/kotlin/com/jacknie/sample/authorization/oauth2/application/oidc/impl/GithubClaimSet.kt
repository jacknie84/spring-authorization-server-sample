package com.jacknie.sample.authorization.oauth2.application.oidc.impl

import com.jacknie.sample.authorization.oauth2.application.oidc.OidcStandardClaimSet
import org.springframework.security.oauth2.core.user.OAuth2User
import java.time.Instant
import java.util.*

class GithubClaimSet(private val user: OAuth2User): OidcStandardClaimSet {
    override val email: String? get() = user.attributes["email"] as String?
    override val picture: String? get() = user.attributes["avatar_url"] as String?
    override val profile: String? get() = user.attributes["html_url"] as String?
    override val name: String? get() = user.attributes["name"] as String?
    override val updatedAt: Date? get() = user.attributes["updated_at"]?.let { Date.from(Instant.parse(it as String)) }
}