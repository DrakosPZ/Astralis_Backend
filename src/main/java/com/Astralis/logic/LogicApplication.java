package com.Astralis.logic;

import com.Astralis.backend.BackendApplication;
import com.Astralis.logic.mechanic.GameLoop;
import com.Astralis.logic.model.Country;
import com.Astralis.logic.model.LogicGameState;
import com.Astralis.logic.model.Position;
import com.Astralis.logic.model.Ship;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class LogicApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogicApplication.class, args);

		GameLoop gameLoop = new GameLoop();

		List<Country> countries = new ArrayList<>();
		countries.add(Country.builder()
						.name("Player1")
						.ship(Ship.builder()
								.currentPosition(new Position(0, 0))
								.targetPosition(new Position(100, 100))
								.movementSpeed(100)
								.build())
						.build());
		countries.add(Country.builder()
				.name("Player2")
				.ship(Ship.builder()
						.currentPosition(new Position(0, 0))
						.targetPosition(new Position(-100, -100))
						.movementSpeed(10)
						.build())
				.build());

		LogicGameState logicGameState = new LogicGameState(4000, 1, 1, 0, countries);

		gameLoop.startLoop(logicGameState, new SseEmitter());
	}

}