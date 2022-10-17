package com.karsync.karsyncservices.rest.requests

import com.karsync.karsyncservices.db.model.enumtype.UserRoleType

class UserUpdateRequest : UserRegistrationRequest() {

    var parentId: Int = 0

    var userId: Int = 0

    var userRoleType: UserRoleType? = null

    var isStatus: Boolean = false
}
