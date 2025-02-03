package cellsociety.model.XMLHandlers;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import cellsociety.model.XMLHandlers.types.GameOfLifeXMLHandler;
import cellsociety.model.XMLHandlers.types.PercolationXMLHandler;
import cellsociety.model.XMLHandlers.types.SegregationXMLHandler;
import cellsociety.model.XMLHandlers.types.SpreadingOfFireXMLHandler;

/**
* Separate class for differentiating between the different XMLHandler subclasses
*
*/
public class XMLDefiner {
    /**
    * Method for referencing simulation type and returning the proper XMLHandler
    *
    * @param xmlFilePath: The path/location of the XML file that we want to parse for simulation data
    *                     represented as a String
    */
    public static XMLHandler createHandler(String xmlFilePath) throws SAXException, ParserConfigurationException, IOException {
        File xmlFile = new File(xmlFilePath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        String type = doc.getElementsByTagName("Type").item(0).getTextContent();

        switch (type) {
            case "GameOfLife" -> {
                return new GameOfLifeXMLHandler(xmlFilePath);
            }
            case "Percolation" -> {
                return new PercolationXMLHandler(xmlFilePath);
            }
            case "SpreadingOfFire" -> {
                return new SpreadingOfFireXMLHandler(xmlFilePath);
            }
            case "Segregation" -> {
                return new SegregationXMLHandler(xmlFilePath);
            }
            default -> throw new IllegalArgumentException("Unsupported simulation type: " + type);
        }
    }
}