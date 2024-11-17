package com.jacknie.sample.authorization

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AuthorizationServerApp

fun main(args: Array<String>) {
	runApplication<AuthorizationServerApp>(*args)
}
