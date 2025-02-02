package cellsociety.model.XMLHandlers.types;

import java.util.HashMap;

import org.w3c.dom.Document;

import cellsociety.model.XMLHandlers.XMLHandler;
import cellsociety.model.simulation.rules.PercolationRules;
import cellsociety.model.simulation.types.Percolation;

/**
* Properly updates Percolation Simulations with the right Simulation and SimulationRules objects
* after parsing the XML for universal Simulation data
*/
public class PercolationXMLHandler extends XMLHandler {

    /**
    * PercolationXMLHandler constructor that collects the universal sim data
    */
    public PercolationXMLHandler(String xmlFilePath) {
        super(xmlFilePath);
    }

    /**
    * Handles the parameters for Percolation Simulation (none)
    */
    @Override
    protected void parseParameters(Document doc) {
        myParameters = new HashMap<>();
    }

    /**
    * Properly updates SimRules and Sim objects for Percolation Sim
    */
    @Override
    protected void setSim(){
        mySimRules = new PercolationRules();
        mySim = new Percolation(mySimRules, mySimData);
    }
}
