package com.jacknie.sample.authorization.oauth2.adapter.framework

import com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.FrameworkOAuth2Authorization
import com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.FrameworkOAuth2AuthorizationService
import com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.token.impl.*
import com.jacknie.sample.authorization.oauth2.application.OAuth2AuthorizationService
import com.jacknie.sample.authorization.oauth2.application.authorization.OAuth2Authorization
import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationTokenId
import org.springframework.dao.DataRetrievalFailureException
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.stereotype.Component

@Component
class OAuth2AuthorizationServiceImpl(
    private val authorizationService: OAuth2AuthorizationService,
    private val registeredClientRepository: RegisteredClientRepository,
): FrameworkOAuth2AuthorizationService {

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

    override fun save(authorization: FrameworkOAuth2Authorization) {
        val localAuthorization = authorization.run {
            val tokens = tokenHandlers.mapNotNull { it.getOAuth2AuthorizationToken(authorization) }
            OAuth2Authorization(
                id, registeredClientId, principalName, authorizationGrantType.value, authorizedScopes, attributes, tokens
            )
        }
        authorizationService.saveOAuth2Authorization(localAuthorization)
    }

    override fun remove(authorization: FrameworkOAuth2Authorization) {
        val tokenIds = tokenHandlers.map { OAuth2AuthorizationTokenId(authorization.id, it.type.value) }
        val identifier = OAuth2Authorization.Identifier(authorization.id, tokenIds)
        authorizationService.deleteOAuth2Authorization(identifier)
    }

    override fun findById(id: String): FrameworkOAuth2Authorization? {
        val tokenIds = tokenHandlers.map { OAuth2AuthorizationTokenId(id, it.type.value) }
        val identifier = OAuth2Authorization.Identifier(id, tokenIds)
        val authorization = authorizationService.getOAuth2Authorization(identifier)
        return authorization?.let { buildOAuth2Authorization(it) }
    }

    override fun findByToken(token: String, tokenType: OAuth2TokenType?): FrameworkOAuth2Authorization? {
        val authorization = authorizationService.getOAuth2Authorization(token, tokenType?.value)
        return authorization?.let { buildOAuth2Authorization(it) }
    }

    private fun buildOAuth2Authorization(authorization: OAuth2Authorization): FrameworkOAuth2Authorization {
        val registeredClient = registeredClientRepository.findById(authorization.registeredClientId)
            ?: throw DataRetrievalFailureException("The RegisteredClient with id '${authorization.registeredClientId}' was not found in the RegisteredClientRepository.")
        val builder = FrameworkOAuth2Authorization.withRegisteredClient(registeredClient)
        with(authorization) {
            builder
                .id(id)
                .principalName(principalName)
                .authorizationGrantType(AuthorizationGrantType(authorizationGrantType))
                .authorizedScopes(authorizedScopes)
                .attributes { it += attributes }
        }
        for (token in authorization.tokens) {
            val tokenType = OAuth2TokenType(token.type)
            tokenHandlerByTokenType[tokenType]?.setOAuth2AuthorizationToken(builder, token)
        }
        return builder.build()
    }
}