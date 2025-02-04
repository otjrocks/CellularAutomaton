package cellsociety.model.XMLHandlers;

import java.io.File;
import java.util.Map;

import javafx.stage.FileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.model.simulation.SimulationRules;

/**
 * Class for saving simulation data into an XML file.
 */
public class XMLWriter {

    /**
     * Saves the current simulation to an XML file
     * 
     * @param sim:  The simulation object
     * @param grid:  The grid containing cell states
     * 
     */
    public static void saveSimulationToXML(Simulation sim, Grid grid) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element simElement = doc.createElement("Simulation");
            doc.appendChild(simElement);

            SimulationMetaData simData = sim.getData();
            addElement(doc, simElement, "Type", simData.type());
            addElement(doc, simElement, "Title", simData.name());
            addElement(doc, simElement, "Author", simData.author());
            addElement(doc, simElement, "Description", simData.description());

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
                    if (j < grid.getRows() - 1) {
                        rowValues.append(",");
                    }
                }
                Element rowElement = doc.createElement("Row");
                rowElement.appendChild(doc.createTextNode(rowValues.toString()));
                gridDataElement.appendChild(rowElement);
            }

            SimulationRules rules = sim.getRules();
            Element parametersElement = doc.createElement("Parameters");
            simElement.appendChild(parametersElement);

            for (Map.Entry<String, Double> entry : rules.getParameters().entrySet()) {
                addElement(doc, parametersElement, entry.getKey(), String.valueOf(entry.getValue()));
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("data/User-Generated XMLs/" + simData.name() + ".xml"));

            transformer.transform(source, result);

        } catch (ParserConfigurationException | javax.xml.transform.TransformerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to add a child element with text content to a parent element
     */
    private static void addElement(Document doc, Element parent, String tagName, String value) {
        Element element = doc.createElement(tagName);
        element.appendChild(doc.createTextNode(value));
        parent.appendChild(element);
    }
}