package com.Astralis.backend.gameStateStoring;

import com.Astralis.backend.gameLogic.model.LogicGameState;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.*;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class GameMasterService {

    private final static String FOLDER_BASE = "storage//gameState";

    public Optional<LogicGameState> loadGameStateFromDatabase(long id){
        LogicGameState LogicGameState = null;
        try{

            FileInputStream fileIn =
                    new FileInputStream(new File(FOLDER_BASE + "//" + "TestGame" + "//" + "obj.txt"));
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

    public Optional<LogicGameState> storeGameStateToDatabase(LogicGameState LogicGameState){
        checkForGamesFolder("TestGame");
        try {

            FileOutputStream fileOut =
                    new FileOutputStream(new File(FOLDER_BASE + "//" + "TestGame" + "//" + "obj.txt"));
            ObjectOutputStream objectOut =
                    new ObjectOutputStream(fileOut);

            objectOut.writeObject(LogicGameState);

            objectOut.close();
            fileOut.close();

        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        } catch (IOException e) {
            System.err.println("Error initializing stream");
        }

        return Optional.of(LogicGameState);
    }

    private void checkForGamesFolder(String gameName){
        File dir = new File(FOLDER_BASE + "//" + gameName);
        if(!dir.exists()){
            dir.mkdir();
        }
    }
}
