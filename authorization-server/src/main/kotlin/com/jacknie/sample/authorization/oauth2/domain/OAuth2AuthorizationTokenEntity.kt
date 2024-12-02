package com.jacknie.sample.authorization.oauth2.domain

import com.jacknie.sample.authorization.oauth2.application.authorization.OAuth2Authorization
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(
    name = "oauth2_authorization_token",
    indexes = [
        Index(columnList = "authorizationId"),
        Index(columnList = "tokenValue"),
        Index(columnList = "tokenValue, type"),
    ]
)
@IdClass(OAuth2AuthorizationTokenId::class)
data class OAuth2AuthorizationTokenEntity(

    @Id
    val authorizationId: String,

    @Id
    val type: String,

    @Column(name = "tokenValue", length = 1024 * 4)
    val value: String,

    val issuedAt: Instant?,

    val expiredAt: Instant?,

    @Convert(converter = OAuth2AuthorizationAttributesConverter::class)
    @Column(columnDefinition = "text")
    val metadata: MutableMap<String, Any>,

    @ManyToOne(optional = false)
    @JoinColumn(name = "authorizationId", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    val authorization: OAuth2AuthorizationEntity,
) {
    companion object {
        fun from(token: OAuth2Authorization.Token, authorization: OAuth2AuthorizationEntity) = token.run {
            OAuth2AuthorizationTokenEntity(authorization.id, type, value, issuedAt, expiredAt, metadata, authorization)
        }
    }
}
