package com.karsync.karsyncservices.rest.security

object SecurityConstants {

    const val SECRET = "1234567812345678"
    const val EXPIRATION_TIME: Long = 864000000 // 10 days
    const val TOKEN_PREFIX = "Bearer "
    const val HEADER_STRING = "Authorization"
    const val SIGN_UP_URL = "/createUser"
    const val AUTH_SIGN_UP = "/register"

}
