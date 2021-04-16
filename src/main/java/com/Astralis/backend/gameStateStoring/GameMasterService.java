package com.Astralis.backend.gameStateStoring;

import com.Astralis.backend.accountManagement.model.GameState;
import com.Astralis.backend.accountManagement.model.GameStatus;
import com.Astralis.backend.accountManagement.model.UserGameState;
import com.Astralis.backend.gameLogic.model.Country;
import com.Astralis.backend.gameLogic.model.LogicGameState;
import com.Astralis.backend.gameLogic.model.Position;
import com.Astralis.backend.gameLogic.model.Ship;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class GameMasterService {

    private final static String FOLDER_BASE = "storage//gameState";

    //#TODO: Documentation
    public Optional<LogicGameState> loadGameStateFromDatabase(String gameStateStorageLink){
        LogicGameState LogicGameState = null;
        try{
            File[] files = new File(gameStateStorageLink).listFiles();

            FileInputStream fileIn =
                    new FileInputStream(getLastStoredGameStateOfFiles(files));
            ObjectInputStream objectIn =
                    new ObjectInputStream(fileIn);

            LogicGameState = (LogicGameState) objectIn.readObject();

            objectIn.close();
            fileIn.close();

        }catch (FileNotFoundException e) {
            System.err.println("File not found");
        } catch (IOException e) {
            System.err.println("Error initializing stream");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return Optional.of(LogicGameState);
    }

    //#TODO: Documentation
    public Optional<LogicGameState> storeGameStateToDatabase(LogicGameState logicGameState, String gameName){
        checkForGamesFolder(gameName);
        try {
            FileOutputStream fileOut =
                    new FileOutputStream(new File(getGameStorageLink(gameName) + "//" +
                            "GameState_" + logicGameState.getYear() + "_" + logicGameState.getMonth() + "_" + logicGameState.getDay() +  "_" + logicGameState.getHour() + ".txt"));
            ObjectOutputStream objectOut =
                    new ObjectOutputStream(fileOut);

            objectOut.writeObject(logicGameState);

            objectOut.close();
            fileOut.close();

        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        } catch (IOException e) {
            System.err.println("Error initializing stream");
        }

        return Optional.of(logicGameState);
    }

    //#TODO: Documentation
    public void initializeGameState(GameState gameState){
        //Test Data - replace with proper galaxy initialization
        String player1ID = "";
        String player2ID = "";
        List<UserGameState> list = gameState.getUserGameStates();
        for (UserGameState userGameState: list) {
            if(userGameState.getUser().getNickName().equals("DrakoD")){
                player1ID = userGameState.getUser().getIdentifier();
            }
            if(userGameState.getUser().getNickName().equals("KuroK")){
                player2ID = userGameState.getUser().getIdentifier();
            }
        }



        List<Country> countries = new ArrayList<>();
        countries.add(Country.builder()
                .id(1L)
                .name("Player1")
                .ship(Ship.builder()
                        .id(2L)
                        .currentPosition(new Position(0, 0))
                        .targetPosition(new Position(100, 100))
                        .movementSpeed(100)
                        .build())
                .owner(player1ID)
                .build());
        countries.add(Country.builder()
                .id(3L)
                .name("Player2")
                .ship(Ship.builder()
                        .id(4L)
                        .currentPosition(new Position(0, 0))
                        .targetPosition(new Position(-100, -100))
                        .movementSpeed(10)
                        .build())
                .owner(player2ID)
                .build());
        LogicGameState logicGameState = new LogicGameState(null, GameStatus.RUNNING,4000, 1, 1, 0, countries);

        storeGameStateToDatabase(logicGameState, gameState.getName());
        gameState.setGameStorageFolder(getGameStorageLink(gameState.getName()));
        gameState.setStatus(GameStatus.INITIALIZING);
    }

    //#TODO: Documentation
    private String getGameStorageLink(String gameName){
        return FOLDER_BASE + "//" + gameName;
    }

    //#TODO: Documentation
    private void checkForGamesFolder(String gameName){
        File dir = new File(getGameStorageLink(gameName));
        if(!dir.exists()){
            dir.mkdir();
        }
    }

    //#TODO: Documentation
    private File getLastStoredGameStateOfFiles(File[] files){
        File newestFile = files[0];
        for (File file : files) {
            long newestFileDate = getDateOfFileName(newestFile);
            long fileDate = getDateOfFileName(file);
            if(fileDate > newestFileDate){
                newestFile = file;
            }
        }
        return newestFile;
    }


    //#TODO: Documentation
    private long getDateOfFileName(File file){
        try {
            String[] name = file.getName().split("\\.")[0].split("_");
            long year = Long.parseLong(name[1]);
            long month = Long.parseLong(name[2]);
            long day = Long.parseLong(name[3]);
            long hour = Long.parseLong(name[4]);
            return ((year * 12 + month) * 30 + day) * 24 + hour;
        } catch (Exception e){
            //In case some other file is being stored here because of what ever reason, return -1 as the smallest possible value
            return -1;
        }
    }
}
