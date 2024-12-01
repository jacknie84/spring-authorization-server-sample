package com.jacknie.sample.authorization.oauth2.domain

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.springframework.security.oauth2.core.AuthorizationGrantType

@Converter
class AuthorizationGrantTypeConverter: AttributeConverter<AuthorizationGrantType, String?> {

    override fun convertToDatabaseColumn(attribute: AuthorizationGrantType): String {
        return attribute.value
    }

    override fun convertToEntityAttribute(dbData: String?): AuthorizationGrantType {
        return AuthorizationGrantType(dbData)
    }
}