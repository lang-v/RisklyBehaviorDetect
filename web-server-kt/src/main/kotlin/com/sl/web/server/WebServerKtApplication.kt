package com.sl.web.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebServerKtApplication

fun main(args: Array<String>) {
    runApplication<WebServerKtApplication>(*args)
}
