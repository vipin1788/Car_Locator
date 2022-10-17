package com.karsync.karsyncservices.rest.response

open class Response<T> {

    var status: Boolean = false

    var message = ""

    var data: T? = null

    constructor() {}

    constructor(status: Boolean, message: String, data: T) {
        this.status = status
        this.message = message
        this.data = data
    }

    constructor(data: T) {
        this.data = data
        this.status = true
    }

    constructor(status: Boolean, message: String) {
        this.status = status
        this.message = message
    }
}