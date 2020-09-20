package com.Astralis.backend.filter;

import com.Astralis.backend.configuration.TokenProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;
    private final TokenProperties tokenProperties;

    public AuthenticationFilter(AuthenticationManager authenticationManager, TokenProperties tokenProperties) {
        objectMapper = new ObjectMapper();
        this.authenticationManager = authenticationManager;
        this.tokenProperties = tokenProperties;
        setLoginPath(tokenProperties);
    }

    private void setLoginPath(TokenProperties tokenProperties) {
        setRequiresAuthenticationRequestMatcher(
                new AntPathRequestMatcher(tokenProperties.getLoginPath(), "POST"));
    }
    //#!Update
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            PersonDto credentials = getCredentials(request);
            UsernamePasswordAuthenticationToken token = createAuthenticationToken(credentials);
            return authenticationManager.authenticate(token);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //#!Update
    private PersonDto getCredentials(HttpServletRequest request) throws IOException {
        Map<String, String> holder = objectMapper.readValue(request.getInputStream(), HashMap.class);
        String username = holder.keySet().stream()
                .map(key -> key)
                .collect(Collectors.toList()).get(0);
        String password = holder.keySet().stream()
                .map(key -> holder.get(key))
                .collect(Collectors.toList()).get(0);
        return PersonDto.builder().username(username).password(password).build();
    }
    //#!Update
    private UsernamePasswordAuthenticationToken createAuthenticationToken(PersonDto credentials) {
        return new UsernamePasswordAuthenticationToken(
                credentials.getUsername(),
                credentials.getPassword(),
                new ArrayList<>()
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth) {
        response.addHeader(tokenProperties.getHeader(), tokenProperties.getPrefix() + createToken(auth));
    }
    //#!Update
    private String createToken(Authentication auth) {
        long now = System.currentTimeMillis();
        List<String> authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return Jwts.builder()
                .setSubject(auth.getName())
                .claim("authorities", authorities)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + tokenProperties.getExpiration() * 1000))
                .signWith(SignatureAlgorithm.HS512, tokenProperties.getSecret().getBytes())
                .compact();
    }
}
