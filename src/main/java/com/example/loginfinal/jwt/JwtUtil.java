package com.example.loginfinal.jwt;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "kVauphSootxUy0k0T5NicxJegdgl7xtoU9Tr+emLk1JlSW6Er51zPivCUH4Aht7Zfdtdfchgvhfgdfgddgdgdfsfxdfdfsdfsdfdsf"; // Replace with a strong secret key
    private final long EXPIRATION_TIME = 1000*60*3; // 1 day in milliseconds

    private Key getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes();
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
    }


    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public Long extractExpiration(String token) {
        Date expirationDate = extractClaims(token).getExpiration();
       return expirationDate != null ? expirationDate.getTime() : null; // Returns expiration time in milliseconds, or null if date is null
   }


    public boolean isTokenExpired(String token) {
        Long expirationTime = extractExpiration(token);
        return expirationTime != null && expirationTime < System.currentTimeMillis();
    }

    public boolean validateToken(String token, String username) {
        return (extractUsername(token).equals(username) && !isTokenExpired(token));
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
