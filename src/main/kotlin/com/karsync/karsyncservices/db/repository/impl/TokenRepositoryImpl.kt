package com.karsync.karsyncservices.db.repository.impl

import com.karsync.karsyncservices.db.model.BlackListToken
import com.karsync.karsyncservices.db.query.TokenRepositoryQuery
import com.karsync.karsyncservices.db.repository.TokenRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.*

@Repository
class TokenRepositoryImpl : TokenRepository {

    @Autowired
    private val jdbcTemplate: JdbcTemplate? = null

    override fun addToken(blackListToken: BlackListToken) {

        val holder = GeneratedKeyHolder()
        jdbcTemplate!!.update(object : PreparedStatementCreator {
            @Throws(SQLException::class)
            override fun createPreparedStatement(connection: Connection): PreparedStatement {
                val ps = connection.prepareStatement(TokenRepositoryQuery.BLACK_INSERT_TOKEN, Statement.RETURN_GENERATED_KEYS)
                ps.setString(1, blackListToken!!.jwtToken)
                return ps
            }
        }, holder)

        val newUserId = holder.key!!.toInt()
        blackListToken!!.id = newUserId
    }

    override fun getByToken(token: String): BlackListToken {
        try{
            return jdbcTemplate!!.queryForObject(TokenRepositoryQuery.FETCH_TOKEN_QUERY, arrayOf(token), TokenMapper())
        } catch (e: Exception) {
            return BlackListToken()
        }
    }

    private inner class TokenMapper : RowMapper<BlackListToken>{
        override fun mapRow(rs: ResultSet?, rowNum: Int): BlackListToken {
            val blackListToken = BlackListToken()
            blackListToken.id = rs!!.getInt("id")
            blackListToken.jwtToken = rs!!.getString("token")
            return blackListToken
        }

    }
}


