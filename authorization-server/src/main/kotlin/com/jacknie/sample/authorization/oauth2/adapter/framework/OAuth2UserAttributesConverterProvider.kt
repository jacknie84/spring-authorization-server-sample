package com.jacknie.sample.authorization.oauth2.adapter.framework

import com.jacknie.sample.authorization.oauth2.application.OAuth2Service
import org.springframework.core.convert.converter.Converter
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.stereotype.Component

@Component
class OAuth2UserAttributesConverterProvider(
    private val oauth2Service: OAuth2Service
) {

    fun getUserAttributesConverter(source: OAuth2UserRequest): Converter<Map<String, Any>, Map<String, Any>> {
        return oauth2Service.getUserAttributesConverter(source)
    }
}