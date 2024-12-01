package com.jacknie.sample.authorization.oauth2.application

import com.jacknie.sample.authorization.oauth2.adapter.persistence.OAuth2AuthorizationRepository
import com.jacknie.sample.authorization.oauth2.adapter.persistence.OAuth2AuthorizationTokenRepository
import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationEntity
import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationTokenEntity
import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationTokenId
import org.springframework.dao.DataRetrievalFailureException
import org.springframework.security.oauth2.core.*
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils

@Service
class OAuth2Service(
    private val authorizationRepository: OAuth2AuthorizationRepository,
    private val tokenRepository: OAuth2AuthorizationTokenRepository,
    private val registeredClientRepository: RegisteredClientRepository,
) {

    fun saveOAuth2Authorization(authorization: OAuth2Authorization) {
        var authorizationEntity = OAuth2AuthorizationEntity.from(authorization)
        authorizationEntity = authorizationRepository.save(authorizationEntity)
        val tokenEntities = TOKEN_HANDLERS
            .mapNotNull { it.getOAuth2AuthorizationTokenEntity(authorization, authorizationEntity) }
        tokenRepository.saveAll(tokenEntities)
    }

    fun deleteOAuth2Authorization(authorization: OAuth2Authorization) {
        val tokenIds = TOKEN_HANDLERS.map { OAuth2AuthorizationTokenId(authorization.id, it.type) }
        tokenRepository.deleteAllById(tokenIds)
        authorizationRepository.deleteById(authorization.id)
    }

    fun getOAuth2Authorization(id: String): OAuth2Authorization? {
        return authorizationRepository.findById(id)
            .map {
                val tokenIds = TOKEN_HANDLERS.map { h -> OAuth2AuthorizationTokenId(id, h.type) }
                val tokenEntities = tokenRepository.findAllById(tokenIds)
                buildOAuth2Authorization(it, tokenEntities)
            }
            .orElse(null)
    }

    fun getOAuth2Authorization(token: String, tokenType: OAuth2TokenType?): OAuth2Authorization? {
        return (tokenType
            ?.let { tokenRepository.findByValueAndType(token, it) }
            ?: tokenRepository.findByValue(token))
            .map { it.authorization to tokenRepository.findAllByAuthorizationId(it.authorizationId) }
            .map { buildOAuth2Authorization(it.first, it.second) }
            .orElse(null)
    }

    private fun buildOAuth2Authorization(authorizationEntity: OAuth2AuthorizationEntity, tokenEntities: List<OAuth2AuthorizationTokenEntity>): OAuth2Authorization {
        val registeredClient = registeredClientRepository.findById(authorizationEntity.registeredClientId)
            ?: throw DataRetrievalFailureException("The RegisteredClient with id '${authorizationEntity.registeredClientId}' was not found in the RegisteredClientRepository.")
        val builder = OAuth2Authorization.withRegisteredClient(registeredClient)
        with(authorizationEntity) {
            builder
                .id(id)
                .principalName(principalName)
                .authorizationGrantType(authorizationGrantType)
                .authorizedScopes(authorizedScopes)
                .attributes { it += attributes }
        }
        for (tokenEntity in tokenEntities) {
            TOKEN_HANDLER_MAP_BY_TOKEN_TYPE[tokenEntity.type]?.setOAuth2AuthorizationTokenEntity(builder, tokenEntity)
        }
        return builder.build()
    }

    private interface OAuth2AuthorizationTokenEntityHandler {
        val type: OAuth2TokenType
        fun getOAuth2AuthorizationTokenEntity(
            authorization: OAuth2Authorization,
            authorizationEntity: OAuth2AuthorizationEntity
        ): OAuth2AuthorizationTokenEntity?
        fun setOAuth2AuthorizationTokenEntity(
            builder: OAuth2Authorization.Builder,
            entity: OAuth2AuthorizationTokenEntity
        )
    }

    private interface OAuth2TokenConverter<T, K> {
        val type: OAuth2TokenType
        val javaClass: Class<T>
        fun convertToOAuth2Token(entity: OAuth2AuthorizationTokenEntity): T
        fun convertToOAuth2AuthorizationTokenEntity(
            token: K,
            authorizationEntity: OAuth2AuthorizationEntity
        ): OAuth2AuthorizationTokenEntity
    }

    private interface OAuth2TokenMetadataHandler<T> {
        fun setOAuth2AuthorizationTokenEntityMetadata(entity: OAuth2AuthorizationTokenEntity, value: T)
        fun getOAuth2TokenFieldValue(entity: OAuth2AuthorizationTokenEntity): T
    }

    private class OAuth2StateHandler: OAuth2AuthorizationTokenEntityHandler {
        private val converter = OAuth2StateConverter()

        override val type: OAuth2TokenType get() = converter.type

        override fun getOAuth2AuthorizationTokenEntity(
            authorization: OAuth2Authorization,
            authorizationEntity: OAuth2AuthorizationEntity
        ): OAuth2AuthorizationTokenEntity? {
            return authorization.getAttribute<String>(converter.type.value)
                ?.let { converter.convertToOAuth2AuthorizationTokenEntity(it, authorizationEntity) }
        }

        override fun setOAuth2AuthorizationTokenEntity(
            builder: OAuth2Authorization.Builder,
            entity: OAuth2AuthorizationTokenEntity
        ) {
            val state = converter.convertToOAuth2Token(entity)
            builder.attribute(converter.type.value, state)
        }
    }

    private class OAuth2TokenHandler(
        private val converter: OAuth2TokenConverter<out OAuth2Token, OAuth2Authorization.Token<out OAuth2Token>>
    ): OAuth2AuthorizationTokenEntityHandler {
        override val type: OAuth2TokenType get() = converter.type

        override fun getOAuth2AuthorizationTokenEntity(
            authorization: OAuth2Authorization,
            authorizationEntity: OAuth2AuthorizationEntity
        ): OAuth2AuthorizationTokenEntity? {
            return authorization.getToken(converter.javaClass)
                ?.let { converter.convertToOAuth2AuthorizationTokenEntity(it, authorizationEntity) }
        }

        override fun setOAuth2AuthorizationTokenEntity(
            builder: OAuth2Authorization.Builder,
            entity: OAuth2AuthorizationTokenEntity
        ) {
            val token = converter.convertToOAuth2Token(entity)
            builder.token(token) { it += entity.metadata }
        }
    }

    private abstract class AbstractOAuth2TokenConverter<T>:
        OAuth2TokenConverter<T, OAuth2Authorization.Token<out OAuth2Token>> {
        override fun convertToOAuth2AuthorizationTokenEntity(
            token: OAuth2Authorization.Token<out OAuth2Token>,
            authorizationEntity: OAuth2AuthorizationEntity
        ) = token.run {
            OAuth2AuthorizationTokenEntity(
                authorizationId = authorizationEntity.id,
                type = type,
                value = this.token.tokenValue,
                issuedAt = this.token.issuedAt,
                expiredAt = this.token.expiresAt,
                metadata = this.metadata.toMutableMap(),
                authorization = authorizationEntity,
            )
        }
    }

    private class OAuth2StateConverter: OAuth2TokenConverter<String, String> {
        override val type: OAuth2TokenType get() = OAuth2TokenType(OAuth2ParameterNames.STATE)
        override val javaClass: Class<String> get() = String::class.java

        override fun convertToOAuth2Token(entity: OAuth2AuthorizationTokenEntity): String {
            return entity.value
        }

        override fun convertToOAuth2AuthorizationTokenEntity(
            token: String,
            authorizationEntity: OAuth2AuthorizationEntity
        ) = OAuth2AuthorizationTokenEntity(
            authorizationId = authorizationEntity.id,
            type = type,
            value = token,
            issuedAt = null,
            expiredAt = null,
            metadata = mutableMapOf(),
            authorization = authorizationEntity
        )
    }

    private class OAuth2AuthorizationCodeConverter: AbstractOAuth2TokenConverter<OAuth2AuthorizationCode>() {
        override val type: OAuth2TokenType get() = OAuth2TokenType(OAuth2ParameterNames.CODE)
        override val javaClass: Class<OAuth2AuthorizationCode> get() = OAuth2AuthorizationCode::class.java

        override fun convertToOAuth2Token(entity: OAuth2AuthorizationTokenEntity) = entity.run {
            OAuth2AuthorizationCode(value, issuedAt, expiredAt)
        }
    }

    private class OAuth2AccessTokenConverter: AbstractOAuth2TokenConverter<OAuth2AccessToken>() {
        private val tokenTypeHandler = OAuth2AccessTokenTypeHandler()
        private val tokenScopesHandler = OAuth2AccessTokenScopesHandler()

        override val type: OAuth2TokenType get() = OAuth2TokenType.ACCESS_TOKEN
        override val javaClass: Class<OAuth2AccessToken> get() = OAuth2AccessToken::class.java

        override fun convertToOAuth2Token(entity: OAuth2AuthorizationTokenEntity) = entity.run {
            val tokenType = tokenTypeHandler.getOAuth2TokenFieldValue(this)
            val scopes = tokenScopesHandler.getOAuth2TokenFieldValue(this)
            OAuth2AccessToken(tokenType, value, issuedAt, expiredAt, scopes)
        }

        override fun convertToOAuth2AuthorizationTokenEntity(
            token: OAuth2Authorization.Token<out OAuth2Token>,
            authorizationEntity: OAuth2AuthorizationEntity
        ) = token.run {
            val entity = super.convertToOAuth2AuthorizationTokenEntity(token, authorizationEntity)
            val accessToken = token.token as OAuth2AccessToken
            tokenTypeHandler.setOAuth2AuthorizationTokenEntityMetadata(entity, accessToken.tokenType)
            tokenScopesHandler.setOAuth2AuthorizationTokenEntityMetadata(entity, accessToken.scopes)
            entity
        }
    }

    private class OAuth2AccessTokenTypeHandler: OAuth2TokenMetadataHandler<OAuth2AccessToken.TokenType?> {
        override fun setOAuth2AuthorizationTokenEntityMetadata(
            entity: OAuth2AuthorizationTokenEntity,
            value: OAuth2AccessToken.TokenType?
        ) {
            value?.also { entity.metadata["access_token_type"] = it.value }
        }

        override fun getOAuth2TokenFieldValue(entity: OAuth2AuthorizationTokenEntity): OAuth2AccessToken.TokenType? {
            return if (OAuth2AccessToken.TokenType.BEARER.value == entity.metadata["access_token_type"]) {
                OAuth2AccessToken.TokenType.BEARER
            } else {
                null
            }
        }
    }

    private class OAuth2AccessTokenScopesHandler: OAuth2TokenMetadataHandler<Set<String>> {
        override fun setOAuth2AuthorizationTokenEntityMetadata(
            entity: OAuth2AuthorizationTokenEntity,
            value: Set<String>
        ) {
            entity.metadata["access_token_scopes"] = StringUtils.collectionToCommaDelimitedString(value)
        }

        override fun getOAuth2TokenFieldValue(entity: OAuth2AuthorizationTokenEntity): Set<String> {
            return entity.metadata["access_token_scopes"]
                ?.let { StringUtils.commaDelimitedListToSet(it.toString()) }
                ?: emptySet()
        }
    }

    private class OidcIdTokenConverter: AbstractOAuth2TokenConverter<OidcIdToken>() {
        private val idTokenClaimsHandler = OidcIdTokenClaimsHandler()

        override val type: OAuth2TokenType get() = OAuth2TokenType(OidcParameterNames.ID_TOKEN)
        override val javaClass: Class<OidcIdToken> get() = OidcIdToken::class.java

        override fun convertToOAuth2Token(entity: OAuth2AuthorizationTokenEntity) = entity.run {
            val claims = idTokenClaimsHandler.getOAuth2TokenFieldValue(this)
            OidcIdToken(value, issuedAt, expiredAt, claims)
        }
    }

    private class OidcIdTokenClaimsHandler: OAuth2TokenMetadataHandler<Map<String, Any>> {
        override fun setOAuth2AuthorizationTokenEntityMetadata(
            entity: OAuth2AuthorizationTokenEntity,
            value: Map<String, Any>
        ) {
            entity.metadata[OAuth2Authorization.Token.CLAIMS_METADATA_NAME] = value
        }

        override fun getOAuth2TokenFieldValue(entity: OAuth2AuthorizationTokenEntity): Map<String, Any> {
            return entity.metadata[OAuth2Authorization.Token.CLAIMS_METADATA_NAME]
                ?.let { it as Map<*, *> }
                ?.filter { (k, v) -> k != null && v != null }?.map { (k, v) -> k.toString() to v!! }
                ?.toMap()
                ?: emptyMap()
        }
    }

    private class OAuth2RefreshTokenConverter: AbstractOAuth2TokenConverter<OAuth2RefreshToken>() {
        override val type: OAuth2TokenType get() = OAuth2TokenType.REFRESH_TOKEN
        override val javaClass: Class<OAuth2RefreshToken> get() = OAuth2RefreshToken::class.java

        override fun convertToOAuth2Token(entity: OAuth2AuthorizationTokenEntity) = entity.run {
            OAuth2RefreshToken(value, issuedAt, expiredAt)
        }
    }

    private class OAuth2UserCodeConverter: AbstractOAuth2TokenConverter<OAuth2UserCode>() {
        override val type: OAuth2TokenType get() = OAuth2TokenType(OAuth2ParameterNames.USER_CODE)
        override val javaClass: Class<OAuth2UserCode> get() = OAuth2UserCode::class.java

        override fun convertToOAuth2Token(entity: OAuth2AuthorizationTokenEntity) = entity.run {
            OAuth2UserCode(value, issuedAt, expiredAt)
        }
    }

    private class OAuth2DeviceCodeConverter: AbstractOAuth2TokenConverter<OAuth2DeviceCode>() {
        override val type: OAuth2TokenType get() = OAuth2TokenType(OAuth2ParameterNames.DEVICE_CODE)
        override val javaClass: Class<OAuth2DeviceCode> get() = OAuth2DeviceCode::class.java

        override fun convertToOAuth2Token(entity: OAuth2AuthorizationTokenEntity) = entity.run {
            OAuth2DeviceCode(value, issuedAt, expiredAt)
        }
    }

    companion object {
        private val TOKEN_HANDLERS = listOf(
            OAuth2StateHandler(),
            *listOf(
                OAuth2AuthorizationCodeConverter(),
                OAuth2AccessTokenConverter(),
                OidcIdTokenConverter(),
                OAuth2RefreshTokenConverter(),
                OAuth2UserCodeConverter(),
                OAuth2DeviceCodeConverter(),
            ).map { OAuth2TokenHandler(it) }.toTypedArray(),
        )
        private val TOKEN_HANDLER_MAP_BY_TOKEN_TYPE = TOKEN_HANDLERS.associateBy { it.type }
    }
}