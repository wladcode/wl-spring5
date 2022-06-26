package com.bolsa.factura.app.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.security.Key;
import java.util.Collection;

public interface JWTService {
    public static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    public static final long EXPIRATION_TIME = 3600000L;
    public static final String TOKEN_PREFIX ="Bearer ";

    public String create(Authentication auth) throws JsonProcessingException;

    public boolean validate(String token);

    public Claims getClaims(String token);

    public String getUsername(String token);

    public Collection<? extends GrantedAuthority> getRoles(String token) throws IOException;

    public String resolveToken(String token);

}
