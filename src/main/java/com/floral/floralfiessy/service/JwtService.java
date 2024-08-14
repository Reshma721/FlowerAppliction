package com.floral.floralfiessy.service;

import com.floral.floralfiessy.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
        private final String secretKey = "eb0b5fb3cf704f88d21dc82b57b02afccd059d7bdc3dd5cdbe81926f693749c5";

        public String generateToken(User user) {
            return Jwts.builder()
                    .setSubject(user.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 24 hours
                    .signWith(getSignInKey())
                    .compact();
        }

        private SecretKey getSignInKey() {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            return Keys.hmacShaKeyFor(keyBytes);
        }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder() // Use parserBuilder for newer versions
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        }

        public boolean isTokenValid(String token, UserDetails userDetails) {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
        }

        public boolean isTokenExpired(String token) {
            return extractExpiration(token).before(new Date());
        }

        public Date extractExpiration(String token) {
            return extractClaim(token, Claims::getExpiration);
        }

        public String extractUsername(String token) {
            return extractClaim(token, Claims::getSubject);
        }
    }


