package com.Astralis.backend.gameDatabase;

import com.Astralis.backend.accountManagement.dto.GameStateDTO;
import com.Astralis.backend.accountManagement.model.GameState;
import com.Astralis.backend.gameDatabase.model.LogicGameState;
import com.Astralis.backend.gameDatabase.service.CountryService;
import com.Astralis.backend.gameDatabase.service.LogicGameStateService;
import com.Astralis.backend.gameDatabase.service.ShipService;
import com.Astralis.backend.gameLogic.model.mLogicGameState;
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

    private final LogicGameStateService logicGameStateService;
    private final CountryService countryService;
    private final ShipService shipService;

    private final

    /**
     * Convert a given Model into a respective Memory
     *
     * @param model to be transformed
     * @return transformed Memory
     */
    mLogicGameState convertModelIntoDTO(LogicGameState model) {
        return new mLogicGameState(model);
    }

    /**
     * Convert a given Memory into a respective Model
     *
     * @param memory to be transformed
     * @return transformed model
     */
    LogicGameState convertDTOIntoModel(mLogicGameState memory) {
        LogicGameState model = new LogicGameState(memory);
        return model;
    }


    /**
     * Returns a searched for object by the given identifier.
     *
     * @param id of the to find object
     * @return the looked for object
     */
    public Optional<mLogicGameState> findModelById(long id) {
        return logicGameStateService.findModelById(id);
    }

    /**
     * Deletes and returns a given object afterwards.
     *
     * @param id of the to be deleted object
     * @return true if successful
     */
    public boolean deleteModelById(long id) {
        //preDeleteCleanUp(id);
        //deleteByIdentifier(id);
        return findModelById(id).isEmpty();
    }

    /**
     * Calls the delete by identifier method of the respective repo
     *
     * @param id the to be deleted model's identifier
     */
    void deleteById(long id) {
        logicGameStateService.deleteModelById(id);
    }


    public Optional<mLogicGameState> loadGameStateFromDatabase(long id){
        mLogicGameState mLogicGameState = null;

        try{

            FileInputStream fi = new FileInputStream(new File("obj.txt"));
            ObjectInputStream oi = new ObjectInputStream(fi);

            mLogicGameState = (mLogicGameState) oi.readObject();
            oi.close();
            fi.close();

        }catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return Optional.of(mLogicGameState);
    }

    public Optional<mLogicGameState> storeGameStateToDatabase(mLogicGameState mLogicGameState){
        //LogicGameState logicGameState = convertDTOIntoModel(mLogicGameState);

        //#TODO: Add the actual translating, checking and then storing part here.
        /*if(mLogicGameState.getId() != null){
            //ID set - update
            logicGameStateService.update(Optional.of(mLogicGameState));
        } else {
            // ID empty - save
            logicGameStateService.save(Optional.of(mLogicGameState));
        }*/

        try {

            FileOutputStream fileOut = new FileOutputStream(new File("obj.txt"));
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(mLogicGameState);
            objectOut.close();
            fileOut.close();
            System.out.println("The Object  was succesfully written to a file");

        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return Optional.of(mLogicGameState);
    }

}
