package com.karsync.karsyncservices.service.Impl

import com.karsync.karsyncservices.db.model.BlackListToken
import com.karsync.karsyncservices.db.repository.TokenRepository
import com.karsync.karsyncservices.exception.UnRecoverableException
import com.karsync.karsyncservices.service.JwtTokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils

@Service
class TokenServiceImpl : JwtTokenService {


    @Autowired
    private val tokenRepository: TokenRepository? = null

    override fun addJwtToken(jwtToken: String): Boolean {
        if (!StringUtils.isEmpty(tokenRepository!!.getByToken(jwtToken))) {
           val blackListToken = BlackListToken()
            blackListToken.jwtToken = jwtToken
            tokenRepository!!.addToken(blackListToken)
            return true
        }
        throw UnRecoverableException("you can not logout")
    }

    override fun getJwtToken(token: String): BlackListToken {
        return tokenRepository!!.getByToken(token)
    }
}
