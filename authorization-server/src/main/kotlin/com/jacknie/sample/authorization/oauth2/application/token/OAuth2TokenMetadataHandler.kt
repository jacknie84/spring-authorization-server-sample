package com.jacknie.sample.authorization.oauth2.application.token

import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationTokenEntity

interface OAuth2TokenMetadataHandler<T> {
    fun setOAuth2AuthorizationTokenEntityMetadata(entity: OAuth2AuthorizationTokenEntity, value: T)
    fun getOAuth2TokenFieldValue(entity: OAuth2AuthorizationTokenEntity): T
}