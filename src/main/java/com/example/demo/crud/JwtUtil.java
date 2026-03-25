package com.example.demo.crud;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Long userId, String email) {
        Key key = getSigningKey();

        return Jwts.builder()
                .claim("userId", userId)
                .claim("email", email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 5))) // 5 hours
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validateToken(String token) throws Exception {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (JwtException e) {
            throw new Exception("Invalid JWT token: " + e.getMessage());
        }
    }
}