package com.jacknie.sample.authorization.oauth2.authorization.domain

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.springframework.util.StringUtils

@Converter
class StringMutableSetConverter: AttributeConverter<MutableSet<String>, String?> {

    override fun convertToDatabaseColumn(attribute: MutableSet<String>?): String? {
        return attribute?.let { StringUtils.collectionToCommaDelimitedString(it) }
    }

    override fun convertToEntityAttribute(dbData: String?): MutableSet<String> {
        return StringUtils.commaDelimitedListToSet(dbData)
    }
}