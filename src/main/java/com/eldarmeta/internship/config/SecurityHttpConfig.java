package com.eldarmeta.internship.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityHttpConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // CSRF: отключаем для auth и h2-console
                .csrf(csrf -> csrf.ignoringRequestMatchers(
                        "/api/auth/**",
                        "/h2-console/**"
                ))

                // H2 console работает во фрейме
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))

                // доступы
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",
                                "/h2-console/**"
                        ).permitAll()
                        .anyRequest().permitAll()
                )

                // отключаем дефолтную security-логику
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());

        return http.build();
    }
}
