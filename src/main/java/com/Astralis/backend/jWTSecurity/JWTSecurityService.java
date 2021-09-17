package com.Astralis.backend.jWTSecurity;

import com.Astralis.backend.management.configuration.TokenProperties;
import com.Astralis.backend.management.dto.LoginInformationDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//TODO: Document
@Service
@RequiredArgsConstructor
public class JWTSecurityService {
    private final TokenProperties tokenProperties;

    //TODO: Document
    public void authorizeHTTP(HttpServletRequest httpServletRequest){
        String header = httpServletRequest.getHeader(tokenProperties.getHeader());

        if (headerIsValid(header)) {
            try {
                Claims claims = getClaims(getToken(header));
                Optional.ofNullable(claims.getSubject())
                        .ifPresent(username -> setUserContext(claims, username));
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
                throw new IllegalArgumentException("Authorization Denied!");
            }
        }
    }

    //TODO: Document
    public void authorizeWS(StompHeaderAccessor accessor){
        String header = accessor.getFirstNativeHeader(tokenProperties.getHeader());
        if (headerIsValid(header)) {
            try {
                Claims claims = getClaims(getToken(header));
                Optional.ofNullable(claims.getSubject())
                        .ifPresent((username) -> {
                            UsernamePasswordAuthenticationToken user = setUserContext(claims, username);
                            accessor.setUser(user /*new WsToken(header)*/);
                        });
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
                throw new IllegalArgumentException("Authorization Denied!");
            }
        } else {
            SecurityContextHolder.clearContext();
            throw new IllegalArgumentException("Authorization Denied!");
        }
    }

    /*
    //TODO: Currently not in use, if stays this way till 0.5.x remove class and asscoaiated comments
    public void authorizeWSUser(UsernamePasswordAuthenticationToken token){
        String header = token.getName();
        if (headerIsValid(header)) {
            try {
                //Jumps out at Claims if wrong, maybe that's the check?
                Claims claims = getClaims(getToken(header));
                Optional.ofNullable(claims.getSubject())
                        .ifPresent((username) -> {
                            setUserContext(claims, username);
                        });
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
                System.err.println(e.getMessage());
                throw new IllegalArgumentException("Authorization Denied!");
            }
        } else {
            SecurityContextHolder.clearContext();
            throw new IllegalArgumentException("Authorization Denied!");
        }
    }*/


    //TODO: Document
    private boolean headerIsValid(String header) {
        return header != null && header.startsWith(tokenProperties.getPrefix());
    }

    //TODO: Document
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(tokenProperties.getSecret().getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

    //TODO: Document
    private String getToken(String header) {
        return header.replace(tokenProperties.getPrefix(), "");
    }

    //TODO: Document
    private UsernamePasswordAuthenticationToken setUserContext(Claims claims, String loginName) {
        LoginInformationDTO userDetails = LoginInformationDTO.builder().loginName(loginName).password("").build();
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                getGrantedAuthorities(claims)
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        return auth;
    }

    //TODO: Document
    @SuppressWarnings("unchecked")
    private List<SimpleGrantedAuthority> getGrantedAuthorities(Claims claims) {
        return ((List<String>) claims.get("authorities")).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}
