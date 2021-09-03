package com.Astralis.backend.management.service;

import com.Astralis.backend.management.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CustomUserService implements UserDetailsService {
    @Autowired
    private UserService userService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.findByLoginName(username)
                .map(this::getUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username: %s not found", username)));
    }

    private org.springframework.security.core.userdetails.User getUserDetails(UserDTO u) {
        return new org.springframework.security.core.userdetails.User(
                u.getLoginInformation().getLoginName(),
                u.getLoginInformation().getPassword(),
                getGrantedAuthorities(u));
    }

    private List<GrantedAuthority> getGrantedAuthorities(UserDTO u) {
        //return AuthorityUtils
        //.commaSeparatedStringToAuthorityList(u.getUserCredentials().getRole());
        return Collections.emptyList();
    }
}
