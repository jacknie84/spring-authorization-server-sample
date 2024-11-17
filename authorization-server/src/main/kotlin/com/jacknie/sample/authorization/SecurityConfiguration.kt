package com.jacknie.sample.authorization

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.core.convert.converter.Converter
import org.springframework.http.MediaType
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher
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
        cookieSerializer.setCookieName("AUTHORIZATION_SERVER_SESSION")
        return cookieSerializer
    }

    @Bean
    @Order(value = Ordered.HIGHEST_PRECEDENCE)
    fun authorizationServerSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)
        http.getConfigurer(OAuth2AuthorizationServerConfigurer::class.java).oidc(Customizer.withDefaults())
        http.oauth2ResourceServer { it.opaqueToken { c -> c.introspector(opaqueTokenIntrospector(http)) } }
        http.exceptionHandling {
            val entryPoint = LoginUrlAuthenticationEntryPoint("/login")
            val preferredMatcher = MediaTypeRequestMatcher(MediaType.TEXT_HTML)
            preferredMatcher.setIgnoredMediaTypes(setOf(MediaType.ALL))
            it.defaultAuthenticationEntryPointFor(entryPoint, preferredMatcher)
        }
        return http.build()
    }

    @Bean
    @Order(value = SecurityProperties.BASIC_AUTH_ORDER)
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { it.anyRequest().authenticated() }
            .formLogin(Customizer.withDefaults())
            .oauth2Login(Customizer.withDefaults())
        return http.build()
    }

    @Bean
    fun opaqueTokenIntrospector(http: HttpSecurity) = OpaqueTokenIntrospector { token ->
        val authorizationService = http.getSharedObject(OAuth2AuthorizationService::class.java)
        val authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN)
            ?: throw BadOpaqueTokenException("토큰에 대해서 인가 정보 찾을 수 없음(token: $token)")
        OAuth2IntrospectionAuthenticatedPrincipal(authorization.attributes, emptySet())
    }

    @Bean
    fun oidcUserService(): OidcUserService {
        val oidcUserService = OidcUserService()
        oidcUserService.setOauth2UserService(oauth2UserService())
        return oidcUserService
    }

    @Bean
    fun oauth2UserService(): OAuth2UserService<OAuth2UserRequest, OAuth2User> {
        val userService = DefaultOAuth2UserService()
        userService.setAttributesConverter { request ->
            if (request.clientRegistration.registrationId == "naver") {
                Converter {
                    val response = it["response"] as Map<*, *>
                    response.entries.associate { (k, v) -> k.toString() to v }
                }
            } else {
                Converter { it }
            }
        }
        return userService
    }
}