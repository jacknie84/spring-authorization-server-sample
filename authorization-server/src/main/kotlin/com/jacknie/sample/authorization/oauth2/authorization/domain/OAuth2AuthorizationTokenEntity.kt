package com.jacknie.sample.authorization.oauth2.authorization.domain

import jakarta.persistence.*
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import java.time.Instant

@Entity
@Table(name = "oauth2_authorization_token", indexes = [Index(columnList = "authorizationId")])
@IdClass(OAuth2AuthorizationTokenId::class)
data class OAuth2AuthorizationTokenEntity(

    @Id
    val authorizationId: String,

    @Id
    val type: OAuth2TokenType,

    @Column(name = "tokenValue", length = 1024 * 4, unique = true)
    val value: String,

    val issuedAt: Instant?,

    val expiredAt: Instant?,

    @Convert(converter = OAuth2AuthorizationAttributesConverter::class)
    @Column(columnDefinition = "text")
    val metadata: MutableMap<String, Any>,

    @ManyToOne(optional = false)
    @JoinColumn(name = "authorizationId", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    val authorization: OAuth2AuthorizationEntity,
)
