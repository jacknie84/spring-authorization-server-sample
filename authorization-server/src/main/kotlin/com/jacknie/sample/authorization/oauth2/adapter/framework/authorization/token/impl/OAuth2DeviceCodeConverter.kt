package com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.token.impl

import com.jacknie.sample.authorization.oauth2.application.authorization.OAuth2Authorization
import org.springframework.security.oauth2.core.OAuth2DeviceCode
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType

class OAuth2DeviceCodeConverter: AbstractOAuth2TokenConverter<OAuth2DeviceCode>() {
    override val type: OAuth2TokenType get() = OAuth2TokenType(OAuth2ParameterNames.DEVICE_CODE)
    override val javaClass: Class<OAuth2DeviceCode> get() = OAuth2DeviceCode::class.java

    override fun convertToOAuth2Token(token: OAuth2Authorization.Token) = token.run {
        OAuth2DeviceCode(value, issuedAt, expiredAt)
    }
}