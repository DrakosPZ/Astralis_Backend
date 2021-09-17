package com.Astralis.backend.jWTSecurity;

import javax.security.auth.Subject;
import java.security.Principal;

//TODO: Currently not in use, if stays this way till 0.5.x remove class and asscoaiated comments
public class WsToken implements Principal {
    private String token;

    public WsToken(String token){
        this.token = token;
    }

    @Override
    public String getName() {
        return token;
    }

    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }
}
