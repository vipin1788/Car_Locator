package com.karsync.karsyncservices.rest.security.token

import com.karsync.karsyncservices.rest.security.SecurityConstants
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Service

import java.util.Date
import java.util.HashMap

@Service
class TokenProvider {

    fun getAuthToken(email: String): String {
        val map = HashMap<String, Any>()

        map["emailId"] = email

        return Jwts.builder()
                .setClaims(map)
                .setExpiration(Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
                .compact()
    }
}
