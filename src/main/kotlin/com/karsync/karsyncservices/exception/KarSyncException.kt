package com.karsync.karsyncservices.exception

open class KarSyncException : RuntimeException {

    constructor() {}

    constructor(message: String) : super(message) {}

    constructor(message: String, cause: Throwable) : super(message, cause) {}
}
