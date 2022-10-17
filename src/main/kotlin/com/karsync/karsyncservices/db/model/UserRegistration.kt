package com.karsync.karsyncservices.db.model

import com.karsync.karsyncservices.db.model.enumtype.UserRoleType

class UserRegistration : BaseModel {

    var name: String? = null

    var emailId: String? = null

    var password: String? = null

    var userRoleType: UserRoleType? = null

    var isStatus: Boolean = false

    var selfRegister: Boolean = false

    var referalId: Int = 0

    constructor() {}

    constructor(name: String, emailId: String, password: String, userRoleType: UserRoleType, status: Boolean, selfRegister: Boolean, referalId: Int) {
        this.name = name
        this.emailId = emailId
        this.password = password
        this.userRoleType = userRoleType
        this.isStatus = status
        this.referalId = referalId
        this.selfRegister = selfRegister    }
}
