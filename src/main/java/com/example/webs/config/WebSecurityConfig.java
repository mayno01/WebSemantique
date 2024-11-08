package com.example.webs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.authentication.AuthenticationManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints (without token)
                        .requestMatchers(("/reservations/**")).permitAll()
                        .requestMatchers("/equipes/**").permitAll()  // Allow all "/equipes" routes

                        .requestMatchers("/users/register", "/users/login").permitAll()
                        .requestMatchers("/api/reclamations/all", "/api/reclamations/{reclamationId}/responses").permitAll()
                        .requestMatchers("/api/reclamations/addReclamation", "/api/reclamations/addResponse").permitAll()
                        .requestMatchers("/api/reclamations/delete/{id}", "/api/reclamations/responses/delete/{responseId}").permitAll()
                        .anyRequest().authenticated() // Secure any other requests
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No session management
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults())
                .build();
    }
}
