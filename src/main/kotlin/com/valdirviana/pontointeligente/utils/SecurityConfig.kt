package com.valdirviana.pontointeligente.utils

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {
//    @Autowired
//    @Throws(Exception::class)
//    fun configureGlobal(auth: AuthenticationManagerBuilder) {
//        auth
//                .inMemoryAuthentication()
//                .withUser("user").password("password").roles("USER", "ADMIN")
//    }


    @Throws(java.lang.Exception::class)
    override fun configure(http: HttpSecurity) {
        http
                .authorizeRequests()
                .antMatchers("/**").authenticated() // These urls are allowed by any authenticated user
                .and()
                .httpBasic()
        http.csrf().disable()
    }

}