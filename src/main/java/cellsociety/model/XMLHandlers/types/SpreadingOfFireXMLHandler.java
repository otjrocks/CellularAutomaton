package cellsociety.model.XMLHandlers.types;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cellsociety.model.XMLHandlers.XMLHandler;
import cellsociety.model.simulation.rules.SpreadingOfFireRules;
import cellsociety.model.simulation.types.SpreadingOfFire;

/**
* Properly updates Spreading of Fire Simulations with the right Simulation and SimulationRules objects
* after parsing the XML for universal Simulation data
*/
public class SpreadingOfFireXMLHandler extends XMLHandler {

    /**
    * SpreadingOfFireXMLHandler constructor that collects the universal sim data
    */
    public SpreadingOfFireXMLHandler(String xmlFilePath) {
        super(xmlFilePath);
    }

    /**
    * Collects the necessary parameters for Spreading of Fire Simulation (ignitionWithoutNeighbors and growInEmptyCell)
    */
    @Override
    protected void parseParameters(Document doc) {
        myParameters = new HashMap<>();
        NodeList params = doc.getElementsByTagName("Parameters");
        if (params.getLength() > 0) {
            Node param = params.item(0);
            if (param.getNodeType() == Node.ELEMENT_NODE) {
                Element paramElement = (Element) param;
                
                if (paramElement.getElementsByTagName("ignitionWithoutNeighbors").getLength() > 0) {
                    try {
                        double ignitionWithoutNeighbors = Double.parseDouble(paramElement.getElementsByTagName("ignitionWithoutNeighbors").item(0).getTextContent());
                        myParameters.put("ignitionWithoutNeighbors", ignitionWithoutNeighbors);
                    } catch (NumberFormatException e) {
                        System.err.println("Warning: Invalid parameter value. Defaulting to 0.15.");
                        myParameters.put("ignitionWithoutNeighbors", 0.15);
                    }
                }

                if (paramElement.getElementsByTagName("growInEmptyCell").getLength() > 0) {
                    try {
                        double growInEmptyCell = Double.parseDouble(paramElement.getElementsByTagName("growInEmptyCell").item(0).getTextContent());
                        myParameters.put("growInEmptyCell", growInEmptyCell);
                    } catch (NumberFormatException e) {
                        System.err.println("Warning: Invalid parameter value. Defaulting to 0.1.");
                        myParameters.put("growInEmptyCell", 0.1);
                    }
                }
            }
        }
    }
    
    /**
    * Properly updates SimRules and Sim objects for Spreading of Fire Sim
    */
    @Override
    protected void setSim(){
        mySimRules = new SpreadingOfFireRules(myParameters);
        mySim = new SpreadingOfFire(mySimRules, mySimData);
    }
}