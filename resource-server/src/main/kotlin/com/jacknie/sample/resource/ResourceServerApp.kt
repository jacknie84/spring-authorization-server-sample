package com.jacknie.sample.resource

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ResourceServerApp

fun main(args: Array<String>) {
	runApplication<ResourceServerApp>(*args)
}
