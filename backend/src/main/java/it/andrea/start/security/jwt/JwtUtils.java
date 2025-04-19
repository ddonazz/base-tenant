package it.andrea.start.security.jwt;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import it.andrea.start.security.service.JWTokenUserDetails;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtils {

    private static final Logger LOG = LoggerFactory.getLogger(JwtUtils.class);

    @Value(value = "${jwt.secret}")
    private String jwtSecret;

    @Value(value = "${jwt.expiration.days}")
    private int jwtExpirationDays;

    private SecretKey secretKey;

    @PostConstruct
    protected void init() {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Authentication authentication) {
        JWTokenUserDetails userPrincipal = (JWTokenUserDetails) authentication.getPrincipal();

        Instant now = Instant.now();
        Instant expiration = now.plus(jwtExpirationDays, ChronoUnit.DAYS);

        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .claim("authorities", userPrincipal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }

    public Optional<JWTokenUserDetails> validateAndParseToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String username = claims.getSubject();
            List<?> rawAuthorities = claims.get("authorities", List.class);
            List<String> authorities = rawAuthorities.stream()
                    .map(String::valueOf)
                    .toList();

            return Optional.of(new JWTokenUserDetails.Builder()
                    .username(username)
                    .password("")
                    .authorities(authorities.stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList())
                    .build());

        } catch (ExpiredJwtException ex) {
            LOG.warn("JWT expired: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            LOG.warn("Invalid JWT: {}", ex.getMessage());
        } catch (JwtException | IllegalArgumentException ex) {
            LOG.warn("JWT error: {}", ex.getMessage());
        }

        return Optional.empty();
    }

}