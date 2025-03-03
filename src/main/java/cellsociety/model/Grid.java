package cellsociety.model;

import cellsociety.model.edge.EdgeStrategy;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import cellsociety.config.SimulationConfig;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.simulation.Simulation;

/**
 * A class that handles working with cells within a simulation grid.
 *
 * @author Owen Jennings
 */
public class Grid {

  private final int myNumRows;
  private final int myNumCols;
  private final Map<Point2D, Cell> myCells;
  private EdgeStrategy myEdgeStrategy;

  /**
   * Initialize a data structure to store a grid with the defined width and height
   *
   * @param numRows:      (int) number of rows
   * @param numCols:      (int) number of columns
   * @param edgeStrategy: The edge strategy to use handling the edges of this grid
   */
  public Grid(int numRows, int numCols, EdgeStrategy edgeStrategy) {
    myNumRows = numRows;
    myNumCols = numCols;
    myEdgeStrategy = edgeStrategy;
    myCells = new HashMap<>();
  }

  /**
   * Set the edge strategy for this grid
   *
   * @param edgeStrategy The edge strategy you want to use
   */
  public void setEdgeStrategy(EdgeStrategy edgeStrategy) {
    myEdgeStrategy = edgeStrategy;
  }

  /**
   * Get the number of rows in a grid
   *
   * @return (int) number of rows in a grid
   */
  public int getRows() {
    return myNumRows;
  }

  /**
   * Get number of columns in a grid
   *
   * @return (int) number of columns in a grid
   */
  public int getCols() {
    return myNumCols;
  }

  /**
   * Get a cell at the specified col and row, if it exists
   *
   * @param row: Row of cell
   * @param col: Column of cell
   * @return The cell at the specified location if it exists, or null if it does not exist or is out
   * of bounds
   */
  public Cell getCell(int row, int col) {
    Point2D adjustedPoint = myEdgeStrategy.adjustCoordinate(new Point2D.Double(row, col), myNumRows,
        myNumCols);
    if (checkOutOfBounds(adjustedPoint)) {
      return null;
    }
    if (!cellExists(adjustedPoint)) {
      return null;
    }
    return myCells.get(adjustedPoint);
  }

  /**
   * Get a cell at specified point if it exists
   *
   * @param point: point
   * @return The cell at the specified location if it exists, or null if it does not exist or is out
   * of bounds
   */
  public Cell getCell(Point2D point) {
    Point2D adjustedPoint = myEdgeStrategy.adjustCoordinate(point, myNumRows, myNumCols);
    if (checkOutOfBounds(adjustedPoint)) {
      return null;
    }
    if (!cellExists(adjustedPoint)) {
      return null;
    }
    return myCells.get(adjustedPoint);
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
    location = myEdgeStrategy.adjustCoordinate(location, myNumRows, myNumCols);
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
   * @return The cell state updates that have occurred when the grid was updated
   */
  public List<CellUpdate> updateGrid(Simulation simulation) {
    List<CellUpdate> nextStates = simulation.rules().getNextStatesForAllCells(this);
    for (CellUpdate nextState : nextStates) {
      updateCell(nextState.getNextCell());
    }
    return nextStates;
  }

  /**
   * Checks if the current locations is a boundary/wall for a simulation
   *
   * @param row - the row of the cell you are checking
   * @param col - the col of the cell you are checking
   * @return - a boolean representation on whether it is a wall.
   */
  public boolean isWall(int row, int col) {
    if (checkOutOfBounds(new Point2D.Double(row, col))) {
      return false;
    }

    return row == 0 || row == myNumRows - 1 || col == 0 || col == myNumCols - 1;
  }

  /**
   * Attempt to update a cell in the grid
   *
   * @param cell the cell you which to update. This will update the grid with the cell provided
   */
  public void updateCell(Cell cell) {
    if (!cellExists(cell.getLocation())) {
      return;
    }
    attemptAddCell(cell);
  }

  /**
   * Get an iterator of all the cells that are in the Grid The iterator is from a copy of the grid,
   * so iterator operations such as .remove() will not mutate the grid
   *
   * @return - an iterator of all the cells in a grid
   */
  public Iterator<Cell> getCellIterator() {
    return new ArrayList<>(myCells.values()).iterator();
  }

  /**
   * Set the state of the cell at the specified location
   *
   * @param row:      Row of cell you wish to update
   * @param col:      Column of cell you wish to update
   * @param newState: The new state you wish to give the cell
   */
  public void setState(int row, int col, int newState, Simulation simulation) {
    Cell cell = SimulationConfig.getNewCell(row, col, newState, simulation.data().type());
    updateCell(cell);
  }

  private boolean attemptAddCell(Cell cell) {
    // attempts to add cell to grid. Fails and returns false if cell provided does not have a properly formatted location or does not fit within the grid's width and height
    if (checkOutOfBounds(cell.getLocation())) {
      return false;
    }
    myCells.put(cell.getLocation(), cell);
    return true;
  }

  private boolean checkOutOfBounds(Point2D location) {
    location = myEdgeStrategy.adjustCoordinate(location, myNumRows, myNumCols);
    return (!(location.getX() >= 0)) ||
        (!(location.getY() >= 0)) ||
        (!(location.getX() < myNumRows)) ||
        (!(location.getY() < myNumCols));
  }

}
