package com.karsync.karsyncservices.rest.handler


import com.karsync.karsyncservices.rest.response.Response

class ApiErrorResponse : Response<String> {

    var errorCode: Int = 0
        private set

    constructor(errorCode: Int) {
        this.errorCode = errorCode
    }

    constructor(status: Boolean, message: String, data: String, errorCode: Int) : super(status, message, data) {
        this.errorCode = errorCode
    }

    constructor(data: String, errorCode: Int) : super(data) {
        this.errorCode = errorCode
    }

    constructor(status: Boolean, message: String, errorCode: Int) : super(status, message) {
        this.errorCode = errorCode
    }

}

