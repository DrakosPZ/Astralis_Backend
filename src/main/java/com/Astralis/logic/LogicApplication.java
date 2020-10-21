package com.Astralis.logic;

import com.Astralis.logic.mechanic.GameLoop;
import com.Astralis.logic.model.LogicGameState;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class LogicApplication {

	public static void main(String[] args) {
		GameLoop gameLoop = new GameLoop();
		LogicGameState logicGameState = new LogicGameState(4000, 1, 1, 0, new ArrayList<>());
		gameLoop.startLoop(logicGameState);
	}

}