package com.Astralis.backend.management.filter;

import com.Astralis.backend.management.configuration.TokenProperties;
import com.Astralis.backend.management.dto.LoginInformationDTO;
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

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            LoginInformationDTO credentials = getCredentials(request);
            UsernamePasswordAuthenticationToken token = createAuthenticationToken(credentials);
            return authenticationManager.authenticate(token);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private LoginInformationDTO getCredentials(HttpServletRequest request) throws IOException {
        Map<String, String> holder = objectMapper.readValue(request.getInputStream(), HashMap.class);
        String loginName = holder.keySet().stream()
                .map(key -> key)
                .collect(Collectors.toList()).get(0);
        String password = holder.keySet().stream()
                .map(key -> holder.get(key))
                .collect(Collectors.toList()).get(0);
        return LoginInformationDTO.builder().loginName(loginName).password(password).build();
    }

    private UsernamePasswordAuthenticationToken createAuthenticationToken(LoginInformationDTO credentials) {
        return new UsernamePasswordAuthenticationToken(
                credentials.getLoginName(),
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
