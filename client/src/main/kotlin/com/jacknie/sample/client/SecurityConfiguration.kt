package com.jacknie.sample.client

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.web.SecurityFilterChain
import org.springframework.session.web.http.CookieSerializer
import org.springframework.session.web.http.DefaultCookieSerializer

@Configuration
class SecurityConfiguration(private val clientRegistrationRepository: ClientRegistrationRepository) {

    @Bean
    @ConditionalOnProperty(name = ["spring.h2.console.enabled"], havingValue = "true")
    fun h2ConsoleEnabledWebSecurityCustomizer() = WebSecurityCustomizer {
        it.ignoring().requestMatchers(PathRequest.toH2Console())
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { it.anyRequest().authenticated() }
            .oauth2Login(Customizer.withDefaults())
            .logout {
                val logoutSuccessHandler = OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository)
                logoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}")
                it.logoutSuccessHandler(logoutSuccessHandler)
            }
        return http.build()
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