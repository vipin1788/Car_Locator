package com.karsync.karsyncservices.db.model.enumtype

enum class UserRoleType private constructor(private val role: String) {

    SUPER_ADMIN("super_admin"), ADMIN("admin"), DEALER("dealer"), CLIENT("client");

    override fun toString(): String {
        return role.toUpperCase()
    }
}
