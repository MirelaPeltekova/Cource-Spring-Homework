package com.recipes.config;


import com.recipes.model.Role;
import com.recipes.service.UserService;
import com.recipes.web.FilterChainExceptionHandlerFilter;
import com.recipes.web.JWTAuthenticationEntryPoint;
import com.recipes.web.JwtRequestFilter;
import com.recipes.web.NotAllowedAccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private JwtRequestFilter jwtRequestFilter;
    private FilterChainExceptionHandlerFilter filterChainExceptionHandlerFilter;
    private NotAllowedAccessHandler notAllowedAccessHandler;

    @Autowired
    SecurityConfig(JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                   JwtRequestFilter jwtRequestFilter,
                   FilterChainExceptionHandlerFilter filterChainExceptionHandlerFilter,
                   NotAllowedAccessHandler notAllowedAccessHandler) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtRequestFilter = jwtRequestFilter;
        this.filterChainExceptionHandlerFilter = filterChainExceptionHandlerFilter;
        this.notAllowedAccessHandler = notAllowedAccessHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(POST, "/api/login", "api/register").permitAll()
                .antMatchers(GET, "/api/recipes/**").permitAll()
                .antMatchers(POST, "/api/recipes").hasAnyRole(Role.ADMIN.toString(), Role.USER.toString())
                .antMatchers(PUT, "/api/recipes").hasAnyRole(Role.ADMIN.toString(), Role.USER.toString())
                .antMatchers(DELETE, "/api/recipes").hasAnyRole(Role.ADMIN.toString(), Role.USER.toString())
                .antMatchers("/api/users/**").hasRole(Role.ADMIN.toString())
                .antMatchers("/**").permitAll()
                .and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                //handling errors with status 403 (AccessDenied) when role is not allowed for that action
                .and().exceptionHandling().accessDeniedHandler(notAllowedAccessHandler)
                //to not have sessions
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(filterChainExceptionHandlerFilter, LogoutFilter.class);

    }

    @Bean
    public UserDetailsService getUserDetailsService(UserService userService) {
        return userService::getUserByUsername;
    }

    //expose AuthenticationManager coming from webSecurityAdapter
    @Bean
    public AuthenticationManager getAuthenticationManger() throws Exception {
        return super.authenticationManager();
    }

}
