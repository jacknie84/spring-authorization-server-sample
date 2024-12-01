package com.jacknie.sample.authorization.oauth2.adapter.framework

import com.jacknie.sample.authorization.oauth2.application.OAuth2Service
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.stereotype.Component

@Component
class OAuth2AuthorizationServiceImpl(private val oauth2Service: OAuth2Service): OAuth2AuthorizationService {

    override fun save(authorization: OAuth2Authorization) {
        oauth2Service.saveOAuth2Authorization(authorization)
    }

    override fun remove(authorization: OAuth2Authorization) {
        oauth2Service.deleteOAuth2Authorization(authorization)
    }

    override fun findById(id: String): OAuth2Authorization? {
        return oauth2Service.getOAuth2Authorization(id)
    }

    override fun findByToken(token: String, tokenType: OAuth2TokenType?): OAuth2Authorization? {
        return oauth2Service.getOAuth2Authorization(token, tokenType)
    }
}