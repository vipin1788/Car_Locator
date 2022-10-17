package com.karsync.karsyncservices.rest.security


import com.karsync.karsyncservices.rest.handler.ApiErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class KaysyncAuthenticationEntryPoint : AuthenticationEntryPoint {
    @Throws(IOException::class, ServletException::class)
    override fun commence(request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException) {
        authException.printStackTrace()
        val error = ApiErrorResponse(false, authException.message.toString(), HttpStatus.UNAUTHORIZED.value())

        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.writer.write(com.karsync.karsyncservices.utils.JsonUtil.toJson(error))
    }
}
