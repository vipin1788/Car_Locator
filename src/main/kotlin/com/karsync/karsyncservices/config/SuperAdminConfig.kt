package com.karsync.karsyncservices.config


import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties

@ConfigurationProperties(prefix = "super.admin")
class SuperAdminConfig {

    var name: String = ""

    var emailId: String = ""

    var password: String = ""

    constructor() {}

    constructor(name: String, emailId: String, password: String) {
        this.name = name
        this.emailId = emailId
        this.password = password
    }
}
