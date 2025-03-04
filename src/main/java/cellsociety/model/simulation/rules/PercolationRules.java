package cellsociety.model.simulation.rules;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.simulation.GetNeighbors;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.SimulationRules;
import java.util.List;
import java.util.Map;


/**
 * The implementation of percolation simulation rules For a Percolation cell, there can be 3 states
 * <p>
 * - A cell with state 0 indicates it's blocked
 * <p>
 * - A cell with state 1 indicates it's open and water can pass through
 * <p>
 * - A cell with state 2 indicates it's filled and water has passed through this cell
 *
 * @author Justin Aronwald
 */
public class PercolationRules extends SimulationRules {
  private static final int blockedState = 0;
  private static final int openState = 1;
  private static final int filledState = 2;

  /**
   * constructor for percolation model
   *
   * @param parameters - the Map of string to the certain parameter
   * @throws InvalidParameterException - throws when a parameter is invalid
   */
  public PercolationRules(
      Map<String, Parameter<?>> parameters, GetNeighbors myGetNeighbors)
      throws InvalidParameterException {
    super(parameters, myGetNeighbors);
  }

  /**
   * Percolation: Water starts form the top row of the grid. Any open cell (1) in the first row
   * becomes filled If a cell below a filled cell (2) is open, it becomes filled If a neighboring
   * cell (not including diagonals) is open and is connected to another filled cell, it becomes
   * filled If a bottom row cell becomes filled, percolation is successful
   *
   * @param cell - an individual cell from the grid
   * @param grid - the list of cell objects representing the grid
   * @return - the next state of a cell based on the rules of percolation
   */
  @Override
  public int getNextState(Cell cell, Grid grid) {
    int currentState = cell.getState();
    return getNextStatesGivenCurrentState(cell, grid, currentState);
  }

  private int getNextStatesGivenCurrentState(Cell cell, Grid grid, int currentState) {
    if (currentState == blockedState || currentState == filledState) {
      return currentState;
    }
    if (cell.getRow() == blockedState && currentState == openState) {
      return filledState;
    }
    List<Cell> neighbors = getNeighbors(cell, grid);
    for (Cell neighbor : neighbors) {
      if (neighbor.getState() == filledState) {
        return filledState;
      }
    }
    return openState;
  }

  /**
   * gets the total number of states
   *
   * @return - 3, the total number of states
   */
  @Override
  public int getNumberStates() {
    return 3;
  }
}
