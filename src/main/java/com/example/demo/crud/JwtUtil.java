package com.example.demo.crud;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtUtil {

    // 🔑 مفتاح سري ثابت وطويل (على الأقل 32 بايت)
    private static final String SECRET_KEY = "01234567890123456789012345678901";

    private static Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    // 🟢 إنشاء التوكن
    public static String generateToken(Long userId, String email) {
        Key key = getSigningKey();
        return Jwts.builder()
                .claim("userId", userId)
                .claim("email", email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 5))) // 5 ساعات
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 🟣 التحقق من التوكن
    public static Claims validateToken(String token) throws Exception {
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
