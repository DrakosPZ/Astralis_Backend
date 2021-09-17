package com.Astralis.backend.gameEngine.gameLifeCycle.multiplayerStack.configuration;

import com.Astralis.backend.jWTSecurity.JWTSecurityService;
import com.Astralis.backend.jWTSecurity.WsToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    public static String ENDPOINT = "/ws";
    public static String ALLOWED_ORIGIN_PATH = "http://localhost:4200";

    @Autowired
    private JWTSecurityService jwtSecurityService;


    /**
     * Sets the web-socket address that will be used by the clients
     * to initialize the connection.
     * @param registry Registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(ENDPOINT)
                .setAllowedOrigins(ALLOWED_ORIGIN_PATH);
        registry.addEndpoint(ENDPOINT)
                .setAllowedOrigins(ALLOWED_ORIGIN_PATH)
                .withSockJS();
    }

    /**
     * Sets the web-socket connection prefix. And the destination prefix.
     * @param registry MessageBrokerRegistry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic/");
        registry.setApplicationDestinationPrefixes("/app");
    }



    // try to work out how to implement websocket security manual JWT checks based on this guys implementation
    // https://github.com/stomp-js/ng2-stompjs/issues/83#issuecomment-421210171
    // https://stackoverflow.com/questions/44852776/spring-mvc-websockets-with-stomp-authenticate-against-specific-channels
    // https://github.com/roaraurora/zhihu/blob/master/src/main/java/com/zhihu/demo/websocket/InBoundChannelInterceptor.java

    //this is more important
    // https://github.com/spring-projects/spring-framework/blob/main/src/docs/asciidoc/web/websocket.adoc#token-authentication

    //TODO: Document
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                StompCommand command = accessor != null ? accessor.getCommand() : null;
                if (command != null) {
                    //On Connection Handshake, header is checked for JWT, if authorized, UsernamePasswordToken is set
                    // as session user.
                    if (StompCommand.CONNECT.equals(command)) {
                        jwtSecurityService.authorizeWS(accessor);
                    } else {
                        //Subscribe and Message Send is already set with session user so only that one has to be checked
                        UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) accessor.getUser();
                        if(user != null){
                            if(StompCommand.SUBSCRIBE.equals(command)){
                                //TODO: Make StackOverflow Question Is Authenticated Flag to be trusted when it is returned by the user?
                                // does the Method have a checksum or something similar to validify itself?
                                // And if not is there a way to check if the token is valid or not without registering it again?
                                // Am I registering it again?
                                //user.isAuthenticated();
                                //jwtSecurityService.authorizeWSUser(user);
                            }
                            if(StompCommand.SEND.equals(command)){
                                //jwtSecurityService.authorizeWSUser(user);
                            }
                        }
                    }
                }
                return message;
            }
        });
    }
}
