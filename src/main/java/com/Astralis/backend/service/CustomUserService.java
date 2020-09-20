package com.Astralis.backend.service;

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
    private PersonService personService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return personService.findByUsername(username)
                .map(this::getUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username: %s not found", username)));
    }

    private org.springframework.security.core.userdetails.User getUserDetails(PersonDto p) {
        return new org.springframework.security.core.userdetails.User(
                p.getUsername(),
                p.getPassword(),
                getGrantedAuthorities(p));
    }

    private List<GrantedAuthority> getGrantedAuthorities(PersonDto p) {
        //return AuthorityUtils
        //.commaSeparatedStringToAuthorityList(u.getUserCredentials().getRole());
        return Collections.emptyList();
    }
}
