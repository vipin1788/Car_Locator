package com.karsync.karsyncservices.db.repository

import com.karsync.karsyncservices.db.model.UserRegistration
import org.springframework.stereotype.Component

@Component
interface UserRepository {

    fun getUser(emailId: String): UserRegistration

    fun addUser(userRegistration: UserRegistration?): UserRegistration

    fun getUserList(id: Int): List<UserRegistration>

    fun getSuperAdminList(id: Int): List<UserRegistration>

    fun getUserById(id: Int): UserRegistration

    fun updateUser(userRegistration: UserRegistration?): Int

    fun deleterRelatedInfo(id: Int) : Int
    fun deleterUser(id: Int): Int
}
