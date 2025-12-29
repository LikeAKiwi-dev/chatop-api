package com.openclassrooms.chatop.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private static final long EXP_MS = 24 * 60 * 60 * 1000; // 24h
    private final Key key;

    public JwtService() {
        String secret = System.getenv("JWT_SECRET");
        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException("JWT_SECRET must be set and >= 32 chars");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Integer userId) {
        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXP_MS))
                .signWith(key)
                .compact();
    }

    public Integer extractUserId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return Integer.valueOf(claims.getSubject());
    }
}
