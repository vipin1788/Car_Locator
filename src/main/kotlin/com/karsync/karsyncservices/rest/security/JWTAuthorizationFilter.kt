package com.karsync.karsyncservices.rest.security


import com.karsync.karsyncservices.db.repository.UserRepository
import com.karsync.karsyncservices.rest.security.token.TokenService
import com.karsync.karsyncservices.service.JwtTokenService
import io.jsonwebtoken.Jwts
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.util.StringUtils

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.IOException
import java.util.ArrayList


class JWTAuthorizationFilter(authManager: AuthenticationManager,private val userRepository: UserRepository, private val jwtTokenService: JwtTokenService, private val tokenService: TokenService) : BasicAuthenticationFilter(authManager) {

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain) {
        val authentication = getAuthentication(req)
        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(req, res)
    }

    private fun getAuthentication(request: HttpServletRequest): UsernamePasswordAuthenticationToken? {
        val token = request.getHeader(SecurityConstants.HEADER_STRING)

        if (token != null) {
            // parse the token.

            val jwtToken = jwtTokenService.getJwtToken(token.replace(SecurityConstants.TOKEN_PREFIX, ""))

            if(!StringUtils.isEmpty(jwtToken.jwtToken)) {
                return null
            }

            val claims = Jwts.parser()
                    .setSigningKey(SecurityConstants.SECRET)
                    .parseClaimsJws(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
                    .body

            val userEmail = claims["emailId"].toString()

            val user = tokenService.userRegistration
            if(!tokenService.userRegistration!!.emailId.equals(userEmail)) {
                return null
            }

            if (!StringUtils.isEmpty(userEmail)) {
                val user = userRepository!!.getUser(userEmail)
                val role = ArrayList<GrantedAuthority>()
                role.add(SimpleGrantedAuthority("ROLE_" + user.userRoleType))
                return UsernamePasswordAuthenticationToken(claims, null, role)
            }
            return null
        }
        return null
    }
}
