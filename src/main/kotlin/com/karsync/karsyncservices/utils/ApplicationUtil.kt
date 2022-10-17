package com.karsync.karsyncservices.utils


import com.karsync.karsyncservices.config.SuperAdminConfig
import com.karsync.karsyncservices.db.model.UserRegistration
import com.karsync.karsyncservices.db.model.enumtype.UserRoleType
import com.karsync.karsyncservices.db.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class ApplicationUtil {

    @Autowired
    private val superAdminConfig: SuperAdminConfig? = null

    @Autowired
    private val userRepository: UserRepository? = null

    @Autowired
    private val passwordEncoder: PasswordEncoder? = null


    @PostConstruct
    fun createSuperAdmin() {
        var userRegistration = userRepository!!.getUser(superAdminConfig!!.emailId)
        if (!userRegistration.isStatus) {
            userRegistration = UserRegistration()
            userRegistration.name = superAdminConfig!!.name
            userRegistration.emailId = superAdminConfig!!.emailId
            userRegistration.isStatus = true
            userRegistration.referalId = 0
            userRegistration.userRoleType = UserRoleType.SUPER_ADMIN
            userRegistration.selfRegister = false
            userRegistration.password = passwordEncoder!!.encode(superAdminConfig!!.password)
            userRepository!!.addUser(userRegistration)
        }
    }
}
