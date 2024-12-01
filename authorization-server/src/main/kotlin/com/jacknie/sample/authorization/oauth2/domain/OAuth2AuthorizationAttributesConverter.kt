package com.jacknie.sample.authorization.oauth2.domain

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.springframework.security.jackson2.SecurityJackson2Modules
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module

@Converter
class OAuth2AuthorizationAttributesConverter : AttributeConverter<MutableMap<String, Any>, String?> {

    private val objectMapper: ObjectMapper

    init {
        val objectMapper = ObjectMapper()
        val modules = SecurityJackson2Modules.getModules(JdbcOAuth2AuthorizationService::class.java.classLoader)
        objectMapper.registerModules(modules)
        objectMapper.registerModules(OAuth2AuthorizationServerJackson2Module())
        this.objectMapper = objectMapper
    }

    override fun convertToDatabaseColumn(attribute: MutableMap<String, Any>?): String? {
        return attribute?.let { objectMapper.writeValueAsString(it) }
    }

    override fun convertToEntityAttribute(dbData: String?): MutableMap<String, Any> {
        return dbData?.let { objectMapper.readValue<MutableMap<String, Any>>(it) }?: mutableMapOf()
    }
}