package com.jacknie.sample.client

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.session.web.http.CookieSerializer
import org.springframework.session.web.http.DefaultCookieSerializer

@Configuration
class SecurityConfiguration {

    @Bean
    @ConditionalOnProperty(name = ["spring.h2.console.enabled"], havingValue = "true")
    fun h2ConsoleEnabledWebSecurityCustomizer() = WebSecurityCustomizer {
        it.ignoring().requestMatchers(PathRequest.toH2Console())
    }

    @Bean
    fun cookeSerializer(): CookieSerializer {
        val cookieSerializer = DefaultCookieSerializer()
        cookieSerializer.setCookieName("CLIENT_SESSION")
        return cookieSerializer
    }

    @Bean
    fun oidcUserService(): OidcUserService {
        val userService = OidcUserService()
//        userService.setRetrieveUserInfo { false }
        return userService
    }
}