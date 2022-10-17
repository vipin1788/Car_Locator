package com.karsync.karsyncservices.db.query

object TokenRepositoryQuery{

    const val BLACK_INSERT_TOKEN ="INSERT INTO jwt_token(token) VALUES(?)"

    const val FETCH_TOKEN_QUERY = "SELECT * FROM jwt_token WHERE token=?"

}
