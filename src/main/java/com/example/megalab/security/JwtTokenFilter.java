package com.example.megalab.security;

import com.example.megalab.exception.JwtAuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider provider;

    public JwtTokenFilter(JwtTokenProvider provider) {
        this.provider = provider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = provider.resolveToken((HttpServletRequest)servletRequest);
        try{
            if(token != null && provider.validateToken(token)){
                Authentication authentication = provider.getAuthentication(token);
                if(authentication != null){
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            }
        }catch (JwtAuthenticationException e){
            SecurityContextHolder.clearContext();
            HttpServletResponse httpServletResponse = ((HttpServletResponse)servletResponse);
            httpServletResponse.setStatus(e.getHttpStatus().value());
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.getWriter().write("JWT token is expired or invalidate");
            throw  new JwtAuthenticationException("JWT token is expired or invalidate");
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
