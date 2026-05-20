package com.agroconnect.payment.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenParser {

    private final SecretKey signingKey;

    public JwtTokenParser(JwtProperties properties) {
        this.signingKey = Keys.hmacShaKeyFor(properties.secret().getBytes(StandardCharsets.UTF_8));
    }

    public TokenUser parse(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String email = claims.getSubject();
        Object uid = claims.get("uid");
        String role = String.valueOf(claims.get("role"));
        Long userId = uid == null ? null : Long.parseLong(uid.toString());
        return new TokenUser(userId, email, role);
    }
}
