package cellsociety.model.XMLHandlers.types;

import java.util.HashMap;

import org.w3c.dom.Document;

import cellsociety.model.XMLHandlers.XMLHandler;
import cellsociety.model.simulation.rules.GameOfLifeRules;
import cellsociety.model.simulation.types.GameOfLife;

public class GameOfLifeXMLHandler extends XMLHandler {
    public GameOfLifeXMLHandler(String xmlFilePath) {
        super(xmlFilePath);
    }

    @Override
    protected void parseParameters(Document doc) {
        myParameters = new HashMap<>();
    }
    
    @Override
    protected void setSim(){
        mySimRules = new GameOfLifeRules();
        mySim = new GameOfLife(mySimRules, mySimData);
    }
}
