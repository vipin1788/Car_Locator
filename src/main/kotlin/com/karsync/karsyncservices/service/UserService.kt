package com.karsync.karsyncservices.service


import com.karsync.karsyncservices.db.model.UserRegistration
import com.karsync.karsyncservices.rest.requests.AuthoriseUserRegistrationRequest
import com.karsync.karsyncservices.rest.requests.UserRegistrationRequest
import com.karsync.karsyncservices.rest.requests.UserSelfUpdateRequest
import com.karsync.karsyncservices.rest.requests.UserUpdateRequest

/**
 * Purpose - Interface for KarSync Services.
 *
 * @author Jyoti Verma
 */

interface UserService {

    /**
     * @param accountRequest [AccountModel]
     * @return boolean
     * @implNote method is used for Create New AccountModel.
     */
    fun createAuthoriseUser(authoriseUserRegistrationRequest: AuthoriseUserRegistrationRequest): UserRegistration

    fun createUser(userRegistrationRequest: UserRegistrationRequest): UserRegistration

    fun getAllUsers(id: Int): List<UserRegistration>

    fun updateUser(userUpdateRequest: UserUpdateRequest): UserRegistration

    fun updateSelfInfo(userSelfUpdateRequest: UserSelfUpdateRequest): UserRegistration

    fun deleteUser(id: Int): Boolean
}
