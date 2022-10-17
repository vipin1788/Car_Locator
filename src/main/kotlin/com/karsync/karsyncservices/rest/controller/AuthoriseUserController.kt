package com.karsync.karsyncservices.rest.controller

import com.karsync.karsyncservices.db.model.UserRegistration
import com.karsync.karsyncservices.rest.requests.AuthoriseUserRegistrationRequest
import com.karsync.karsyncservices.rest.requests.UserUpdateRequest
import com.karsync.karsyncservices.rest.requests.UserSelfUpdateRequest
import com.karsync.karsyncservices.rest.response.Response
import com.karsync.karsyncservices.rest.security.SecurityConstants
import com.karsync.karsyncservices.rest.security.token.TokenService
import com.karsync.karsyncservices.service.JwtTokenService
import com.karsync.karsyncservices.service.UserService
import com.karsync.karsyncservices.utils.KarSyncConstants
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@RequestMapping(path = arrayOf(KarSyncConstants.AUTHORISE_CONTROLLER))
class AuthoriseUserController @Autowired
constructor(private val userService: UserService, private val jwtTokenService: JwtTokenService, private val tokenService: TokenService) {

    companion object {

        private val LOGGER: Logger = LoggerFactory.getLogger(AuthoriseUserController::class.java)
    }

    @PostMapping(path = arrayOf(com.karsync.karsyncservices.rest.security.SecurityConstants.AUTH_SIGN_UP))
    fun createUser(@Valid @RequestBody authoriseUserRegistrationRequest: AuthoriseUserRegistrationRequest): Response<UserRegistration> {
        LOGGER.info("AccountModel Registration Process is Started...")
        return Response(userService.createAuthoriseUser(authoriseUserRegistrationRequest))
    }

    @GetMapping(path = arrayOf("/getAllUsers/{id}"))
    fun getAllUser(@PathVariable(value = "id") id: Int): Response<List<UserRegistration>> {
        return Response(userService.getAllUsers(id))
    }

    @PutMapping(path = arrayOf("/updateUser"))
    fun updateUser(@Valid @RequestBody userUpdateRequest: UserUpdateRequest): Response<UserRegistration> {
        return Response(userService.updateUser(userUpdateRequest))
    }

    @PutMapping(path = arrayOf("/selfUpdateUser"))
    fun updateSelfUser(@Valid @RequestBody userSelfUpdateRequest: UserSelfUpdateRequest): Response<UserRegistration> {
        return Response(userService.updateSelfInfo(userSelfUpdateRequest))
    }

    @DeleteMapping(path = arrayOf("/deleteUser/{id}"))
    fun deleteUser(@PathVariable(value = "id") id: Int): Response<Void> {
        return Response(userService.deleteUser(id), "all information regarding this user is delete")
    }

    @PostMapping(path = arrayOf("/logout"))
    fun logout(request: HttpServletRequest): Response<Void> {
        return Response(jwtTokenService.addJwtToken(request.getHeader(SecurityConstants.HEADER_STRING).replace(SecurityConstants.TOKEN_PREFIX,"")), "Logged out successfull")
    }
}
