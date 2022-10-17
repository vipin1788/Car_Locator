package com.karsync.karsyncservices.db.model

class BlackListToken : BaseModel {

    var jwtToken: String = ""

    constructor() {}

    constructor(jwtToken: String) {
        this.jwtToken = jwtToken
    }
}
