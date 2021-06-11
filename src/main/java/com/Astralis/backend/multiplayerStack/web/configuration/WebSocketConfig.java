package com.Astralis.backend.multiplayerStack.web.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static String ENDPOINT = "/webgamesocket";
    private static String ALLOWED_ORIGIN_PATH = "http://localhost:4200";


    /**
     * Sets the web-socket address that will be used by the clients
     * to initialize the connection.
     * @param registry Registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
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
        registry.enableSimpleBroker("/topic/", "/queue/");
        registry.setApplicationDestinationPrefixes("/app");
    }
}
