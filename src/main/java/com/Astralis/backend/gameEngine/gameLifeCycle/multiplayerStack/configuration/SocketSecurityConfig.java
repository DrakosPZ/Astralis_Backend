package com.Astralis.backend.gameEngine.gameLifeCycle.multiplayerStack.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

/**
 * AbstractSecurityWebSocketMessageBrokerConfigurer just so I keep this in mind
 * I assume this extended class is what actually checks the websockets associated user if it is authenticated or not
 * as before I implemented this, I had to manually check the token I stored every time a message got in
 * and as I included this again, it started throwing access denied errors, that only went away after I associated the
 * UsernameAnPasswordJWT Token class with the user websocket instead of the token. which then also meant I don't have to
 * check it afterwards again with SEND and SUBSCRIBE messages.
 */
@Configuration
public class SocketSecurityConfig
        extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .simpDestMatchers("/ws/**").authenticated()
                .anyMessage().authenticated();
    }

    //csrf disabled
    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }


}
