package cellsociety.model;

import cellsociety.model.cell.Cell;
import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationRules;
import cellsociety.model.cell.CellStateUpdate;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class that handles working with cells within a simulation grid.
 *
 * @author Owen Jennings
 */
public class Grid {

  private final int myNumRows;
  private final int myNumCols;
  private final Map<Point2D, Cell> myCells;

  /**
   * Initialize a data structure to store a grid with the defined width and height
   *
   * @param numRows:  (int) number of rows
   * @param numCols: (int) number of columns
   */
  public Grid(int numRows, int numCols) {
    myNumRows = numRows;
    myNumCols = numCols;
    myCells = new HashMap<>();
  }

  /**
   * Get a cell at the specified col and row, if it exists
   *
   * @param row: Row of cell
   * @param col: Column of cell
   * @return The cell at the specified location if it exists, or null if it does not exist
   */
  public Cell getCell(int row, int col) {
    return myCells.get(new Point2D.Double(row, col));
  }

  /**
   * Get a cell at specified point if it exists
   *
   * @param point: point
   * @return The cell at the specified location if it exists, or null if it does not exist
   */
  public Cell getCell(Point2D point) {
    return myCells.get(point);
  }

  /**
   * Add a cell to the grid
   *
   * @param cell: The cell you which to add
   * @return true if the cell is successfully added. false if the cell cannot be added because the
   * cells location is improperly formatted or because there is already a cell in the specified
   * location
   */
  public boolean addCell(Cell cell) {
    if (cellExists(
        cell.getLocation())) {  // cannot add a cell if the specified location already holds a seperate cell
      return false;
    }
    return attemptAddCell(cell);
  }

  /**
   * Check if a cell exists in the grid
   *
   * @param location: Location of cell
   * @return true if the cell exists, false otherwise
   */
  public boolean cellExists(Point2D location) {
    return myCells.containsKey(location);
  }

  /**
   * Update all cells in the grid based on the specified cell update rule determined by the
   * simulation's rules class getNextState() Per project specifications: ""A simulation's rules
   * (such as whether a cell changes state, is created, or moves to another position in the grid)
   * are applied on each cell "simultaneously" (i.e., based on its current state and that of its
   * neighbors) and then cell states are updated in a second pass.""
   *
   * @param simulation: The simulation you which to use to update the grid
   */
  public void updateGrid(Simulation simulation) {
    SimulationRules rules = simulation.getRules();
    List<CellStateUpdate> nextStates = getNextStatesForAllCells(rules);
    for (CellStateUpdate nextState: nextStates) {
      getCell(nextState.getRow(), nextState.getCol()).setState(nextState.getState());
    }
  }


  /**
   * For debugging, print current states of cells in grid.
   * I asked ChatGPT for assistance in writing this method
   */
  public void printGrid() {
    for (int row = 0; row < myNumRows; row++) {
      for (int col = 0; col < myNumCols; col++) {
        Cell cell = getCell(row, col);

        if (cell != null) {
          System.out.print(cell.getState() + " ");
        } else {
          System.out.print("0 ");
        }
      }
      System.out.println();
    }
  }



  public List<CellStateUpdate> getNextStatesForAllCells(SimulationRules rules) {
    List<CellStateUpdate> nextStates = new ArrayList<>(); // calculate next states in first pass, then update all next states in second pass
    for (Cell cell : myCells.values()) {
      nextStates.add(new CellStateUpdate(cell.getLocation(), rules.getNextState(cell, this)));
    }
    return nextStates;
  }

  private boolean attemptAddCell(Cell cell) {
    // attempts to add cell to grid. Fails and returns false if cell provided does not have a properly formatted location or does not fit within the grid's width and height
    if (!checkInBounds(cell)) {
      return false;
    }
    myCells.put(cell.getLocation(), cell);
    return true;
  }

  private boolean checkInBounds(Cell cell) {
    return (cell.getLocation().getX() >= 0) &&
        (cell.getLocation().getY() >= 0) &&
        (cell.getLocation().getX() < myNumRows) &&
        (cell.getLocation().getY() < myNumCols);
  }
}
