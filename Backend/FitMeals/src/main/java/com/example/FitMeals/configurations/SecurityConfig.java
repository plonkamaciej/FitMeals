package com.example.FitMeals.configurations;

import com.example.FitMeals.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(crsf->crsf.disable()) // Wyłącza CSRF (opcja przydatna podczas testowania)
                .authorizeRequests(auth -> auth
                        .requestMatchers("/api/users/register", "/api/users/login").permitAll() // Pozwala na dostęp do /register i /login bez autoryzacji
                        .anyRequest().authenticated() // Pozostałe endpointy wymagają autoryzacji
                )
                .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class); // Dodanie filtra JWT

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}