package com.karsync.karsyncservices.db.repository

import com.karsync.karsyncservices.db.model.BlackListToken
import org.springframework.stereotype.Component

@Component
interface TokenRepository {

    fun addToken(blackListToken: BlackListToken)

    fun getByToken(token: String): BlackListToken
}
