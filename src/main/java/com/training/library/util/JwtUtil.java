package com.training.library.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "secret";

    public String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(Instant.now().plus(10, ChronoUnit.MINUTES))
                .sign(getAlgorithm());
    }

    public DecodedJWT decodeJwt(String token) {
        JWTVerifier verifier = JWT.require(getAlgorithm()).build();
        return verifier.verify(token);
    }

    public String extractUsername(String token) {
        return decodeJwt(token).getSubject();
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(SECRET_KEY);
    }
}
