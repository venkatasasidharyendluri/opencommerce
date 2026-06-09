package com.opencommerce.authservice.security;

import com.opencommerce.authservice.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(User user) {

        return Jwts.builder()
                .subject(user.getEmail())
                .claim(
                        "userUuid",
                        user.getUuid().toString()
                )
                .claim(
                        "roles",
                        user.getRoles()
                                .stream()
                                .map(role -> role.getName().name())
                                .toList()
                )
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + jwtExpiration
                        )
                )
                .signWith(getSigningKey())
                .compact();
    }
    private Claims extractClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public String extractEmail(String token){

        return extractClaims(token)
                .getSubject();
    }
    public String extractUserUuid(String token) {

        return extractClaims(token)
                .get("userUuid", String.class);

    }
    private Date extractExpiration(String token) {

        return extractClaims(token).getExpiration();
    }

    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(
            UserDetails userDetails,
            String token
    ) {

        return extractEmail(token)
                .equals(
                        userDetails.getUsername()
                )
                &&
                !isTokenExpired(token);
    }
}
