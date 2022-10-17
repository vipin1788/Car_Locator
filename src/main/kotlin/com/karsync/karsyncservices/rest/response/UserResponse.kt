package com.karsync.karsyncservices.rest.response


import com.karsync.karsyncservices.db.model.UserRegistration

class UserResponse(var userRegistration: UserRegistration?, var token: String?) {
}
