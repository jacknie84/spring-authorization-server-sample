package com.jacknie.sample.authorization.oauth2.application.token.impl

import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationTokenEntity
import org.springframework.security.oauth2.core.OAuth2UserCode
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType

class OAuth2UserCodeConverter: AbstractOAuth2TokenConverter<OAuth2UserCode>() {
    override val type: OAuth2TokenType get() = OAuth2TokenType(OAuth2ParameterNames.USER_CODE)
    override val javaClass: Class<OAuth2UserCode> get() = OAuth2UserCode::class.java

    override fun convertToOAuth2Token(entity: OAuth2AuthorizationTokenEntity) = entity.run {
        OAuth2UserCode(value, issuedAt, expiredAt)
    }
}