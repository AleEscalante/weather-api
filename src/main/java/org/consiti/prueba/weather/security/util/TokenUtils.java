package org.consiti.prueba.weather.security.util;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import io.jsonwebtoken.Jwts;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.consiti.prueba.weather.security.dto.JwtDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class TokenUtils {
    private static final String SECRET = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkFsZWphbmRybyIsImlhdCI6MTUxNjIzOTAyMn0.t74JoOr2rkmMDi-DcnDuPexfZ8wEXIU3W-tvmgsbAyA";

    private static final Long TOKEN_EXPIRATION = 60L; // 60 seg

    private static final Logger log = LoggerFactory.getLogger(TokenUtils.class);

    public static String createToken(String username, String email) {
        long expirationTime = TOKEN_EXPIRATION * 1_000;
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

        Map<String, Object> extra = new HashMap<>();
        extra.put("username", username);

        log.info("Initial life: " + expirationDate);
        log.info("Subject: " + email);
        log.info("Username: " + extra);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .addClaims(extra)
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact();
    }

    public static UsernamePasswordAuthenticationToken getAuthentication(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET.getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();
            return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
        } catch (JwtException e) {
            return null;
        }
    }

    public static String refreshToken(JwtDto jwtDto) throws ParseException {
        try {
            Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtDto.getToken());
        } catch (ExpiredJwtException e) {
            long expirationTime = TOKEN_EXPIRATION * 1_000;
            Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);


            JWT jwt = JWTParser.parse(jwtDto.getToken());
            JWTClaimsSet claims = jwt.getJWTClaimsSet();
            Map<String, Object> extra = new HashMap<>(claims.getClaims());
            String email = claims.getSubject();

            log.info("Refresh life: " + expirationDate);
            log.info("Subject: " + email);
            log.info("Username: " + extra);

            return Jwts.builder()
                    .setSubject(email)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(expirationDate)
                    .addClaims(extra)
                    .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                    .compact();
        }

        return null;
    }
}
