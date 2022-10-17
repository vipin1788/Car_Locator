package com.karsync.karsyncservices.service.Impl


import com.karsync.karsyncservices.db.model.UserRegistration
import com.karsync.karsyncservices.db.model.enumtype.UserRoleType
import com.karsync.karsyncservices.db.repository.UserRepository
import com.karsync.karsyncservices.exception.AccessDeniedException
import com.karsync.karsyncservices.exception.InvalidRequestException
import com.karsync.karsyncservices.exception.UnRecoverableException
import com.karsync.karsyncservices.rest.requests.AuthoriseUserRegistrationRequest
import com.karsync.karsyncservices.rest.requests.UserRegistrationRequest
import com.karsync.karsyncservices.rest.requests.UserSelfUpdateRequest
import com.karsync.karsyncservices.rest.requests.UserUpdateRequest
import com.karsync.karsyncservices.rest.security.token.TokenService
import com.karsync.karsyncservices.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.util.*

/**
 * Purpose : This is Implementation class for KarSync Service Interface [UserService].
 *
 * @author Jyoti Verma
 */
@Service
class UserServiceImpl : UserService {

    @Autowired
    private val tokenService: TokenService? = null

    @Autowired
    private val userRepository: UserRepository? = null

    @Autowired
    private val passwordEncoder: PasswordEncoder? = null

    /**
     * @param accountRequest [AccountModel]
     * @return boolean
     * @implNote method is used for creating customerUser.
     */
    override fun createAuthoriseUser(authoriseUserRegistrationRequest: AuthoriseUserRegistrationRequest): UserRegistration {
        if(authoriseUserRegistrationRequest.userRoleType!!.equals(UserRoleType.SUPER_ADMIN)) {
            throw InvalidRequestException("Not permitted to make super admin")
        }

        var userRegistration = userRepository!!.getUser(authoriseUserRegistrationRequest!!.emailId)
        if (authoriseUserRegistrationRequest.parentId == tokenService!!.userRegistration!!.id) {
            if (!userRegistration.isStatus) {
                userRegistration = UserRegistration()
                userRegistration.name = authoriseUserRegistrationRequest.name
                userRegistration.emailId = authoriseUserRegistrationRequest.emailId
                userRegistration.isStatus = authoriseUserRegistrationRequest.isStatus
                userRegistration.referalId = tokenService!!.userRegistration!!.id
                userRegistration.userRoleType = authoriseUserRegistrationRequest.userRoleType
                userRegistration.selfRegister = false
                userRegistration.password = passwordEncoder!!.encode(authoriseUserRegistrationRequest!!.password)
                return userRepository!!.addUser(userRegistration)
            } else {
                throw InvalidRequestException("User already registered")
            }
        } else {
            throw InvalidRequestException("You are not authorise to register")
        }

    }

    override fun createUser(userRegistrationRequest: UserRegistrationRequest): UserRegistration {
        var userRegistration = userRepository!!.getUser(userRegistrationRequest!!.emailId)
        if (!userRegistration.isStatus) {
            userRegistration = UserRegistration()
            userRegistration.name = userRegistrationRequest.name
            userRegistration.emailId = userRegistrationRequest.emailId
            userRegistration.isStatus = true
            userRegistration.referalId = 0
            userRegistration.userRoleType = UserRoleType.ADMIN
            userRegistration.selfRegister = true
            userRegistration.password = passwordEncoder!!.encode(userRegistrationRequest!!.password)
            return userRepository!!.addUser(userRegistration)
        } else {
            throw InvalidRequestException("User already registered")
        }
    }

    override fun getAllUsers(id: Int): List<UserRegistration> {
        if (tokenService!!.userRegistration!!.userRoleType!!.equals(UserRoleType.SUPER_ADMIN)) {
            return userRepository!!.getSuperAdminList(id)
        } else {
            return userRepository!!.getUserList(id)
        }
    }

    override fun updateUser(userUpdateRequest: UserUpdateRequest): UserRegistration {
        val user = userRepository!!.getUserById(userUpdateRequest.userId)
        if (userUpdateRequest.parentId == tokenService!!.userRegistration!!.id && userUpdateRequest.parentId == user.referalId) {
            user.emailId = userUpdateRequest.emailId
            user.name = userUpdateRequest.name
            user.isStatus = userUpdateRequest.isStatus
            user.userRoleType = userUpdateRequest.userRoleType
            if (!StringUtils.isEmpty(userUpdateRequest.password)) {
                user.password = passwordEncoder!!.encode(userUpdateRequest.password)
            }
            var rowNum = userRepository!!.updateUser(user)
            if (rowNum > 0) {
                return user
            } else {
                throw UnRecoverableException("Can not update user information")
            }
        } else {
            throw AccessDeniedException("You are not permitted to update information")
        }
    }

    override fun updateSelfInfo(userSelfUpdateRequest: UserSelfUpdateRequest): UserRegistration {

        if (userSelfUpdateRequest.userId != tokenService!!.userRegistration!!.id) {
            throw AccessDeniedException("You are updating someone else information")
        }

        val user = userRepository!!.getUserById(userSelfUpdateRequest.userId)
        user!!.name = userSelfUpdateRequest.name
        user.emailId = userSelfUpdateRequest.emailId
        if (!StringUtils.isEmpty(userSelfUpdateRequest.currentPassword) && !StringUtils.isEmpty(userSelfUpdateRequest.newPassword)) {
            if (passwordEncoder!!.matches(userSelfUpdateRequest.currentPassword, user.password)) {
                user.password = passwordEncoder!!.encode(userSelfUpdateRequest.newPassword)
            } else {
                throw InvalidRequestException("Your current password is wrong")
            }
        }
        var rowNum = userRepository!!.updateUser(user)
        if (rowNum > 0) {
            return user
        } else {
            throw UnRecoverableException("Can not update your information")
        }
    }

    override fun deleteUser(id: Int): Boolean {
        val user = userRepository!!.getUserById(id)
        var canDelete: Boolean = false

        if (user.id == tokenService!!.userRegistration!!.id && !tokenService!!.userRegistration!!.userRoleType!!.equals(UserRoleType.SUPER_ADMIN)) {
            canDelete = true
        }

        if (user.referalId == tokenService!!.userRegistration!!.id) {
            canDelete = true
        }

        if (tokenService!!.userRegistration!!.userRoleType!!.equals(UserRoleType.SUPER_ADMIN) && user.id != tokenService!!.userRegistration!!.id) {
            canDelete = true
        }

        if (canDelete) {
            if (!userRepository!!.getUserList(id).isEmpty()) {
                userRepository!!.deleterRelatedInfo(id)
            }
            var rowNum = userRepository!!.deleterUser(id)
            return true
        } else {
            throw UnRecoverableException("Error in deleting whole information")
        }
    }
}
