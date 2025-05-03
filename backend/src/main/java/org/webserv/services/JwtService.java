package org.webserv.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.webserv.config.JwtProperties;
import org.webserv.models.UserRole;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.Base64;

@Service
public class JwtService {
    private final JwtProperties jwtProperties;
    private Key signingKey;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @PostConstruct
    public void init() {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(jwtProperties.getSecret());
            this.signingKey = Keys.hmacShaKeyFor(keyBytes);
            System.out.println("✅ JWT Secret Key Loaded Successfully: " + Base64.getEncoder().encodeToString(keyBytes));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid Base64 encoding for JWT secret!", e);
        }
    }


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("role", String.class);
    }

    public String generateToken(UserDetails userDetails, UserRole role) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", role.name());

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration())) // Uses configured expiration time
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .setAllowedClockSkewSeconds(300) // Allows 5-minute time difference
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
