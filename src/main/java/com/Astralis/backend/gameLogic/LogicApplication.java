package com.Astralis.backend.gameLogic;

import com.Astralis.backend.gameLogic.mechanic.GameLoop;
import com.Astralis.backend.gameLogic.model.mCountry;
import com.Astralis.backend.gameLogic.model.mLogicGameState;
import com.Astralis.backend.gameLogic.model.mPosition;
import com.Astralis.backend.gameLogic.model.mShip;
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

		List<mCountry> countries = new ArrayList<>();
		countries.add(mCountry.builder()
						.name("Player1")
						.mShip(mShip.builder()
								.currentMPosition(new mPosition(0, 0))
								.targetMPosition(new mPosition(100, 100))
								.movementSpeed(100)
								.build())
						.build());
		countries.add(mCountry.builder()
				.name("Player2")
				.mShip(mShip.builder()
						.currentMPosition(new mPosition(0, 0))
						.targetMPosition(new mPosition(-100, -100))
						.movementSpeed(10)
						.build())
				.build());

		GameState gameState = new GameState();
		gameState.setIdentifier("GID0");

		mLogicGameState mLogicGameState = new mLogicGameState(gameState, 4000, 1, 1, 0, countries);

		gameLoop.startLoop("GID0", mLogicGameState, new SseEmitter());
	}

}