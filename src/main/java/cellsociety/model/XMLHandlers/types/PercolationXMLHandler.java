package cellsociety.model.XMLHandlers.types;

import java.util.HashMap;

import org.w3c.dom.Document;

import cellsociety.model.XMLHandlers.XMLHandler;
import cellsociety.model.simulation.rules.PercolationRules;
import cellsociety.model.simulation.types.Percolation;

public class PercolationXMLHandler extends XMLHandler {
    public PercolationXMLHandler(String xmlFilePath) {
        super(xmlFilePath);
    }

    @Override
    protected void parseParameters(Document doc) {
        myParameters = new HashMap<>();
    }
    
    @Override
    protected void setSim(){
        mySimRules = new PercolationRules();
        mySim = new Percolation(mySimRules, mySimData);
    }
}
