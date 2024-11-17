package com.jacknie.sample.resource

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer

@Configuration
class SecurityConfiguration {

    @Bean
    @ConditionalOnProperty(name = ["spring.h2.console.enabled"], havingValue = "true")
    fun h2ConsoleEnabledWebSecurityCustomizer() = WebSecurityCustomizer {
        it.ignoring().requestMatchers(PathRequest.toH2Console())
    }
}