package com.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    public static final String SECRET_KEY="jvgfhgsbafuybghfdjgbfdkglerygbeoryfbelygvbygbgfkgblvdfha";

    public String generateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        claims.put("email","riyaz@gmail.com");

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() * 1000 * 60 * 30))
                .claims(claims)
                .signWith(getKey())
                .compact();


    }

    private Key getKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    private Claims getClaims(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Date extractExpiration(String token){
        return getClaims(token).getExpiration();
    }
}
