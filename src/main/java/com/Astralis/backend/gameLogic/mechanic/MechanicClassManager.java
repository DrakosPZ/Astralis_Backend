package com.Astralis.backend.gameLogic.mechanic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MechanicClassManager {
    @Bean
    public MovementManager movementManager() {
        return new MovementManager();
    }
}
