package cellsociety.model.XMLHandlers.types;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cellsociety.model.XMLHandlers.XMLHandler;
import cellsociety.model.simulation.rules.WaTorWorldRules;
import cellsociety.model.simulation.types.WaTorWorld;

/**
* Properly updates Wa-Tor World Simulations with the right Simulation and SimulationRules objects
* after parsing the XML for universal Simulation data
*/
public class WWXMLHandler extends XMLHandler {

    /**
    * WWXMLHandler constructor that collects the universal sim data
    */
    public WWXMLHandler(String xmlFilePath) {
        super(xmlFilePath);
    }

    /**
    * Collects the necessary parameters for Wa-Tor World Simulation (fishReproductionTime, sharkReproductionTime and sharkEnergyGain)
    */
    @Override
    protected void parseParameters(Document doc) {
        myParameters = new HashMap<>();
        NodeList params = doc.getElementsByTagName("Parameters");
        if (params.getLength() > 0) {
            Node param = params.item(0);
            if (param.getNodeType() == Node.ELEMENT_NODE) {
                Element paramElement = (Element) param;
                
                if (paramElement.getElementsByTagName("fishReproductionTime").getLength() > 0) {
                    try {
                        double fishReproductionTime = Double.parseDouble(paramElement.getElementsByTagName("fishReproductionTime").item(0).getTextContent());
                        myParameters.put("fishReproductionTime", fishReproductionTime);
                    } catch (NumberFormatException e) {
                        System.err.println("Warning: Invalid parameter value. Defaulting to 0.3.");
                        myParameters.put("fishReproductionTime", 0.3);
                    }
                }

                if (paramElement.getElementsByTagName("sharkReproductionTime").getLength() > 0) {
                    try {
                        double sharkReproductionTime = Double.parseDouble(paramElement.getElementsByTagName("sharkReproductionTime").item(0).getTextContent());
                        myParameters.put("sharkReproductionTime", sharkReproductionTime);
                    } catch (NumberFormatException e) {
                        System.err.println("Warning: Invalid parameter value. Defaulting to 0.3.");
                        myParameters.put("sharkReproductionTime", 0.3);
                    }
                }

                if (paramElement.getElementsByTagName("sharkEnergyGain").getLength() > 0) {
                    try {
                        double sharkEnergyGain = Double.parseDouble(paramElement.getElementsByTagName("sharkEnergyGain").item(0).getTextContent());
                        myParameters.put("sharkEnergyGain", sharkEnergyGain);
                    } catch (NumberFormatException e) {
                        System.err.println("Warning: Invalid parameter value. Defaulting to 0.3.");
                        myParameters.put("sharkEnergyGain", 0.3);
                    }
                }
            }
        }
    }
    
    /**
    * Properly updates SimRules and Sim objects for Wa-Tor World Sim
    */
    @Override
    protected void setSim(){
        mySimRules = new WaTorWorldRules(myParameters);
        mySim = new WaTorWorld(mySimRules, mySimData);
    }
}