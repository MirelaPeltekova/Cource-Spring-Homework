package com.recipes.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipes.model.ErrorResponse;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class FilterChainExceptionHandlerFilter extends OncePerRequestFilter {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private ErrorHandlerControllerAdvice controllerAdvice;

    @Autowired
    public FilterChainExceptionHandlerFilter(ErrorHandlerControllerAdvice controllerAdvice) {
        this.controllerAdvice = controllerAdvice;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException | AuthenticationException e) {
            log.error("Spring security filter chain exception", e);
            //we should explicitely call controller advice as it is not called that earlier
            ResponseEntity<ErrorResponse> responseEntity = controllerAdvice.handleAuthenticationException((RuntimeException) e);
            //we should manually set the  status
            response.setStatus(responseEntity.getStatusCodeValue());
            PrintWriter out = response.getWriter();
            //ObjectMapper is jackson object used for serialization
            new ObjectMapper().writeValue(out, responseEntity.getBody());
            //missing token was retrieving 500 with BadCredentialsException so catch here
        } catch (BadCredentialsException e) {
            ResponseEntity<ErrorResponse> responseEntity = controllerAdvice.handleBadCredentialsException((RuntimeException) e);
            response.setStatus(responseEntity.getStatusCodeValue());
            PrintWriter out = response.getWriter();
            new ObjectMapper().writeValue(out, responseEntity.getBody());
        }
    }
}
