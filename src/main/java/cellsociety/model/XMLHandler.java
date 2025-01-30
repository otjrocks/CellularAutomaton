package cellsociety.model;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cellsociety.model.cell.DefaultCell;

/**
 * Allows the program to collect data from an XML configuration file and store the associated date
 *
 * @author Troy Ludwig
 */
public class XMLHandler {
    private String type;
    private String title;
    private String author;
    private String description;
    private int gridHeight;
    private int gridWidth;
    private List<DefaultCell> cells;

    /**
    * XMLHandler constructor for referencing data
    *
    * @param xmlFilePath: The path/location of the XML file that we want to parse for simulation data
    *                     represented as a String
    */
    public XMLHandler(String xmlFilePath) {
        cells = new ArrayList<>();
        parseXMLFile(xmlFilePath);
    }

    /**
    * Method for parsing the XML file and initializing the XMLHandler instance variables with the
    * associated data
    *
    * @param xmlFilePath: The path/location of the XML file that we want to parse for simulation data
    *                     represented as a String
    */
    private void parseXMLFile(String xmlFilePath) {
        try {
            File xmlFile = new File(xmlFilePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            type = doc.getElementsByTagName("Type").item(0).getTextContent();
            title = doc.getElementsByTagName("Title").item(0).getTextContent();
            author = doc.getElementsByTagName("Author").item(0).getTextContent();
            description = doc.getElementsByTagName("Description").item(0).getTextContent();

            Element gridDimensions = (Element) doc.getElementsByTagName("GridDimensions").item(0);
            gridHeight = Integer.parseInt(gridDimensions.getElementsByTagName("Height").item(0).getTextContent());
            gridWidth = Integer.parseInt(gridDimensions.getElementsByTagName("Width").item(0).getTextContent());

            NodeList rows = doc.getElementsByTagName("Row");
            for (int i = 0; i < rows.getLength(); i++) {
                String[] rowValues = rows.item(i).getTextContent().split(",");
                for (int j = 0; j < rowValues.length; j++) {
                    int state = Integer.parseInt(rowValues[j]);
                    cells.add(new DefaultCell(state, new Point2D.Double(j, i)));
                }
            }
        } catch (IOException | NumberFormatException | ParserConfigurationException | DOMException | SAXException e) {
        }
    }

    /**
    * Returns type associated with current simulation
    */
    public String getType() {
        return type;
    }

    /**
    * Returns title of current simulation
    */
    public String getTitle() {
        return title;
    }

    /**
    * Returns author of current simulation
    */
    public String getAuthor() {
        return author;
    }

    /**
    * Returns description of current simulation
    */
    public String getDescription() {
        return description;
    }

    /**
    * Returns the grid height of current simulation
    */
    public int getGridHeight() {
        return gridHeight;
    }

    /**
    * Returns the grid width of current simulation
    */
    public int getGridWidth() {
        return gridWidth;
    }

    /**
    * Returns list of initial cells in current simulation
    */
    public List<DefaultCell> getCells() {
        return new ArrayList<>(cells);
    }

    /**
    * Prints out all information associated with current XMLHandler
    */
    public void testXMLReading() {
        System.out.println("Type: " + type);
        System.out.println("Title: " + title);
        System.out.println("Author: " + author);
        System.out.println("Description: " + description);
        System.out.println("Grid Dimensions: " + gridWidth + "x" + gridHeight);
        System.out.println("Initial Configuration:");
        for (DefaultCell cell : cells) {
            System.out.println("Cell at (" + cell.getLocation().getX() + ", " + cell.getLocation().getY() + ") with state " + cell.getState());
        }
    }

    public static void main(String[] args) {
        XMLHandler handler = new XMLHandler("data/ExampleXMLs/Example1.xml");
        handler.testXMLReading();
    }
}
