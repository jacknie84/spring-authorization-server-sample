package com.jacknie.sample.authorization.oauth2.authorization.domain

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType

@Converter
class OAuth2TokenTypeConverter: AttributeConverter<OAuth2TokenType, String> {

    override fun convertToDatabaseColumn(attribute: OAuth2TokenType): String {
        return attribute.value
    }

    override fun convertToEntityAttribute(dbData: String): OAuth2TokenType {
        return OAuth2TokenType(dbData)
    }
}