package com.karsync.karsyncservices.service

import com.karsync.karsyncservices.db.model.BlackListToken

interface JwtTokenService {

    fun addJwtToken(jwtToken: String): Boolean

    fun getJwtToken(token: String): BlackListToken
}
