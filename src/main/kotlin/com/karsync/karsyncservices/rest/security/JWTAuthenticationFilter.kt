package com.karsync.karsyncservices.rest.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.karsync.karsyncservices.db.repository.UserRepository
import com.karsync.karsyncservices.rest.requests.UserLoginRequest
import com.karsync.karsyncservices.rest.response.UserResponse
import com.karsync.karsyncservices.rest.security.token.TokenProvider
import com.karsync.karsyncservices.rest.security.token.TokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.web.WebAttributes
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.io.IOException
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthenticationFilter @Autowired
constructor(private val auth: AuthenticationManager, private val tokenProvider: TokenProvider, private val userRepository: UserRepository, private val tokenService: TokenService) : AbstractAuthenticationProcessingFilter(AntPathRequestMatcher("/api/v1/public/login")) {

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        try {
            val userLoginRequest = ObjectMapper().readValue(request.inputStream, UserLoginRequest::class.java)
            return auth.authenticate(UsernamePasswordAuthenticationToken(userLoginRequest.userName, userLoginRequest.password, ArrayList<GrantedAuthority>()))
        } catch (e: IOException) {
            throw BadCredentialsException("credential are invalid")
        }

    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain?, auth: Authentication) {
        val userEmail = (auth.principal as org.springframework.security.core.userdetails.User).username
        val token = tokenProvider.getAuthToken(userEmail)
        res.contentType = MediaType.APPLICATION_JSON_VALUE

        val session = req.getSession(true)
        session?.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION)

        val currentUser = userRepository!!.getUser(userEmail)

        tokenService.userRegistration = currentUser

        val user = UserResponse(currentUser, token)

        val objectMapper = ObjectMapper()
        res.writer.write(objectMapper.writeValueAsString(user))
        res.writer.flush()
    }


}
