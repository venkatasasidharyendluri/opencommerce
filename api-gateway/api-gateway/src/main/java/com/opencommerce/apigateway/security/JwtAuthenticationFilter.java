package com.opencommerce.apigateway.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path =
                request.getServletPath();

        if (
                path.startsWith("/api/v1/auth")
                        ||
                        path.startsWith("/api/v1/products")
                        ||
                        path.startsWith("/api/v1/categories")
        ) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }

        String authHeader =
                request.getHeader(
                        "Authorization"
                );

        if (
                authHeader == null
                        ||
                        !authHeader.startsWith(
                                "Bearer "
                        )
        ) {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            return;
        }

        String jwt =
                authHeader.substring(
                        7
                );

        if (
                !jwtService.isTokenValid(
                        jwt
                )
        ) {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            return;
        }

        filterChain.doFilter(
                request,
                response
        );
    }
}