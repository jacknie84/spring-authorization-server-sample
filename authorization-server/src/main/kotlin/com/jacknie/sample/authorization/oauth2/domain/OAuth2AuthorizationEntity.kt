package com.jacknie.sample.authorization.oauth2.domain

import com.jacknie.sample.authorization.oauth2.application.authorization.OAuth2Authorization
import jakarta.persistence.*

@Entity
@Table(name = "oauth2_authorization")
data class OAuth2AuthorizationEntity(

    @Id
    val id: String,

    @Column(nullable = false)
    val registeredClientId: String,

    @Column(nullable = false)
    val principalName: String,

    @Column(nullable = false)
    val authorizationGrantType: String,

    @Convert(converter = StringMutableSetConverter::class)
    @Column(length = 1000)
    val authorizedScopes: MutableSet<String>,

    @Convert(converter = OAuth2AuthorizationAttributesConverter::class)
    @Column(columnDefinition = "text")
    val attributes: MutableMap<String, Any>,
) {
    companion object {
        fun from(authorization: OAuth2Authorization) = authorization.run {
            OAuth2AuthorizationEntity(
                id, registeredClientId, principalName, authorizationGrantType, authorizedScopes, attributes
            )
        }
    }
}
