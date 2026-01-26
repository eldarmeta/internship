package com.eldarmeta.internship.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                // API-style app: no server sessions
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // CSRF: ignore auth endpoints + H2 console
                .csrf(csrf -> csrf.ignoringRequestMatchers(
                        "/api/auth/**",
                        "/h2-console/**"
                ))

                // H2 console uses frames
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))

                // Access rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )

                // Disable browser-style auth mechanisms for API
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                .build();
    }
}
