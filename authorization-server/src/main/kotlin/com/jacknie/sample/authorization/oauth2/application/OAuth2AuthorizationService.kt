package com.jacknie.sample.authorization.oauth2.application

import com.jacknie.sample.authorization.oauth2.adapter.persistence.OAuth2AuthorizationRepository
import com.jacknie.sample.authorization.oauth2.adapter.persistence.OAuth2AuthorizationTokenRepository
import com.jacknie.sample.authorization.oauth2.application.authorization.OAuth2Authorization
import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationEntity
import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationTokenEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OAuth2AuthorizationService(
    private val authorizationRepository: OAuth2AuthorizationRepository,
    private val tokenRepository: OAuth2AuthorizationTokenRepository,
) {

    @Transactional
    fun saveOAuth2Authorization(authorization: OAuth2Authorization) {
        val authorizationEntity = authorizationRepository.save(OAuth2AuthorizationEntity.from(authorization))
        val tokenEntities = authorization.tokens.map { OAuth2AuthorizationTokenEntity.from(it, authorizationEntity) }
        tokenRepository.saveAll(tokenEntities)
    }

    @Transactional
    fun deleteOAuth2Authorization(identifier: OAuth2Authorization.Identifier) {
        tokenRepository.deleteAllById(identifier.tokenIds)
        authorizationRepository.deleteById(identifier.authorizationId)
    }

    @Transactional(readOnly = true)
    fun getOAuth2Authorization(identifier: OAuth2Authorization.Identifier): OAuth2Authorization? {
        return authorizationRepository.findById(identifier.authorizationId)
            .map {
                val tokenEntities = tokenRepository.findAllById(identifier.tokenIds)
                OAuth2Authorization.from(it, tokenEntities)
            }
            .orElse(null)
    }

    @Transactional(readOnly = true)
    fun getOAuth2Authorization(token: String, tokenType: String?): OAuth2Authorization? {
        return (tokenType
            ?.let { tokenRepository.findByValueAndType(token, it) }
            ?: tokenRepository.findByValue(token))
            .map { it.authorization to tokenRepository.findAllByAuthorizationId(it.authorizationId) }
            .map { OAuth2Authorization.from(it.first, it.second) }
            .orElse(null)
    }
}