package com.Astralis.backend.gameLogic;

import com.Astralis.backend.accountManagement.model.GameStatus;
import com.Astralis.backend.multiplayerStack.logicLoop.GameLoop;
import com.Astralis.backend.gameLogic.model.Country;
import com.Astralis.backend.gameLogic.model.LogicGameState;
import com.Astralis.backend.gameLogic.model.Position;
import com.Astralis.backend.gameLogic.model.Ship;
import com.Astralis.backend.accountManagement.model.GameState;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;

;

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

		GameState gameState = new GameState();
		gameState.setIdentifier("GID0");

		LogicGameState LogicGameState = new LogicGameState(gameState, GameStatus.RUNNING, 4000, 1, 1, 0, countries);

		gameLoop.startLoop("GID0", LogicGameState, new SseEmitter());
	}

}