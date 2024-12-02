package com.jacknie.sample.authorization.oauth2.application

import org.springframework.core.convert.converter.Converter
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.stereotype.Service

@Service
class OAuth2UserInfoService {

    fun getUserAttributesConverter(source: OAuth2UserRequest): Converter<Map<String, Any>, Map<String, Any>> {
        return if (source.clientRegistration.registrationId == "naver") {
            Converter {
                val response = it["response"] as Map<*, *>
                response
                    .filter { (k, v) -> k != null && v != null }
                    .map { (k, v) -> k.toString() to v!! }
                    .toMap()
            }
        } else {
            Converter { it }
        }
    }
}