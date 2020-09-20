package com.Astralis.backend.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class TokenProperties {

    private String loginPath = "/user/login";
    private String registrationPath = "/user/register";
    private String logoutPath = "/user/logout";

    private String header = "Authorization";

    private String prefix = "Bearer ";

    private int expiration = 43200; //in Seconds 43200 = 12h

    private String secret = "6B7fZsI!ORgiBÜrlhYK9?p";
}
