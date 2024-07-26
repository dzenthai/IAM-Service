package edu.iam.service.service.authentication;

import edu.iam.service.utils.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.key}")
    private String SECRET_KEY;

    @Value("${jwt.lifetime}")
    private int TOKEN_LIFETIME;

    public String generateToken(CustomUserDetails userDetails) {

        log.info("JwtService | Generate token for user: {}", userDetails.getUsername());

        Map<String, Object> claims = new HashMap<>();
        List<String> rolesList = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put("roles", rolesList);
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claims(claims)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(TOKEN_LIFETIME)))
                .signWith(generateSignature())
                .compact();
    }

    private SecretKey generateSignature() {
        byte[] signature = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(signature);
    }

    public String extractUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public List<String> extractRoleFromToken(String token) {
        return getClaims(token).get("roles", List.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(generateSignature())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
