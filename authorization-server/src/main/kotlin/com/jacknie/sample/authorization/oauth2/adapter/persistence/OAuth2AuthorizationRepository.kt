package com.jacknie.sample.authorization.oauth2.adapter.persistence

import com.jacknie.sample.authorization.oauth2.domain.OAuth2AuthorizationEntity
import org.springframework.data.jpa.repository.JpaRepository

interface OAuth2AuthorizationRepository: JpaRepository<OAuth2AuthorizationEntity, String>