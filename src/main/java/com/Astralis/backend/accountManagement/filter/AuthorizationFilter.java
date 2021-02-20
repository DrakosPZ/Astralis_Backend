package com.Astralis.backend.accountManagement.filter;

import com.Astralis.backend.accountManagement.configuration.TokenProperties;
import com.Astralis.backend.accountManagement.dto.LoginInformationDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AuthorizationFilter extends OncePerRequestFilter {

    private final TokenProperties tokenProperties;

    public AuthorizationFilter(TokenProperties tokenProperties) {
        this.tokenProperties = tokenProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain
    ) throws ServletException, IOException {
        String header = httpServletRequest.getHeader(tokenProperties.getHeader());

        if (headerIsValid(header)) {
            try {
                Claims claims = getClaims(getToken(header));
                Optional.ofNullable(claims.getSubject())
                        .ifPresent(username -> setUserContext(claims, username));
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
        }

        goToNextFilter(httpServletRequest, httpServletResponse, filterChain);
    }

    private boolean headerIsValid(String header) {
        return header != null && header.startsWith(tokenProperties.getPrefix());
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(tokenProperties.getSecret().getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

    private String getToken(String header) {
        return header.replace(tokenProperties.getPrefix(), "");
    }

    private void setUserContext(Claims claims, String loginName) {
        LoginInformationDTO userDetails = LoginInformationDTO.builder().loginName(loginName).password("").build();
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                getGrantedAuthorities(claims)
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @SuppressWarnings("unchecked")
    private List<SimpleGrantedAuthority> getGrantedAuthorities(Claims claims) {
        return ((List<String>) claims.get("authorities")).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private void goToNextFilter(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}