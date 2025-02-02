package cellsociety.model;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cellsociety.model.cell.DefaultCell;
import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationData;
import cellsociety.model.simulation.SimulationRules;
import cellsociety.model.simulation.rules.GameOfLifeRules;
import cellsociety.model.simulation.rules.PercolationRules;
import cellsociety.model.simulation.rules.SegregationModelRules;
import cellsociety.model.simulation.rules.SpreadingOfFireRules;
import cellsociety.model.simulation.types.GameOfLife;
import cellsociety.model.simulation.types.Percolation;
import cellsociety.model.simulation.types.SegregationModel;
import cellsociety.model.simulation.types.SpreadingOfFire;
import javafx.scene.paint.Color;

/**
 * Allows the program to collect data from an XML configuration file and store the associated date
 *
 * @author Troy Ludwig
 */
public class XMLHandler {
    private String myType;
    private String myTitle;
    private String myAuthor;
    private String myDescription;
    private int myGridHeight;
    private int myGridWidth;
    private Grid myGrid;
    private Simulation mySim;
    private SimulationData mySimData;
    private SimulationRules mySimRules;

    /**
    * XMLHandler constructor for referencing data
    *
    * @param xmlFilePath: The path/location of the XML file that we want to parse for simulation data
    *                     represented as a String
    */
    public XMLHandler(String xmlFilePath) {
        ArrayList<Color> colors = new ArrayList(){{
            add(Color.WHITE);
            add(Color.RED);
            add(Color.BLUE);
        }};
        parseXMLFile(xmlFilePath, colors);
        setSim(myType);
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
    private void parseXMLFile(String xmlFilePath, ArrayList<Color> colors) {
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

            mySimData = new SimulationData(myType, myTitle, myAuthor, myDescription, colors);

            Element gridDimensions = (Element) doc.getElementsByTagName("GridDimensions").item(0);
            myGridHeight = Integer.parseInt(gridDimensions.getElementsByTagName("Height").item(0).getTextContent());
            myGridWidth = Integer.parseInt(gridDimensions.getElementsByTagName("Width").item(0).getTextContent());

            myGrid = new Grid(myGridHeight, myGridWidth);
            NodeList rows = doc.getElementsByTagName("Row");
            for (int i = 0; i < rows.getLength(); i++) {
                String[] rowValues = rows.item(i).getTextContent().split(",");
                for (int j = 0; j < rowValues.length; j++) {
                    int state = Integer.parseInt(rowValues[j]);
                    DefaultCell holdingCell = new DefaultCell(state, new Point2D.Double(j, i));
                    myGrid.addCell(holdingCell);
                }
            }
        } catch (IOException | NumberFormatException | ParserConfigurationException | DOMException | SAXException e) {
        }
    }

    private void setSim(String type){
        if (type.equals("GameOfLife")){
            mySimRules = new GameOfLifeRules();
            mySim = new GameOfLife(mySimRules, mySimData);
        }
        if (type.equals("Percolation")){
            mySimRules = new PercolationRules();
            mySim = new Percolation(mySimRules, mySimData);
        }
        if (type.equals("SpreadingOfFire")){
            mySimRules = new SpreadingOfFireRules();
            mySim = new SpreadingOfFire(mySimRules, mySimData);
        }
        if (type.equals("Segregation")){
            mySimRules = new SegregationModelRules();
            mySim = new SegregationModel(mySimRules, mySimData);
        }
    }

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
    public SimulationData getSimData() {
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
    * Prints out all information associated with current XMLHandler
    */
    public void testXMLReading() {
        System.out.println("Type: " + myType);
        System.out.println("Title: " + myTitle);
        System.out.println("Author: " + myAuthor);
        System.out.println("Description: " + myDescription);
        System.out.println("Grid Dimensions: " + myGridWidth + "x" + myGridHeight);
        System.out.println("Initial Configuration:");
        myGrid.printGrid();
    }

    public static void main(String[] args) {
        XMLHandler handler = new XMLHandler("data/ExampleXMLs/Example1.xml");
        handler.testXMLReading();
    }
}
