package com.opencommerce.catalogservice.config;



import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/categories/**"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/products/**"
                        ).permitAll()

                        .anyRequest()
                        .authenticated()

                );

        return http.build();
    }
}