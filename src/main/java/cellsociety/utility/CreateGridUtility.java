package cellsociety.utility;

import cellsociety.config.SimulationConfig;
import cellsociety.model.Grid;
import cellsociety.model.cell.SugarscapeCell;
import cellsociety.model.edge.FixedEdgeStrategy;
import cellsociety.model.edge.MirrorEdgeStrategy;
import cellsociety.model.edge.ToroidalEdgeStrategy;
import cellsociety.model.xml.GridException;
import cellsociety.model.xml.InvalidStateException;
import cellsociety.model.cell.Cell;
import cellsociety.model.simulation.Simulation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * A utility class to handle specific special grid creations.
 */
public class CreateGridUtility {

  /**
   * Method to generate Grid from explicitly defined grid from XML data
   *
   * @param gridDoc:    Document from which you are parsing the explicit grid from
   * @param gridHeight: Height of the grid you're looking to initialize
   * @param gridWidth:  Width of the grid you're looking to initialize
   * @param sim:        The current simulation for getting correct cell types
   */
  public static Grid generateGrid(Document gridDoc, int gridHeight, int gridWidth, Simulation sim)
      throws GridException, InvalidStateException {
    Grid grid = new Grid(gridHeight, gridWidth, new ToroidalEdgeStrategy());
    NodeList rows = gridDoc.getElementsByTagName("Row");
    addAllCellsToGrid(gridHeight, gridWidth, sim, rows, grid);
    return grid;
  }

  private static void addAllCellsToGrid(int gridHeight, int gridWidth, Simulation sim,
      NodeList rows,
      Grid grid) throws GridException, InvalidStateException {
    for (int i = 0; i < rows.getLength(); i++) {
      String[] rowValues = getRowValuesAndHandleExceptions(gridHeight, gridWidth, rows, i);
      for (int j = 0; j < rowValues.length; j++) {
        addCurrentCellToGrid(sim, rowValues, j, i, grid);
      }
    }
  }

  private static void addCurrentCellToGrid(Simulation sim, String[] rowValues, int j, int i,
      Grid grid)
      throws InvalidStateException {
    double rawState = Double.parseDouble(rowValues[j]);
    int state = (int) rawState;
    int param = getDecimalValue(rowValues[j]);
    checkValidState(state, sim);
    Cell holdingCell = SimulationConfig.getNewCell(i, j, state, sim.data().type());

    if (sim.data().type().equals("Sugarscape") && param != 0) {
      ((SugarscapeCell) holdingCell).setSugar(param);
    }

    grid.addCell(holdingCell);
  }

  private static String[] getRowValuesAndHandleExceptions(int gridHeight, int gridWidth,
      NodeList rows, int i)
      throws GridException {
    if (rows.getLength() > gridHeight) {
      throw new GridException();
    }
    String[] rowValues = rows.item(i).getTextContent().split(",");
    if (rowValues.length > gridWidth) {
      throw new GridException();
    }
    return rowValues;
  }

  private static int getDecimalValue(String rawString) {
    if (!rawString.contains(".")) {
      return 0;
    }
    String param = rawString.split("\\.")[1];
    return Integer.parseInt(param);
  }


  /**
   * Method to generate random Grid from a number of defined states
   *
   * @param gridDoc:    Document from which you are parsing the random arguments from
   * @param gridHeight: Height of the grid you're looking to initialize
   * @param gridWidth:  Width of the grid you're looking to initialize
   * @param sim:        The current simulation for getting correct cell types
   */
  public static Grid generateRandomGridFromStateNumber(Document gridDoc, int gridHeight,
      int gridWidth, Simulation sim) {
    // generate a grid with fromDistribution equal to false
    return generateRandomGrid(gridDoc, gridHeight, gridWidth, sim, false);
  }

  /**
   * Method to generate random Grid from a distribution of defined states
   *
   * @param gridDoc:    Document from which you are parsing the random arguments from
   * @param gridHeight: Height of the grid you're looking to initialize
   * @param gridWidth:  Width of the grid you're looking to initialize
   * @param sim:        The current simulation for getting correct cell types
   */
  public static Grid generateRandomGridFromDistribution(Document gridDoc, int gridHeight,
      int gridWidth, Simulation sim) {
    // generate a grid with fromDistribution equal to true
    return generateRandomGrid(gridDoc, gridHeight, gridWidth, sim, true);
  }

  private static Grid generateRandomGrid(Document gridDoc, int gridHeight, int gridWidth,
      Simulation sim, boolean fromDistribution) {
    Grid grid = new Grid(gridHeight, gridWidth, new ToroidalEdgeStrategy());
    int totalCells = gridHeight * gridWidth;

    Map<Integer, Integer> stateCounts = new HashMap<>();
    int assignedCells = 0;

    NodeList randomParams = gridDoc.getElementsByTagName("State");
    for (int i = 0; i < randomParams.getLength(); i++) {
      Element stateElement = (Element) randomParams.item(i);
      String stateName = stateElement.getAttribute("name");
      int stateValue = SimulationConfig.returnStateValueBasedOnName(sim.data().type(), stateName);
      int count = getRandomGridCount(fromDistribution, stateElement, totalCells);

      stateCounts.put(stateValue, count);
      assignedCells += count;
    }

    return initializeGrid(gridHeight, gridWidth, sim, grid, totalCells, stateCounts, assignedCells);
  }

  private static int getRandomGridCount(boolean fromDistribution, Element stateElement,
      int totalCells) {
    if (fromDistribution) {
      int prob = Integer.parseInt(stateElement.getTextContent());
      return Math.round((float) (prob * totalCells) / 100);
    } else {
      return Integer.parseInt(stateElement.getTextContent());
    }
  }


  private static Grid initializeGrid(int gridHeight, int gridWidth, Simulation sim, Grid grid,
      int totalCells, Map<Integer, Integer> stateCounts, int assignedCells) {
    int remainingCells = totalCells - assignedCells;

    List<Integer> cellStates = new ArrayList<>();
    for (Map.Entry<Integer, Integer> entry : stateCounts.entrySet()) {
      for (int i = 0; i < entry.getValue(); i++) {
        cellStates.add(entry.getKey());
      }
    }

    for (int i = 0; i < remainingCells; i++) {
      cellStates.add(0);
    }

    Collections.shuffle(cellStates);

    int index = 0;
    for (int row = 0; row < gridHeight; row++) {
      for (int col = 0; col < gridWidth; col++) {
        int state = cellStates.get(index);
        Cell newCell = SimulationConfig.getNewCell(row, col, state, sim.data().type());
        grid.addCell(newCell);
        index++;
      }
    }
    return grid;
  }

  private static void checkValidState(int state, Simulation sim) throws InvalidStateException {
    if (!sim.data().type().equals("RockPaperScissors") && !sim.data().type().equals("Sugarscape")) {
      int maxState = sim.rules().getNumberStates() - 1;
      if (state > maxState) {
        throw new InvalidStateException();
      }
    }
  }

}
