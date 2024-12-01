package com.jacknie.sample.authorization.oauth2.application

import com.jacknie.sample.authorization.oauth2.adapter.persistence.OAuth2AuthorizationRepository
import com.jacknie.sample.authorization.oauth2.adapter.persistence.OAuth2AuthorizationTokenRepository
import com.jacknie.sample.authorization.oauth2.application.token.impl.*
import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationEntity
import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationTokenEntity
import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationTokenId
import org.springframework.dao.DataRetrievalFailureException
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.stereotype.Service

@Service
class OAuth2Service(
    private val authorizationRepository: OAuth2AuthorizationRepository,
    private val tokenRepository: OAuth2AuthorizationTokenRepository,
    private val registeredClientRepository: RegisteredClientRepository,
) {

    private val tokenHandlers = listOf(
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
    private val tokenHandlerByTokenType = tokenHandlers.associateBy { it.type }

    fun saveOAuth2Authorization(authorization: OAuth2Authorization) {
        var authorizationEntity = OAuth2AuthorizationEntity.from(authorization)
        authorizationEntity = authorizationRepository.save(authorizationEntity)
        val tokenEntities = tokenHandlers
            .mapNotNull { it.getOAuth2AuthorizationTokenEntity(authorization, authorizationEntity) }
        tokenRepository.saveAll(tokenEntities)
    }

    fun deleteOAuth2Authorization(authorization: OAuth2Authorization) {
        val tokenIds = tokenHandlers.map { OAuth2AuthorizationTokenId(authorization.id, it.type) }
        tokenRepository.deleteAllById(tokenIds)
        authorizationRepository.deleteById(authorization.id)
    }

    fun getOAuth2Authorization(id: String): OAuth2Authorization? {
        return authorizationRepository.findById(id)
            .map {
                val tokenIds = tokenHandlers.map { h -> OAuth2AuthorizationTokenId(id, h.type) }
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
            tokenHandlerByTokenType[tokenEntity.type]?.setOAuth2AuthorizationTokenEntity(builder, tokenEntity)
        }
        return builder.build()
    }
}