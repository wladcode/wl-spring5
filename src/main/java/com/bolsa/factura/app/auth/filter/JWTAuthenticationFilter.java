package com.bolsa.factura.app.auth.filter;

import com.bolsa.factura.app.auth.service.JWTService;
import com.bolsa.factura.app.models.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private JWTService jwtService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        if (username != null && password != null) {
            logger.info("USERNAME - PASSWORD: " + username + " - " + password);
        } else {
            User user = null;
            try {
                user = new ObjectMapper().readValue(request.getInputStream(), User.class);
                username = user.getUsername();
                password = user.getPassword();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        username = username.trim();
        UsernamePasswordAuthenticationToken autToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(autToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        String token = jwtService.create(authResult);

        Map<String, Object> bodyResponse = new HashMap<>();
        bodyResponse.put("token", token);
        bodyResponse.put("user", authResult.getPrincipal());
        bodyResponse.put("message", String.format("Welcome %s", authResult.getName()));

        response.addHeader("Authorization", JWTService.TOKEN_PREFIX + token);
        response.getWriter().write(new ObjectMapper().writeValueAsString(bodyResponse));
        response.setStatus(200);
        response.setContentType("application/json");

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        Map<String, Object> bodyResponse = new HashMap<>();
        bodyResponse.put("message", "Usuario o clave incorrecta");
        bodyResponse.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(bodyResponse));
        response.setStatus(401);
        response.setContentType("application/json");

    }
}
