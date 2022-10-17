package com.karsync.karsyncservices.rest.security.token

import com.karsync.karsyncservices.db.model.UserRegistration
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Service

@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
class TokenService {

    var userRegistration: UserRegistration? = null
}
