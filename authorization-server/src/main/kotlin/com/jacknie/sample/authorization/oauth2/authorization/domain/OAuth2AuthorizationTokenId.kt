package com.jacknie.sample.authorization.oauth2.authorization.domain

import jakarta.persistence.Column
import jakarta.persistence.Convert
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType

data class OAuth2AuthorizationTokenId(

    var authorizationId: String? = null,

    @Column(length = 255, columnDefinition = "varchar")
    @Convert(converter = OAuth2TokenTypeConverter::class)
    var type: OAuth2TokenType? = null,
)
