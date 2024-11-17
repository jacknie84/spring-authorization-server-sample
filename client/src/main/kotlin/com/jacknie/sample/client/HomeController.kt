package com.jacknie.sample.client

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController {

    @GetMapping(path = ["/", "/home"])
    fun home(
        @RegisteredOAuth2AuthorizedClient client: OAuth2AuthorizedClient,
        @AuthenticationPrincipal principal: Any,
        model: Model
    ): String {
        model
            .addAttribute("client", client)
            .addAttribute("principal", principal)
        return "home"
    }
}