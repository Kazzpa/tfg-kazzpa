package es.kazzpa.selattserver.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Security Configuration - LDAP and HTTP Authorizations.
 */
@Configuration
// @ImportResource({ "classpath:webSecurityConfig.xml" }) //=> uncomment to use equivalent xml config
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/featureReduction").permitAll().anyRequest().permitAll();
    }

}