package cellsociety.model.XMLHandlers.types;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cellsociety.model.XMLHandlers.XMLHandler;
import cellsociety.model.simulation.rules.SegregationModelRules;
import cellsociety.model.simulation.types.SegregationModel;

public class SegregationXMLHandler extends XMLHandler {
    public SegregationXMLHandler(String xmlFilePath) {
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
    
    @Override
    protected void setSim(){
        mySimRules = new SegregationModelRules();
        mySim = new SegregationModel(mySimRules, mySimData);
    }
}