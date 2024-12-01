package com.jacknie.sample.authorization.oauth2.authorization.domain

import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization

@Entity
@Table(name = "oauth2_authorization")
data class OAuth2AuthorizationEntity(

    @Id
    val id: String,

    @Column(nullable = false)
    val registeredClientId: String,

    @Column(nullable = false)
    val principalName: String,

    @Convert(converter = AuthorizationGrantTypeConverter::class)
    @Column(nullable = false)
    val authorizationGrantType: AuthorizationGrantType,

    @Convert(converter = StringMutableSetConverter::class)
    @Column(length = 1000)
    val authorizedScopes: MutableSet<String>,

    @Convert(converter = OAuth2AuthorizationAttributesConverter::class)
    @Column(columnDefinition = "text")
    val attributes: MutableMap<String, Any>,
) {
    companion object {
        fun from(authorization: OAuth2Authorization): OAuth2AuthorizationEntity {
            return OAuth2AuthorizationEntity(
                id = authorization.id,
                registeredClientId = authorization.registeredClientId,
                principalName = authorization.principalName,
                authorizationGrantType = authorization.authorizationGrantType,
                authorizedScopes = authorization.authorizedScopes,
                attributes = authorization.attributes
            )
        }
    }
}
