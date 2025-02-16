package cellsociety.model.simulation.rules;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.cell.DefaultCell;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.SimulationRules;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A rules class to implement the falling sand simulation
 *
 * @author Owen Jennings
 */
public class FallingSandRules extends SimulationRules {

  private final Random RANDOM = new Random();

  /**
   * Create falling sand rules class
   *
   * @param parameters: parameters for the simulation (may not be used)
   * @throws InvalidParameterException: an exception for missing or invalid parameters provided.
   */
  public FallingSandRules(Map<String, Parameter<?>> parameters)
      throws InvalidParameterException {
    super(parameters);
  }

  // An enum to store the possible states for the simulation
  public enum State {
    EMPTY, WALL, SAND;

    public int getValue() {
      return ordinal();
    }
  }

  @Override
  public int getNextState(Cell cell, Grid grid) {
    return 0;
  }

  @Override
  public List<CellUpdate> getNextStatesForAllCells(Grid grid) {
    List<CellUpdate> updates = new ArrayList<>();
    for (int row = grid.getRows() - 1; row >= 0; row--) { // check from bottom of grid to top
      for (int col = 0; col < grid.getCols(); col++) {
        Cell cell = getCellIfInBounds(grid, row, col);
        if (cell == null) {
          continue;
        }
        if (cell.getState() == State.SAND.getValue()) {
          handleSandMovement(grid, cell, updates);
        }
      }
    }
    return updates;
  }

  private void handleSandMovement(Grid grid, Cell sandCell, List<CellUpdate> updates) {
    Cell cellBelow = getCellIfInBounds(grid, sandCell.getRow() + 1, sandCell.getCol());
    if (cellBelow == null) { // no cell below exists, you cannot move stay in location
      return;
    }
    boolean movedDirectlyBelow = attemptMove(grid, cellBelow, updates, sandCell);
    if (!movedDirectlyBelow) {
      attemptMoveSandDiagonal(sandCell, grid, updates);
    }
  }

  private void attemptMoveSandDiagonal(Cell sandCell, Grid grid, List<CellUpdate> updates) {
    Cell leftDiagonalCell = getCellIfInBounds(grid, sandCell.getRow() + 1, sandCell.getCol() - 1);
    Cell rightDiagonalCell = getCellIfInBounds(grid, sandCell.getRow() + 1, sandCell.getCol() + 1);
    moveWithZeroOrOneEmptyDiagonal(grid, sandCell, updates, leftDiagonalCell, rightDiagonalCell);
    // we now know that both diagonals are empty
    moveToRandomDiagonal(grid, sandCell, updates, leftDiagonalCell, rightDiagonalCell);
  }

  private void moveToRandomDiagonal(Grid grid, Cell sandCell, List<CellUpdate> updates,
      Cell leftDiagonalCell,
      Cell rightDiagonalCell) {
    boolean moveLeft = RANDOM.nextBoolean(); // if both diagonals are empty, randomly choose one to move to
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
      Cell newSand = new DefaultCell(State.SAND.getValue(), otherCell.getLocation());
      Cell newEmpty = new DefaultCell(State.EMPTY.getValue(), sandCell.getLocation());
      updates.add(new CellUpdate(otherCell.getLocation(), newSand)); // move sand below
      updates.add(new CellUpdate(sandCell.getLocation(), newEmpty)); // move empty up
      grid.updateCell(newSand);
      grid.updateCell(newEmpty);
      return true;
    }
    return false;
  }

  private Cell getCellIfInBounds(Grid grid, int row, int col) {
    try {
      return grid.getCell(row, col);
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
  }

  /**
   * The simulation has 3 states: EMPTY, WALL, SAND
   *
   * @return 3 for number of states
   */
  @Override
  public int getNumberStates() {
    return State.values().length;
  }
}
