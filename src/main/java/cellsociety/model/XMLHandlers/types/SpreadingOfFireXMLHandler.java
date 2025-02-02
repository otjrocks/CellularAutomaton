package cellsociety.model.XMLHandlers.types;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cellsociety.model.XMLHandlers.XMLHandler;
import cellsociety.model.simulation.rules.SpreadingOfFireRules;
import cellsociety.model.simulation.types.SpreadingOfFire;

public class SpreadingOfFireXMLHandler extends XMLHandler {
    public SpreadingOfFireXMLHandler(String xmlFilePath) {
        super(xmlFilePath);
    }

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
    
    @Override
    protected void setSim(){
        mySimRules = new SpreadingOfFireRules();
        mySim = new SpreadingOfFire(mySimRules, mySimData);
    }
}