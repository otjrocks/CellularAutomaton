package cellsociety;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cellsociety.model.Grid;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.cell.DarwinCell;
import cellsociety.model.cell.DarwinCellRecord;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.getNeighborOptions.MooreNeighbors;
import cellsociety.model.simulation.rules.DarwinRules;

/**
 * A simple test program to run DarwinRules in a 5x5 grid and print output to the console.
 * @author Justin Aronwald
 */
public class DarwinRulesConsoleTest {
  public static final Logger LOGGER = LogManager.getLogger();
  public static void main(String[] args) {
    try {
      runSimulation();
    } catch (InvalidParameterException e) {
      LOGGER.warn("INVALID PARAMETER");
    }
  }

  private static void runSimulation() throws InvalidParameterException {
    int gridSize = 5;

    // Initialize parameters (if needed)
    Map<String, Parameter<?>> parameters = new HashMap<>();

    // Initialize the Darwin Rules
    DarwinRules darwinRules = new DarwinRules(parameters, new MooreNeighbors(2));

    // Initialize the 5x5 grid
    Grid grid = new Grid(gridSize, gridSize);

    // Fill the grid with EMPTY cells
    for (int i = 0; i < gridSize; i++) {
      for (int j = 0; j < gridSize; j++) {
        grid.addCell(new DarwinCell(0, new Point2D.Double(i, j)));
      }
    }

    // Place some DarwinCells with different species
    DarwinCell cell1 = new DarwinCell(new DarwinCellRecord(2, new Point2D.Double(2, 2), 0, 0, 0, new ArrayList<>(), false, 0)); // Species 1 in center

    // Set instructions
    setCellInstruction(cell1);

    // Add cells to the grid
    grid.updateCell(cell1);

    // Print the initial state
    //System.out.println("Initial Grid:");
    printGrid(grid, gridSize);

    // Run the simulation for 5 steps
    for (int i = 0; i < 5; i++) {
      //System.out.println("\nStep " + (i + 1) + ":");

      // Get updates from DarwinRules
      List<CellUpdate> updates = darwinRules.getNextStatesForAllCells(grid);

      // Debug: Check if updates exist
      /*
      if (updates.isEmpty()) {
        System.out.println("No updates generated.");
      } else {
        System.out.println("Applying " + updates.size() + " updates...");
      }
      */

      /*
      // Apply updates to the grid manually
      for (CellUpdate update : updates) {
        grid.updateCell(update.getNextCell());
      }
      */

      // Print the updated grid
      printGrid(grid, gridSize);
    }
  }

  /**
  * Print map for debugging purposes
  */
  private static void printGrid(Grid grid, int size) {
    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        DarwinCell cell = (DarwinCell) grid.getCell(row, col);
        if (cell != null) {
          //System.out.print(cell.getState() + " ");
        } else {
          //System.out.print(". "); // Empty space
        }
      }
      //System.out.println();
    }
  }

  /**
 * Set instructions for the test cell
 */
  private static void setCellInstruction(DarwinCell cell){
    cell.setInstructions("MOVE 1"); // Move forward 1 cell
    cell.setInstructions("LEFT 90"); // Turn
    cell.setInstructions("GO 1"); // Repeat
  }
}