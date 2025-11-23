package com.duoc.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private static final String SECRET_KEY = "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0u1v2w3x4y5z6a7b8c9d0";

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    /** Genera TOKEN JWT*/
    public String generateToken(String subject, Map<String, Object> claims, long millisTTL) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + millisTTL);

        return Jwts.builder()
                .setSubject(subject)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key)
                .compact();
    }
}
