package com.karsync.karsyncservices.rest.controller


import com.karsync.karsyncservices.db.model.UserRegistration
import com.karsync.karsyncservices.rest.requests.AuthoriseUserRegistrationRequest
import com.karsync.karsyncservices.rest.requests.UserRegistrationRequest
import com.karsync.karsyncservices.rest.response.Response
import com.karsync.karsyncservices.service.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * Purpose : This is KarSync Controller Class.
 *
 * @author Jyoti Verma
 */
@RestController
@RequestMapping(path = arrayOf(com.karsync.karsyncservices.utils.KarSyncConstants.PUBLIC_URL))
class PublicUserController @Autowired
constructor(private val userService: UserService) {


    /**
     * @param accountRequest
     * @return response [Response]
     * @implNote This method is Used For AccountModel registration
     */
    @PostMapping(path = arrayOf(com.karsync.karsyncservices.rest.security.SecurityConstants.SIGN_UP_URL))
    fun createUser(@Valid @RequestBody userRegistrationRequest: UserRegistrationRequest): Response<UserRegistration> {
        LOGGER.info("AccountModel Registration Process is Started...")
        return Response(userService.createUser(userRegistrationRequest))
    }
    companion object {

        private val LOGGER: Logger = LoggerFactory.getLogger(PublicUserController::class.java)
    }


}
