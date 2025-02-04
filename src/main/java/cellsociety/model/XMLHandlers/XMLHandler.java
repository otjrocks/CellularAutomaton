package cellsociety.model.XMLHandlers;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cellsociety.model.Grid;
import cellsociety.model.cell.DefaultCell;
import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.model.simulation.SimulationRules;

/**
 * Allows the program to collect data from an XML configuration file and store the associated date
 *
 * @author Troy Ludwig
 */
public abstract class XMLHandler {
    private String myType;
    private String myTitle;
    private String myAuthor;
    private String myDescription;
    private int myGridHeight;
    private int myGridWidth;
    private Grid myGrid;

    protected Simulation mySim;
    protected SimulationMetaData mySimData;
    protected SimulationRules mySimRules;
    protected Map<String, Double> myParameters;

    /**
    * XMLHandler constructor for referencing data
    *
    * @param xmlFilePath: The path/location of the XML file that we want to parse for simulation data
    *                     represented as a String
    */
    public XMLHandler(String xmlFilePath) {
        parseXMLFile(xmlFilePath);
    }

    /**
    * Method for parsing the XML file and initializing the XMLHandler instance variables with the
    * associated data
    *
    * @param xmlFilePath: The path/location of the XML file that we want to parse for simulation data
    *                     represented as a String
    * @param colors: An arraylist of colors to pass as options for SimulationData
    *                Just a temporary variable until I can update the XML file tags
    */
    private void parseXMLFile(String xmlFilePath) {
        try {
            File xmlFile = new File(xmlFilePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            myType = doc.getElementsByTagName("Type").item(0).getTextContent();
            myTitle = doc.getElementsByTagName("Title").item(0).getTextContent();
            myAuthor = doc.getElementsByTagName("Author").item(0).getTextContent();
            myDescription = doc.getElementsByTagName("Description").item(0).getTextContent();

            mySimData = new SimulationMetaData(myType, myTitle, myAuthor, myDescription);

            Element gridDimensions = (Element) doc.getElementsByTagName("GridDimensions").item(0);
            myGridHeight = Integer.parseInt(gridDimensions.getElementsByTagName("Height").item(0).getTextContent());
            myGridWidth = Integer.parseInt(gridDimensions.getElementsByTagName("Width").item(0).getTextContent());

            myGrid = new Grid(myGridHeight, myGridWidth);
            NodeList rows = doc.getElementsByTagName("Row");
            for (int i = 0; i < rows.getLength(); i++) {
                String[] rowValues = rows.item(i).getTextContent().split(",");
                for (int j = 0; j < rowValues.length; j++) {
                    int state = Integer.parseInt(rowValues[j]);
                    DefaultCell holdingCell = new DefaultCell(state, new Point2D.Double(i, j));
                    myGrid.addCell(holdingCell);
                }
            }

            parseParameters(doc);
            setSim();

        } catch (IOException | NumberFormatException | ParserConfigurationException | DOMException | SAXException e) {
        }
    }

    /**
    * Abstract method that assigns the parameters for the current simulation based on simulation type
    * @param doc: parsed XML file containing the simulation data
    *             most importantly for this function, the additional sim parameters
    */
    protected abstract void parseParameters(Document doc);

    /**
    * Abstract method that assigns SimRules and Sim based on simulation type
    */
    protected abstract void setSim();

    /**
    * Returns type associated with current simulation
    */
    public String getType() {
        return myType;
    }

    /**
    * Returns title of current simulation
    */
    public String getTitle() {
        return myTitle;
    }

    /**
    * Returns author of current simulation
    */
    public String getAuthor() {
        return myAuthor;
    }

    /**
    * Returns description of current simulation
    */
    public String getDescription() {
        return myDescription;
    }

    /**
    * Returns the grid height of current simulation
    */
    public int getGridHeight() {
        return myGridHeight;
    }

    /**
    * Returns the grid width of current simulation
    */
    public int getGridWidth() {
        return myGridWidth;
    }

    /**
    * Returns the initial grid
    */
    public Grid getGrid() {
        return myGrid;
    }

    /**
    * Returns the simulation data associated with current simulation
    */
    public SimulationMetaData getSimData() {
        return mySimData;
    }

    /**
    * Returns the simulation rules associated with current simulation
    */
    public SimulationRules getSimRules(){
        return mySimRules;
    }

    /**
    * Returns the current simulation object
    */
    public Simulation getSim(){
        return mySim;
    }

    /**
    * Returns the current additional simulation parameters
    */
    public Map<String, Double> getParams(){
        return myParameters;
    }
}
