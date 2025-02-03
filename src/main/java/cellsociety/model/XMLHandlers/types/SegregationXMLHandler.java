package cellsociety.model.XMLHandlers.types;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cellsociety.model.XMLHandlers.XMLHandler;
import cellsociety.model.simulation.rules.SegregationModelRules;
import cellsociety.model.simulation.types.SegregationModel;

/**
* Properly updates Segregation Simulations with the right Simulation and SimulationRules objects
* after parsing the XML for universal Simulation data
*/
public class SegregationXMLHandler extends XMLHandler {

    /**
    * SegregationXMLHandler constructor that collects the universal sim data
    */
    public SegregationXMLHandler(String xmlFilePath) {
        super(xmlFilePath);
    }

    /**
    * Collects the necessary parameters for Segregation Simulation (toleranceThreshold)
    */
    @Override
    protected void parseParameters(Document doc) {
        myParameters = new HashMap<>();
        NodeList params = doc.getElementsByTagName("Parameters");
        if (params.getLength() > 0) {
            Node param = params.item(0);
            if (param.getNodeType() == Node.ELEMENT_NODE) {
                Element paramElement = (Element) param;
                
                try {
                    double toleranceThreshold = Double.parseDouble(paramElement.getElementsByTagName("toleranceThreshold").item(0).getTextContent());
                    myParameters.put("toleranceThreshold", toleranceThreshold);
                } catch (NumberFormatException e) {
                    System.err.println("Warning: Invalid parameter value. Defaulting to 0.3.");
                    myParameters.put("toleranceThreshold", 0.3);
                }
            }
        }
    }
    
    /**
    * Properly updates SimRules and Sim objects for Segregation Sim
    */
    @Override
    protected void setSim(){
        mySimRules = new SegregationModelRules(myParameters);
        mySim = new SegregationModel(mySimRules, mySimData);
    }
}