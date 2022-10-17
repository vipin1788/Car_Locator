package com.karsync.karsyncservices.rest.security


import com.karsync.karsyncservices.db.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserDetailServiceImp : UserDetailsService {

    @Autowired
    private val userRepository: UserRepository? = null

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(s: String): UserDetails {
        val user = userRepository!!.getUser(s) ?: throw UsernameNotFoundException("complete registration first")
        if (!user.isStatus) {
            throw BadCredentialsException("User is not approved to login ")
        }

        val role = ArrayList<GrantedAuthority>()
        role.add(SimpleGrantedAuthority("ROLE_" + user.userRoleType))
        return User(user.emailId, user.password, role)
    }
}
