package com.jacknie.sample.authorization.oauth2.adapter.framework.authorization.token

import com.jacknie.sample.authorization.oauth2.application.authorization.OAuth2Authorization

interface OAuth2TokenMetadataHandler<T> {
    fun setOAuth2AuthorizationTokenEntityMetadata(token: OAuth2Authorization.Token, value: T)
    fun getOAuth2TokenFieldValue(token: OAuth2Authorization.Token): T
}