package com.jacknie.sample.authorization

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.oidc.StandardClaimNames
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer
import java.time.Instant
import java.time.ZoneId
import java.util.*

class JwtEncodingContextCustomizer: OAuth2TokenCustomizer<JwtEncodingContext> {

    override fun customize(context: JwtEncodingContext) {
        if (OidcParameterNames.ID_TOKEN == context.tokenType.value) {
            val token = context.getPrincipal<OAuth2AuthenticationToken>()
            val userInfo = getUserInfoStandardClaimSet(token)
            configure(context.claims, userInfo)
        }
    }

    private fun getUserInfoStandardClaimSet(token: OAuth2AuthenticationToken): UserInfoStandardClaimSet {
        return when (token.authorizedClientRegistrationId) {
            "github" -> GithubUserInfo(token.principal)
            "naver" -> NaverUserInfo(token.principal)
            "okta" -> OktaUserInfo(token.principal)
            "kakao" -> KakaoUserInfo(token.principal)
            "google" -> GoogleUserInfo(token.principal)
            else -> NoopUserInfo()
        }
    }

    private fun configure(builder: JwtClaimsSet.Builder, userInfo: UserInfoStandardClaimSet) {
        with(userInfo) {
            email?.also { builder.claim(StandardClaimNames.EMAIL, it) }
            emailVerified?.also { builder.claim(StandardClaimNames.EMAIL_VERIFIED, it) }
            phoneNumber?.also { builder.claim(StandardClaimNames.PHONE_NUMBER, it) }
            phoneNumberVerified?.also { builder.claim(StandardClaimNames.PHONE_NUMBER_VERIFIED, it) }
            name?.also { builder.claim(StandardClaimNames.NAME, it) }
            familyName?.also { builder.claim(StandardClaimNames.FAMILY_NAME, it) }
            givenName?.also { builder.claim(StandardClaimNames.GIVEN_NAME, it) }
            middleName?.also { builder.claim(StandardClaimNames.MIDDLE_NAME, it) }
            nickname?.also { builder.claim(StandardClaimNames.NICKNAME, it) }
            preferredUsername?.also { builder.claim(StandardClaimNames.PREFERRED_USERNAME, it) }
            profile?.also { builder.claim(StandardClaimNames.PROFILE, it) }
            picture?.also { builder.claim(StandardClaimNames.PICTURE, it) }
            website?.also { builder.claim(StandardClaimNames.WEBSITE, it) }
            gender?.also { builder.claim(StandardClaimNames.GENDER, it) }
            birthdate?.also { builder.claim(StandardClaimNames.BIRTHDATE, it) }
            zoneInfo?.also { builder.claim(StandardClaimNames.ZONEINFO, it) }
            locale?.also { builder.claim(StandardClaimNames.LOCALE, it) }
            updatedAt?.also { builder.claim(StandardClaimNames.UPDATED_AT, it) }
        }
    }

    private interface UserInfoStandardClaimSet {
        val email: String? get() = null
        val emailVerified: Boolean? get() = null
        val phoneNumber: String? get() = null
        val phoneNumberVerified: Boolean? get() = null
        val name: String? get() = null
        val familyName: String? get() = null
        val givenName: String? get() = null
        val middleName: String? get() = null
        val nickname: String? get() = null
        val preferredUsername: String? get() = null
        val profile: String? get() = null
        val picture: String? get() = null
        val website: String? get() = null
        val gender: String? get() = null
        val birthdate: Date? get() = null
        val zoneInfo: ZoneId? get() = null
        val locale: Locale? get() = null
        val updatedAt: Date? get() = null
    }

    private class GithubUserInfo(private val user: OAuth2User): UserInfoStandardClaimSet {
        override val email: String? get() = user.attributes["email"] as String?
        override val picture: String? get() = user.attributes["avatar_url"] as String?
        override val profile: String? get() = user.attributes["html_url"] as String?
        override val name: String? get() = user.attributes["name"] as String?
        override val updatedAt: Date? get() = user.attributes["updated_at"]?.let { Date.from(Instant.parse(it as String)) }
    }

    private class NaverUserInfo(private val user: OAuth2User): UserInfoStandardClaimSet {
        override val picture: String? get() = user.attributes["profile_image"] as String?
        override val phoneNumber: String? get() = user.attributes["mobile_e164"] as String?
        override val name: String? get() = user.attributes["name"] as String?
        override val email: String? get() = user.attributes["email"] as String?
    }

    private class OktaUserInfo(private val user: OAuth2User): UserInfoStandardClaimSet {
        override val emailVerified: Boolean? get() = user.attributes["email_verified"] as Boolean?
        override val givenName: String? get() = user.attributes["given_name"] as String?
        override val picture: String? get() = user.attributes["picture"] as String?
        override val updatedAt: Date? get() = user.attributes["updated_at"]?.let { Date.from(it as Instant) }
        override val nickname: String? get() = user.attributes["nickname"] as String?
        override val name: String? get() = user.attributes["name"] as String?
        override val familyName: String? get() = user.attributes["family_name"] as String?
        override val email: String? get() = user.attributes["email"] as String?
    }

    private class KakaoUserInfo(private val user: OAuth2User): UserInfoStandardClaimSet {
        override val nickname: String? get() = user.attributes["nickname"] as String?
        override val picture: String? get() = user.attributes["picture"] as String?
    }

    private class GoogleUserInfo(private val user: OAuth2User): UserInfoStandardClaimSet {
        override val emailVerified: Boolean? get() = user.attributes["email_verified"] as Boolean?
        override val givenName: String? get() = user.attributes["given_name"] as String?
        override val picture: String? get() = user.attributes["picture"] as String?
        override val name: String? get() = user.attributes["name"] as String?
        override val familyName: String? get() = user.attributes["family_name"] as String?
        override val email: String? get() = user.attributes["email"] as String?
    }

    private class NoopUserInfo: UserInfoStandardClaimSet
}