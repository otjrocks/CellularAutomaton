package cellsociety.model.xml;

import static cellsociety.config.MainConfig.DEFAULT_CELL_SHAPE;
import static cellsociety.config.MainConfig.DEFAULT_EDGE_STRATEGY;

import cellsociety.model.edge.EdgeStrategyFactory;
import cellsociety.model.edge.EdgeStrategyFactory.EdgeStrategyType;
import cellsociety.model.simulation.SimulationCreationException;
import cellsociety.utility.CreateGridUtility;
import cellsociety.view.grid.GridViewFactory.CellShapeType;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
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
 * Allows the program to collect data from an XML configuration file and store the associated date.
 *
 * @author Troy Ludwig
 */
public class XMLHandler {

  public static final String EDGE_TYPE = "EdgeType";
  public static final String CELL_TYPE = "CellType";
  private static int myGridHeight;
  private static int myGridWidth;
  private static Grid myGrid;
  private static Simulation mySim;
  private static SimulationMetaData mySimData;
  private static Map<String, Parameter<?>> myParameters;
  private static CellShapeType myCellShapeType;
  private static EdgeStrategyType myEdgeStrategyType;

  /**
   * XMLHandler constructor for referencing data
   *
   * @param xmlFilePath The path/location of the XML file that we want to parse for simulation data
   *                    represented as a String
   */
  public XMLHandler(String xmlFilePath)
      throws SAXException, IOException, ParserConfigurationException, GridException, InvalidStateException {
    parseXMLFile(xmlFilePath);
  }

  /**
   * Method for parsing the XML file and initializing the XMLHandler instance variables with the
   * associated data
   *
   * @param xmlFilePath The path/location of the XML file that we want to parse for simulation data
   *                    represented as a String
   */
  private void parseXMLFile(String xmlFilePath)
      throws SAXException,
      IOException,
      ParserConfigurationException,
      GridException,
      InvalidStateException {

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
   * @param data Document from which you are extracting the simulation data
   */
  private void parseSimData(Document data) throws InvalidStateException {
    String type = getElement(data, "Type", true).getTextContent();
    String title = getElement(data, "Title", true).getTextContent();
    String author = getElement(data, "Author", true).getTextContent();
    String description = getElement(data, "Description", true).getTextContent();
    parseCellTypeIfPresent(data);
    parseEdgeTypeIfPresent(data);
    Element neighborsElement = getElement(data, "Neighbors", true);
    String neighborType = getElement(data, "NeighborType", true).getTextContent();
    int layers = Integer.parseInt(
        neighborsElement.getElementsByTagName("NeighborLayer").item(0).getTextContent());
    mySimData = new SimulationMetaData(type, title, author, description, neighborType, layers);
  }

  private static void parseCellTypeIfPresent(Document data) {
    try {
      attemptSettingCellType(data);
    } catch (IllegalArgumentException |
             DOMException | InvalidStateException e) {
      // fallback to default cell shape if field is missing in xml file,
      // incorrectly spelled, or other error
      myCellShapeType = DEFAULT_CELL_SHAPE;
    }
  }

  private static void attemptSettingCellType(Document data) throws InvalidStateException {
    if (getElement(data, CELL_TYPE, false) == null) {
      myCellShapeType = DEFAULT_CELL_SHAPE;
      return;
    }
    String cellType = getElement(data, CELL_TYPE, false).getTextContent();
    if (cellType == null) {
      myCellShapeType = DEFAULT_CELL_SHAPE;
    } else {
      myCellShapeType = CellShapeType.valueOf(cellType.toUpperCase());
    }
  }

  private static void parseEdgeTypeIfPresent(Document data) {
    try {
      attemptSettingEdgeType(data);
    } catch (IllegalArgumentException |
             DOMException |
             InvalidStateException e) { // fallback to default edge type if field is missing
      myEdgeStrategyType = DEFAULT_EDGE_STRATEGY;
    }
  }

  private static void attemptSettingEdgeType(Document data) throws InvalidStateException {
    if (getElement(data, EDGE_TYPE, false) == null) {
      myEdgeStrategyType = DEFAULT_EDGE_STRATEGY;
      return;
    }
    String edgeType = getElement(data, EDGE_TYPE, false).getTextContent();
    if (edgeType == null) {
      myEdgeStrategyType = DEFAULT_EDGE_STRATEGY;
    } else {
      myEdgeStrategyType = EdgeStrategyType.valueOf(edgeType.toUpperCase());
    }
  }


  /**
   * Helper method to parse grid dimensions from document
   *
   * @param dimDoc Document from which you are extracting the grid dimensions
   */
  private void parseDimensions(Document dimDoc) throws InvalidStateException {
    Element gridDimensions = getElement(dimDoc, "GridDimensions", true);
    myGridHeight = Integer.parseInt(
        gridDimensions.getElementsByTagName("Height").item(0).getTextContent());
    myGridWidth = Integer.parseInt(
        gridDimensions.getElementsByTagName("Width").item(0).getTextContent());
  }

  /**
   * Helper method to differentiate between explicit and random grid generation
   *
   * @param gridDoc Document from which you are extracting/generating the initial grid data
   */
  private static void parseGrid(Document gridDoc) throws GridException, InvalidStateException {
    if (isRandomInitByState(gridDoc)) {
      myGrid = CreateGridUtility.generateRandomGridFromStateNumber(gridDoc, myGridHeight,
          myGridWidth, EdgeStrategyFactory.createEdgeStrategy(myEdgeStrategyType), mySim);
    } else if (isRandomInitByProb(gridDoc)) {
      myGrid = CreateGridUtility.generateRandomGridFromDistribution(gridDoc, myGridHeight,
          myGridWidth, EdgeStrategyFactory.createEdgeStrategy(myEdgeStrategyType), mySim);
    } else {
      myGrid = CreateGridUtility.generateGrid(gridDoc, myGridHeight, myGridWidth, mySim,
          EdgeStrategyFactory.createEdgeStrategy(myEdgeStrategyType));
    }
  }

  private static boolean isRandomInitByProb(Document gridDoc) {
    return gridDoc.getElementsByTagName("RandomInitByProb").getLength() > 0;
  }

  private static boolean isRandomInitByState(Document gridDoc) {
    return gridDoc.getElementsByTagName("RandomInitByState").getLength() > 0;
  }

  /**
   * Method that assigns the parameters for the current simulation based on simulation type
   *
   * @param doc parsed XML file containing the simulation data most importantly for this function,
   *            the additional sim parameters
   */
  private void parseParameters(Document doc) throws InvalidStateException {
    myParameters = new HashMap<>();
    NodeList params = doc.getElementsByTagName("Parameters");
    getParametersIfTheyExist(params);
  }

  private void getParametersIfTheyExist(NodeList params) throws InvalidStateException {
    if (params.getLength() > 0) {
      Node param = params.item(0);
      if (param.getNodeType() == Node.ELEMENT_NODE) {
        Element paramElement = (Element) param;
        handleParameterElement(paramElement);
      }
    }
  }

  private void handleParameterElement(Element paramElement) throws InvalidStateException {
    for (String paramString : SimulationConfig.getParameters(mySimData.type())) {
      checkAndLoadParameter(paramElement, paramString);
    }
  }

  /**
   * Method that checks an XML file for a parameter with a given name
   *
   * @param paramElement element containing all parameters for a given simulation
   * @param paramName    name of the parameter being checked
   */
  private void checkAndLoadParameter(Element paramElement, String paramName)
      throws InvalidStateException {
    if (getElement(paramElement, paramName, false) != null) {
      String paramValue = getElement(paramElement, paramName, false).getTextContent();
      myParameters.put(paramName, new Parameter<>(paramValue));
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
      throw new SimulationCreationException(
          "Unable to assign the simulation rules based on the provided simulation type", e);
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

  /**
   * Returns the current simulation's cell shape type.
   *
   * @return CellShapeType from the file or the default cell shape if not found
   */
  public CellShapeType getCellShapeType() {
    return myCellShapeType;
  }

  /**
   * Returns the current simulation's edge strategy.
   *
   * @return EdgeStrategyType from the file or the default edge type if not found in the file
   */
  public EdgeStrategyType getEdgeStrategyType() {
    return myEdgeStrategyType;
  }

  public static Element getElement(Element parent, String tagName, boolean required)
      throws InvalidStateException {
    Element element = (Element) parent.getElementsByTagName(tagName).item(0);
    if (element == null && required) {
      throw new InvalidStateException("Missing required element: " + tagName);
    }
    return element;
  }

  private static Element getElement(Document doc, String tagName, boolean required)
      throws InvalidStateException {
    Element element = (Element) doc.getElementsByTagName(tagName).item(0);
    if (element == null && required) {
      throw new InvalidStateException("Missing required element: " + tagName);
    }
    return element;
  }
}
