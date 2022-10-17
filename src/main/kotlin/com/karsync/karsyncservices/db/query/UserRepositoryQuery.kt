package com.karsync.karsyncservices.db.query

object UserRepositoryQuery {

    const val DELETE_USER = "DELETE FROM users WHERE id= ?"

    const val INSERT_ACTION = "INSERT INTO users(name,emailId,password,roleType,status,selfRegister,referalId) VALUES(?,?,?,?,?,?,?)"

    const val FETCH_ACTION = "SELECT * FROM users WHERE emailId=?"

    const val FETCH_USER_BY_ID = "SELECT * FROM users WHERE id=?"

    const val UPDATE_USER_RECORD = "UPDATE users SET name= ?, emailId= ?, password= ?,roleType= ?, status= ?, selfRegister= ?, referalId= ? WHERE id= ?"

    const val FETCH_USER_LIST = "SELECT * FROM users WHERE referalId = "

    const val FETCH_ADMIN_USER_LIST = "SELECT * FROM users WHERE selfRegister = true OR referalId = "

    const val DELETE_MULTI_ROWS = "DELETE FROM users WHERE id IN (select id from (select * from users order by referalId, id) users_sorted, (select @id := ?) initialisation where find_in_set(referalId, @id) and length(@id := concat(@id, ',', id))) AND selfRegister = ?"
}
