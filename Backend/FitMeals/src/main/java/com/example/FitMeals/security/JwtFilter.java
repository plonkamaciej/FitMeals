package com.example.FitMeals.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (JwtUtil.isTokenValid(token)) {
                // Wstrzykiwanie autoryzacji na podstawie tokena
                SecurityContextHolder.getContext().setAuthentication(getAuthentication(token));
            }
        }
        filterChain.doFilter(request, response);
    }

    private Authentication getAuthentication(String token) {
        String username = JwtUtil.extractClaims(token).getSubject();
        return new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
    }
}
