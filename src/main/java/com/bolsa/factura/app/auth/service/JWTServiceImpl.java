package com.bolsa.factura.app.auth.service;

import com.bolsa.factura.app.auth.SimpleGrantedAuthorityMixin;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Component
public class JWTServiceImpl implements JWTService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public String create(Authentication authResult) throws JsonProcessingException {
        Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();

        Claims claims = Jwts.claims();
        claims.put("authorities", new ObjectMapper().writeValueAsString(roles));

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(authResult.getName())
                .signWith(SECRET_KEY)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME  )) //Para una hora
                .compact();

        return token;
    }

    @Override
    public boolean validate(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException e) {
            logger.error(String.format("Error validating token: %s", token), e);
            return false;
        }
    }

    @Override
    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(resolveToken(token))
                .getBody();

    }

    @Override
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    @Override
    public Collection<? extends GrantedAuthority> getRoles(String token) throws IOException {
        Object roles = getClaims(token).get("authorities");

        Collection<? extends GrantedAuthority> authorities =
                Arrays.asList(
                        new ObjectMapper()
                                .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class)
                                .readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class));

        return authorities;
    }

    @Override
    public String resolveToken(String token) {

        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            return token.replace(TOKEN_PREFIX, "");
        }

        return null;

    }
}
