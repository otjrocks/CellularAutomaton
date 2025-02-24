package cellsociety.model.xml;

import cellsociety.utility.CreateGridUtility;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cellsociety.config.SimulationConfig;
import cellsociety.model.Grid;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationMetaData;

/**
 * Allows the program to collect data from an XML configuration file and store the associated date
 *
 * @author Troy Ludwig
 */
public class XMLHandler {

  private static int myGridHeight;
  private static int myGridWidth;
  private static Grid myGrid;
  private static Simulation mySim;
  private static SimulationMetaData mySimData;
  private static Map<String, Parameter<?>> myParameters;

  /**
   * XMLHandler constructor for referencing data
   *
   * @param xmlFilePath: The path/location of the XML file that we want to parse for simulation data
   *                     represented as a String
   */
  public XMLHandler(String xmlFilePath)
      throws SAXException, IOException, ParserConfigurationException, GridException, InvalidStateException {
    parseXMLFile(xmlFilePath);
  }

  /**
   * Method for parsing the XML file and initializing the XMLHandler instance variables with the
   * associated data
   *
   * @param xmlFilePath: The path/location of the XML file that we want to parse for simulation data
   *                     represented as a String
   */
  private void parseXMLFile(String xmlFilePath)
      throws SAXException, IOException, ParserConfigurationException, GridException, InvalidStateException {

    File xmlFile = new File(xmlFilePath);
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(xmlFile);
    doc.getDocumentElement().normalize();

    parseSimData(doc);
    parseDimensions(doc);
    parseParameters(doc);
    setSim();
    parseGrid(doc);
  }

  /**
   * Helper method to parse simulation data from document
   *
   * @param data: Document from which you are extracting the simulation data
   */
  private void parseSimData(Document data) {
    String type = data.getElementsByTagName("Type").item(0).getTextContent();
    String title = data.getElementsByTagName("Title").item(0).getTextContent();
    String author = data.getElementsByTagName("Author").item(0).getTextContent();
    String description = data.getElementsByTagName("Description").item(0).getTextContent();
    String neighborType = data.getElementsByTagName("NeighborType").item(1).getTextContent();
    int layers = Integer.parseInt(data.getElementsByTagName("NeighborLayer").item(0).getTextContent());
    mySimData = new SimulationMetaData(type, title, author, description, neighborType, layers);
  }

  /**
   * Helper method to parse grid dimensions from document
   *
   * @param dimDoc: Document from which you are extracting the grid dimensions
   */
  private void parseDimensions(Document dimDoc) {
    Element gridDimensions = (Element) dimDoc.getElementsByTagName("GridDimensions").item(0);
    myGridHeight = Integer.parseInt(
        gridDimensions.getElementsByTagName("Height").item(0).getTextContent());
    myGridWidth = Integer.parseInt(
        gridDimensions.getElementsByTagName("Width").item(0).getTextContent());
  }

  /**
   * Helper method to differentiate between explicit and random grid generation
   *
   * @param gridDoc: Document from which you are extracting/generating the initial grid data
   */
  private static void parseGrid(Document gridDoc) throws GridException, InvalidStateException {
    if (gridDoc.getElementsByTagName("RandomInitByState").getLength() > 0) {
      myGrid = CreateGridUtility.generateRandomGridFromStateNumber(gridDoc, myGridHeight,
          myGridWidth, mySim);
    } else if (gridDoc.getElementsByTagName("RandomInitByProb").getLength() > 0) {
      myGrid = CreateGridUtility.generateRandomGridFromDistribution(gridDoc, myGridHeight,
          myGridWidth, mySim);
    } else {
      myGrid = CreateGridUtility.generateGrid(gridDoc, myGridHeight, myGridWidth, mySim);
    }
  }

  /**
   * Method that assigns the parameters for the current simulation based on simulation type
   *
   * @param doc: parsed XML file containing the simulation data most importantly for this function,
   *             the additional sim parameters
   */
  private void parseParameters(Document doc) {
    myParameters = new HashMap<>();
    NodeList params = doc.getElementsByTagName("Parameters");
    if (params.getLength() > 0) {
      Node param = params.item(0);
      if (param.getNodeType() == Node.ELEMENT_NODE) {
        Element paramElement = (Element) param;
        for (String paramString : SimulationConfig.getParameters(mySimData.type())) {
          if (paramString.equals("ruleString")) {
            checkAndLoadRulestring(paramElement);
          } else {
            checkAndLoadParameter(paramElement, paramString);
          }
        }
      }
    }
  }

  /**
   * Method that checks an XML file for a parameter with a given name
   *
   * @param paramElement: element containing all parameters for a given simulation
   * @param paramName:    name of the parameter being checked
   */
  private void checkAndLoadParameter(Element paramElement, String paramName) {
    try {
      String paramValue = paramElement.getElementsByTagName(paramName).item(0).getTextContent();
      myParameters.put(paramName, new Parameter<>(paramValue));
    } catch (NumberFormatException e) {
      System.err.println("Warning: Invalid parameter value. Defaulting to 1.0.");
      myParameters.put(paramName, new Parameter<Object>(1.0));
    }
  }

  private void checkAndLoadRulestring(Element paramElement) {
    try {
      String paramString = paramElement.getElementsByTagName("ruleString").item(0).getTextContent();
      myParameters.put("ruleString", new Parameter<>(paramString));
    } catch (Exception e) {
      myParameters.clear();
    }
  }

  /**
   * Method that assigns SimRules and Sim based on simulation type
   */
  private void setSim() {
    try {
      mySim = SimulationConfig.getNewSimulation(mySimData.type(), mySimData, myParameters);
    } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException |
             InstantiationException | IllegalAccessException | InvalidParameterException e) {
      throw new RuntimeException(e);
    }
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
   * Returns the current simulation object
   */
  public Simulation getSim() {
    return mySim;
  }

  /**
   * Returns the current additional simulation parameters
   */
  public Map<String, Parameter<?>> getParams() {
    return myParameters;
  }
}
