package cellsociety.model.XMLHandlers.types;

import java.util.HashMap;

import org.w3c.dom.Document;

import cellsociety.model.XMLHandlers.XMLHandler;
import cellsociety.model.simulation.rules.GameOfLifeRules;
import cellsociety.model.simulation.types.GameOfLife;

/**
* Properly updates Game of Life Simulations with the right Simulation and SimulationRules objects
* after parsing the XML for universal Simulation data
*/
public class GameOfLifeXMLHandler extends XMLHandler {

    /**
    * GameOfLifeXMLHandler constructor that collects the universal sim data
    */
    public GameOfLifeXMLHandler(String xmlFilePath) {
        super(xmlFilePath);
    }

    /**
    * Handles the parameters for Game of Life (none)
    */
    @Override
    protected void parseParameters(Document doc) {
        myParameters = new HashMap<>();
    }
    
    /**
    * Properly updates SimRules and Sim objects for Game of Life Sim
    */
    @Override
    protected void setSim(){
        mySimRules = new GameOfLifeRules();
        mySim = new GameOfLife(mySimRules, mySimData);
    }
}
