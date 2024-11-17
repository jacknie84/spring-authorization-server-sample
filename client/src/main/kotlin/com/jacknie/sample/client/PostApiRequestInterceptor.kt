package com.jacknie.sample.client

import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.http.HttpHeaders

class PostApiRequestInterceptor: RequestInterceptor {

    override fun apply(template: RequestTemplate) {
        template.request().header(HttpHeaders.AUTHORIZATION, "Bearer 12345")
    }
}