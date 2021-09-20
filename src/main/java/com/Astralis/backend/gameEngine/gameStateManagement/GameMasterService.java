package com.Astralis.backend.gameEngine.gameStateManagement;

import com.Astralis.backend.management.model.GameLobby;
import com.Astralis.backend.management.model.GameStatus;
import com.Astralis.backend.management.model.UserGameLobby;
import com.Astralis.backend.gameEngine.gameLogic.model.Country;
import com.Astralis.backend.gameEngine.gameLogic.model.LogicGameState;
import com.Astralis.backend.gameEngine.gameLogic.model.Position;
import com.Astralis.backend.gameEngine.gameLogic.model.Ship;
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
    private final GameInitializationService gameInitializationService;

    /**
     * A Method to load the stored gameFile out of the given folder's name.
     *
     * @param gameStateStorageLink The name of the folder where the gameFile is stored
     * @return The latest gameState (gameState with the furthest date).
     */
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

    /**
     * Method to turn logicGameState into a gameFile and stores it in the given gameName's folder.
     * The name of the file is written in a pattern like: "GameState_YEAR_MONTH_DAY_HOUR.txt".
     * Checks first if a folder for the given gameName already exists, if not it is created.
     *
     * @param logicGameState The to be stored logicGameState.
     * @param gameName The name of the folder where the file is supposed to be stored.
     * @return The stored logicGameState.
     */
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

    /**
     * A Method to initialize and handle the received new gameState, based of the given GameLobby.
     * <ol>
     *     <li>First calls initialization method</li>
     *     <li>Then stores finished LogicGameState to a GameFile. with the GameStates.Name as a Folder Name</li>
     *     <li>Then sets the same Storing folder name in the GameStates.</li>
     *     <li>Finally sets GameState Status to INITIALIZING.</li>
     * </ol>
     *
     * @param gameLobby The database's gameState reference used to link the logicGameState to the lobby.
     */
    public void setUpNewGameState(GameLobby gameLobby){
        LogicGameState logicGameState = gameInitializationService.initialize(gameLobby);

        storeGameStateToDatabase(logicGameState, gameLobby.getName());
        gameLobby.setGameStorageFolder(getGameStorageLink(gameLobby.getName()));
        gameLobby.setStatus(GameStatus.INITIALIZING);
    }

    /**
     * Formulates the link to where the gameState Files are being stored:
     *      storage/gameState/Given_Game_Name
     *
     * @param gameName the given game Name
     * @return the full link as String
     */
    private String getGameStorageLink(String gameName){
        return FOLDER_BASE + "//" + gameName;
    }

    /**
     * Checks the given gameName for if a folder is already present in the game storing folder.
     * If not the folder with the game name is created to store further game states.
     *
     * @param gameName the game State's name to be checked
     */
    private void checkForGamesFolder(String gameName){
        File dir = new File(getGameStorageLink(gameName));
        if(!dir.exists()){
            dir.mkdir();
        }
    }

    /**
     * Gets a List of Game State Files and checks which of the files is the newest in therms of the in game date.
     * It determines this by forwarding the file to the getDateOfFileName() Method, returning the date in game hours.
     * It then checks if the currently iterated file has an older game date than the currently newest file,
     *      if so, the it is stored as the currently newest file.
     * which is then returned at the end.
     *
     * @param files a List of Game State files which have to be looked through.
     * @return the newest Game State file out of the given List.
     */
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


    /**
     * The given game state file's name is read and then split into, game-years, -months, -days, and -hours,
     * <ol>
     *      <li>years are turned to months by x 12</li>
     *      <li>months are turned to days by x 30</li>
     *      <li>days are turned to hours by x 24</li>
     *      <li>hours are the smallest amount of time unit in game and are then returned</li>
     * </ol>
     *
     * In case an exception is thrown when the name is read and calculated,
     *  -1 is returned.
     *
     * @param file of which the date should be retrieved.
     * @return the date in the smallest possible game date unit (days).
     */
    private long getDateOfFileName(File file){
        try {
            String[] name = file.getName().split("\\.")[0].split("_");
            long year = Long.parseLong(name[1]);
            long month = Long.parseLong(name[2]);
            long day = Long.parseLong(name[3]);
            long hour = Long.parseLong(name[4]);
            return ((year * 12 + month) * 30 + day) * 24 + hour;
        } catch (Exception e){
            //In case some other file is being stored here because of what ever reason,
            // return -1 as the smallest possible value.
            return -1;
        }
    }
}
