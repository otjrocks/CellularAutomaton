package cellsociety.model.xml;

import cellsociety.view.grid.GridViewFactory.CellShapeType;
import java.io.File;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.model.simulation.SimulationRules;
import cellsociety.view.config.FileChooserConfig;
import javafx.stage.Stage;

/**
 * Class for saving simulation data into an XML file.
 */
public class XMLWriter {

  /**
   * Saves the current simulation to an XML file
   *
   * @param sim:  The simulation object
   * @param grid: The grid containing cell states
   */
  public static void saveSimulationToXML(Simulation sim, Grid grid, CellShapeType cellShapeType,
      Stage stage) {
    File file = FileChooserConfig.makeSaveChooser(sim.data().name()).showSaveDialog(stage);
    if (file == null) {
      return; // User canceled the save operation
    }
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();

      Element simElement = doc.createElement("Simulation");
      doc.appendChild(simElement);

      writeSimData(doc, sim, simElement);
      writeCellShapeType(doc, cellShapeType, simElement);
      writeGrid(doc, grid, simElement);

      SimulationRules rules = sim.rules();
      writeParameters(doc, rules, simElement);

      transformXML(doc, file);

    } catch (ParserConfigurationException | javax.xml.transform.TransformerException _) {
    }
  }

  /**
   * Helper method to add a child element with text content to a parent element
   *
   * @param doc:     Document to which you are adding the element
   * @param parent:  Parent element you're appending the new element to
   * @param tagName: Tag label for newly added element
   * @param value:   Value associated with the newly added element
   */

  private static void addElement(Document doc, Element parent, String tagName, String value) {
    Element element = doc.createElement(tagName);
    element.appendChild(doc.createTextNode(value));
    parent.appendChild(element);
  }

  private static void writeCellShapeType(Document doc, CellShapeType cellShapeType,
      Element simElement) {
    addElement(doc, simElement, "CellType", cellShapeType.toString());
  }

  /**
   * Helper method to add simulation data to XML Writer document
   *
   * @param doc:        Document to which you are adding the simulation data
   * @param sim:        The simulation containing the data you want to save
   * @param simElement: The parent element for all the simulation data
   */
  private static void writeSimData(Document doc, Simulation sim, Element simElement) {
    SimulationMetaData simData = sim.data();
    addElement(doc, simElement, "Type", simData.type());
    addElement(doc, simElement, "Title", simData.name());
    addElement(doc, simElement, "Author", simData.author());
    addElement(doc, simElement, "Description", simData.description());
  }

  /**
   * Helper method to add grid configuration data to XML Writer document
   *
   * @param doc:        Document to which you are adding the grid data
   * @param grid:       The grid containing the data you want to save
   * @param simElement: The parent element for all the simulation data
   */
  private static void writeGrid(Document doc, Grid grid, Element simElement) {
    Element gridElement = doc.createElement("GridDimensions");
    simElement.appendChild(gridElement);
    addElement(doc, gridElement, "Height", String.valueOf(grid.getRows()));
    addElement(doc, gridElement, "Width", String.valueOf(grid.getCols()));

    Element gridDataElement = doc.createElement("GridData");
    simElement.appendChild(gridDataElement);
    for (int i = 0; i < grid.getRows(); i++) {
      StringBuilder rowValues = new StringBuilder();
      for (int j = 0; j < grid.getCols(); j++) {
        Cell cell = grid.getCell(i, j);
        rowValues.append(cell.getState());
        if (j < grid.getCols() - 1) {
          rowValues.append(",");
        }
      }
      Element rowElement = doc.createElement("Row");
      rowElement.appendChild(doc.createTextNode(rowValues.toString()));
      gridDataElement.appendChild(rowElement);
    }
  }

  /**
   * Helper method to add necessary parameters to XML Writer document
   *
   * @param doc:        Document to which you are adding the grid data
   * @param rules:      SimulationRules pbject that dictates necessary parameters
   * @param simElement: The parent element for all the simulation data
   */
  private static void writeParameters(Document doc, SimulationRules rules, Element simElement) {
    Element parametersElement = doc.createElement("Parameters");
    simElement.appendChild(parametersElement);

    for (Map.Entry<String, Parameter<?>> entry : rules.getParameters().entrySet()) {
      addElement(doc, parametersElement, entry.getKey(), String.valueOf(entry.getValue()));
    }
  }

  /**
   * Helper method to transform doc into XML file at designated path
   *
   * @param doc:  Document to which you are adding the grid data
   * @param file: The file generated from filepath
   */
  private static void transformXML(Document doc, File file) throws TransformerException {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();

    // Asked ChatGPT to help with formatting
    transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

    DOMSource source = new DOMSource(doc);
    StreamResult result = new StreamResult(file);

    transformer.transform(source, result);
  }
}