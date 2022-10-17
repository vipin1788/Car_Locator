package com.karsync.karsyncservices.config


import com.karsync.karsyncservices.db.repository.UserRepository
import com.karsync.karsyncservices.rest.security.JWTAuthenticationFilter
import com.karsync.karsyncservices.rest.security.JWTAuthorizationFilter
import com.karsync.karsyncservices.rest.security.KaysyncAuthenticationEntryPoint
import com.karsync.karsyncservices.rest.security.token.TokenProvider
import com.karsync.karsyncservices.rest.security.token.TokenService
import com.karsync.karsyncservices.service.JwtTokenService
import com.karsync.karsyncservices.utils.KarSyncConstants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.util.*

/**
 * Purpose - This is Security Configuration class.
 *
 * @author Jyoti Verma
 */

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
class WebSecurity
@Autowired
constructor(private val userDetailsService: UserDetailsService, private val tokenProvider: TokenProvider, private val userRepository: UserRepository, private val kaysyncAuthenticationEntryPoint: KaysyncAuthenticationEntryPoint, private val passwordEncoder: PasswordEncoder, private val tokenService: TokenService, private val jwtTokenService: JwtTokenService) : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {

        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(kaysyncAuthenticationEntryPoint).and()
                .authorizeRequests()
                .antMatchers(*KarSyncConstants.SWAGGER_URL).permitAll()
                .anyRequest().authenticated()
                .anyRequest().not().anonymous()
                .and()
                .addFilterBefore(JWTAuthenticationFilter(authenticationManager(), tokenProvider, userRepository, tokenService), UsernamePasswordAuthenticationFilter::class.java)
                .addFilter(JWTAuthorizationFilter(authenticationManager(), userRepository, jwtTokenService,tokenService))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    @Throws(Exception::class)
    public override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder)
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.addAllowedHeader("*")
        configuration.allowedOrigins = Arrays.asList("*")
        configuration.allowCredentials = true
        configuration.allowedMethods = Arrays.asList("OPTIONS", "GET", "POST", "PUT", "DELETE", "PATCH")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }


}