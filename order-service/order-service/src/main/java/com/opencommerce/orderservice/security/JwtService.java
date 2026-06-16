package com.opencommerce.orderservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                secret.getBytes()
        );
    }

    private Claims extractClaims(
            String token
    ) {

        return Jwts.parser()
                .verifyWith(
                        getSigningKey()
                )
                .build()
                .parseSignedClaims(
                        token
                )
                .getPayload();
    }

    public String extractUserUuid(
            String token
    ) {

        return extractClaims(token)
                .get(
                        "userUuid",
                        String.class
                );
    }
}