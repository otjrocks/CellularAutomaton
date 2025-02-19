package cellsociety.model.simulation.rules;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.SimulationRules;
import java.util.List;
import java.util.Map;

//For a Percolation cell, there can be 3 states
// A cell with state 0 indicates it's blocked
// A cell with state 1 indicates it's open and water can pass through
// A cell with state 2 indicates it's filled and water has passed through this cell

public class PercolationRules extends SimulationRules {

  /**
   * constructor for percolation model
   *
   * @param parameters - the Map of string to the certain parameter
   * @throws InvalidParameterException - throws when a parameter is invalid
   */
  public PercolationRules(
      Map<String, Parameter<?>> parameters) throws InvalidParameterException {
    super(parameters);
  }


  /**
   * @param cell - an individual cell from the grid
   * @param grid - the list of cell objects representing the grid
   * @return -  a list of cell objects representing the neighbors of the cell (adjacent and not
   * diagonals)
   */
  public List<Cell> getNeighbors(Cell cell, Grid grid) {
    return super.getNeighbors(cell, grid, false);
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

    if (cell.getRow() >= grid.getRows() || cell.getRow() < 0 || cell.getCol() >= grid.getCols()
        || cell.getCol() < 0) {
      throw new IndexOutOfBoundsException("Cell position out of bounds");
    }

    int currentState = cell.getState();
    if (currentState == 0) {
      return 0;
    }
    if (currentState == 2) {
      return 2;
    }
    if (cell.getRow() == 0 && currentState == 1) {
      return 2;
    }

    List<Cell> neighbors = getNeighbors(cell, grid);
    for (Cell neighbor : neighbors) {
      if (neighbor.getState() == 2) {
        return 2;
      }
    }

    return 1;
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
