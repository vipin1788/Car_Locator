package com.karsync.karsyncservices.db.repository.impl

import com.karsync.karsyncservices.db.model.UserRegistration
import com.karsync.karsyncservices.db.model.enumtype.UserRoleType
import com.karsync.karsyncservices.db.query.UserRepositoryQuery
import com.karsync.karsyncservices.db.repository.UserRepository
import com.karsync.karsyncservices.exception.InvalidRequestException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.*

@Repository
class UserRepositoryImpl : UserRepository {


    @Autowired
    private val jdbcTemplate: JdbcTemplate? = null


    override fun getUser(emailId: String): UserRegistration {
        try {
            return jdbcTemplate!!.queryForObject(UserRepositoryQuery.FETCH_ACTION, arrayOf(emailId), UserMapper())
        } catch (e: Exception) {
            return UserRegistration()
        }
    }

    override fun addUser(userRegistration: UserRegistration?): UserRegistration {
        val holder = GeneratedKeyHolder()
        jdbcTemplate!!.update(object : PreparedStatementCreator {
            @Throws(SQLException::class)
            override fun createPreparedStatement(connection: Connection): PreparedStatement {
                val ps = connection.prepareStatement(UserRepositoryQuery.INSERT_ACTION, Statement.RETURN_GENERATED_KEYS)
                ps.setString(1, userRegistration!!.name)
                ps.setString(2, userRegistration!!.emailId)
                ps.setString(3, userRegistration!!.password)
                ps.setString(4, userRegistration!!.userRoleType.toString().toUpperCase())
                ps.setBoolean(5, userRegistration!!.isStatus)
                ps.setBoolean(6, userRegistration.selfRegister)
                ps.setInt(7, userRegistration!!.referalId)
                return ps
            }
        }, holder)

        val newUserId = holder.key!!.toInt()
        userRegistration!!.id = newUserId
        return userRegistration
    }

    override fun getUserList(id: Int): MutableList<UserRegistration> {
        try {
             return jdbcTemplate!!.query(UserRepositoryQuery.FETCH_USER_LIST+id,UserMapper())
        } catch (e: Exception) {
            return ArrayList<UserRegistration>()
        }
    }

    override fun getSuperAdminList(id: Int): MutableList<UserRegistration> {
        try {
            return jdbcTemplate!!.query(UserRepositoryQuery.FETCH_ADMIN_USER_LIST+id,UserMapper())
        } catch (e: Exception) {
            return ArrayList<UserRegistration>()
        }
    }

    override fun getUserById(id: Int): UserRegistration {
        try {
            return jdbcTemplate!!.queryForObject(UserRepositoryQuery.FETCH_USER_BY_ID, arrayOf(id), UserMapper())
        } catch (e: Exception) {
            throw InvalidRequestException("Record does not exist")
        }
    }

    override fun updateUser(userRegistration: UserRegistration?): Int {
        try {

            return jdbcTemplate!!.update(UserRepositoryQuery.UPDATE_USER_RECORD, userRegistration!!.name,userRegistration!!.emailId,userRegistration!!.password,userRegistration!!.userRoleType.toString(),userRegistration!!.isStatus,userRegistration!!.selfRegister,userRegistration!!.referalId,userRegistration!!.id)
        } catch (e: Exception) {
            throw InvalidRequestException("Record does not exist")
        }
    }

    override fun deleterRelatedInfo(id: Int) : Int{
        return jdbcTemplate!!.update(UserRepositoryQuery.DELETE_MULTI_ROWS, id,false)
    }

    override fun deleterUser(id: Int) : Int{
        return jdbcTemplate!!.update(UserRepositoryQuery.DELETE_USER, id)
    }

    private inner class UserMapper : RowMapper<UserRegistration> {

        @Throws(SQLException::class)
        override fun mapRow(rs: ResultSet, rowNum: Int): UserRegistration? {
            val userRegistration = UserRegistration()
            userRegistration.id = rs!!.getInt("id")
            userRegistration.name = rs!!.getString("name")
            userRegistration.emailId = rs!!.getString("emailId")
            userRegistration.password = rs!!.getString("password")
            userRegistration.userRoleType = UserRoleType.valueOf(rs!!.getString("roleType"))
            userRegistration.isStatus = rs.getBoolean("status")
            userRegistration.selfRegister = rs.getBoolean("selfRegister")
            userRegistration.referalId = rs.getInt("referalId")
            return userRegistration
        }
    }
}
