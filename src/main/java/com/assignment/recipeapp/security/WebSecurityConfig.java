package com.assignment.recipeapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for security.
 *
 * @author Boris Becker
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    /**
     * Filter chain bean.
     * This method is used to configure the security filter chain.
     * It is annotated with @Bean to indicate that it is a Spring bean.
     *
     * The security filter chain is configured to allow access to the H2 console and the Swagger UI.
     * All other requests are allowed.
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/h2-console/**").permitAll()
                .and()
                .httpBasic();
        return http.build();
    }

    /**
     * Configure global.
     * This method is used to configure the authentication manager builder.
     * @param auth
     * @param passwordEncoder
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER");
    }

}

