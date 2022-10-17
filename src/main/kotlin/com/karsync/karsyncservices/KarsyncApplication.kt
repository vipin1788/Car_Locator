package com.karsync.karsyncservices

import com.karsync.karsyncservices.config.SuperAdminConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("com.karsync.karsyncservices")
@EnableConfigurationProperties(SuperAdminConfig::class)
class KarsyncApplication {

    constructor() {
        LOGGER.info("Bootstrapping Application")
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(KarsyncApplication::class.java)
    }

}

fun main(args: Array<String>) {
    runApplication<KarsyncApplication>(*args)
}

