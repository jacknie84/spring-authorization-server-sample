package com.jacknie.sample.authorization.oauth2.application.oidc

import org.springframework.security.oauth2.core.oidc.StandardClaimNames
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import java.time.ZoneId
import java.util.*

interface OidcStandardClaimSet {
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
    fun configure(builder: JwtClaimsSet.Builder) {
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