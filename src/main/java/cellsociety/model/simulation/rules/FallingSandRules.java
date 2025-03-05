package cellsociety.model.simulation.rules;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.cell.DefaultCell;
import cellsociety.model.simulation.neighbors.GetNeighbors;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A rules class to implement the falling sand simulation.
 *
 * @author Owen Jennings
 */
public class FallingSandRules extends SimulationRules {

  private static final Random RANDOM = new Random();

  /**
   * Create falling sand rules class. No parameters are required for this rules class.
   *
   * @param parameters parameters for the simulation (may not be used)
   * @throws InvalidParameterException an exception for missing or invalid parameters provided.
   */
  public FallingSandRules(Map<String, Parameter<?>> parameters, GetNeighbors myGetNeighbors)
      throws InvalidParameterException {
    super(parameters, myGetNeighbors);
  }

  /**
   * An enum to store the possible states for this simulation.
   */
  public enum State {
    EMPTY, WALL, SAND;

    /**
     * Get the ordinal value of this enum entry.
     *
     * @return An int representing the state
     */
    public int getValue() {
      return ordinal();
    }
  }

  /**
   * Get the next states cell update list for all cells in the simulation.
   *
   * @param grid The grid that you wish to get the next states for
   * @return A list of cell updates representing changes that need to be made to the grid on next
   * update
   */
  @Override
  public List<CellUpdate> getNextStatesForAllCells(Grid grid) {
    List<CellUpdate> updates = new ArrayList<>();
    handleEntireGrid(grid, updates);
    return updates;
  }

  private void handleEntireGrid(Grid grid, List<CellUpdate> updates) {
    for (int row = grid.getRows() - 1; row >= 0; row--) { // check from bottom of grid to top
      for (int col = 0; col < grid.getCols(); col++) {
        handleCurrentCell(grid, row, col, updates);
      }
    }
  }

  private void handleCurrentCell(Grid grid, int row, int col, List<CellUpdate> updates) {
    Cell cell = getCellIfInBounds(grid, row, col);
    if (cell == null) {
      return;
    }
    if (cell.getState() == State.SAND.getValue()) {
      handleSandMovement(grid, cell, updates);
    }
  }

  /**
   * The simulation has 3 states: EMPTY, WALL, SAND.
   *
   * @return 3 for number of states
   */
  @Override
  public int getNumberStates() {
    return State.values().length;
  }

  private void handleSandMovement(Grid grid, Cell sandCell, List<CellUpdate> updates) {
    Cell cellBelow = getNeighborCell(sandCell, grid, sandCell.getRow() + 1, sandCell.getCol());
    if (cellBelow == null) { // no cell below exists, you cannot move stay in location
      return;
    }
    boolean movedDirectlyBelow = attemptMove(grid, cellBelow, updates, sandCell);
    if (!movedDirectlyBelow) {
      attemptMoveSandDiagonal(sandCell, grid, updates);
    }
  }

  private void attemptMoveSandDiagonal(Cell sandCell, Grid grid, List<CellUpdate> updates) {
    Cell leftDiagonalCell = getNeighborCell(sandCell, grid, sandCell.getRow() + 1,
        sandCell.getCol() - 1);
    Cell rightDiagonalCell = getNeighborCell(sandCell, grid, sandCell.getRow() + 1,
        sandCell.getCol() + 1);
    moveWithZeroOrOneEmptyDiagonal(grid, sandCell, updates, leftDiagonalCell, rightDiagonalCell);
    // we now know that both diagonals are empty
    moveToRandomDiagonal(grid, sandCell, updates, leftDiagonalCell, rightDiagonalCell);
  }

  private void moveToRandomDiagonal(Grid grid, Cell sandCell, List<CellUpdate> updates,
      Cell leftDiagonalCell,
      Cell rightDiagonalCell) {
    boolean moveLeft = RANDOM.nextBoolean();
    // if both diagonals are empty, randomly choose one to move to
    if (moveLeft) {
      attemptMove(grid, leftDiagonalCell, updates, sandCell);
    } else {
      attemptMove(grid, rightDiagonalCell, updates, sandCell);
    }
  }

  private static void moveWithZeroOrOneEmptyDiagonal(Grid grid, Cell sandCell,
      List<CellUpdate> updates,
      Cell leftDiagonalCell,
      Cell rightDiagonalCell) {
    if (leftDiagonalCell == null || leftDiagonalCell.getState()
        != State.EMPTY.getValue()) { // can only potentially move to right
      attemptMove(grid, rightDiagonalCell, updates, sandCell);
    }
    if (rightDiagonalCell == null || rightDiagonalCell.getState()
        != State.EMPTY.getValue()) { // can only potentially move to left
      attemptMove(grid, leftDiagonalCell, updates, sandCell);
    }
  }

  // Try moving sandCell to otherCell
  // returns true if move was allowed, false otherwise
  private static boolean attemptMove(Grid grid, Cell otherCell, List<CellUpdate> updates,
      Cell sandCell) {
    if (sandCell == null
        || sandCell.getState() != State.SAND.getValue()) { // cannot move if not sand cell
      return false;
    }
    if (otherCell != null
        && otherCell.getState() == State.EMPTY.getValue()) { // can move to other cell
      swapSandAndEmptyCell(grid, otherCell, updates, sandCell);
      return true;
    }
    return false;
  }

  private static void swapSandAndEmptyCell(Grid grid, Cell otherCell, List<CellUpdate> updates,
      Cell sandCell) {
    Cell newSand = new DefaultCell(State.SAND.getValue(), otherCell.getLocation());
    Cell newEmpty = new DefaultCell(State.EMPTY.getValue(), sandCell.getLocation());
    updates.add(new CellUpdate(otherCell.getLocation(), newSand)); // move sand below
    updates.add(new CellUpdate(sandCell.getLocation(), newEmpty)); // move empty up
    grid.updateCell(newSand);
    grid.updateCell(newEmpty);
  }

  private Cell getNeighborCell(Cell cell, Grid grid, int row, int col) {
    List<Cell> neighbors = getNeighbors(cell, grid);
    for (Cell neighbor : neighbors) {
      if (neighbor.getRow() == row && neighbor.getCol() == col) {
        return neighbor;
      }
    }
    return null;
  }

  private Cell getCellIfInBounds(Grid grid, int row, int col) {
    try {
      return grid.getCell(row, col);
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
  }

}
