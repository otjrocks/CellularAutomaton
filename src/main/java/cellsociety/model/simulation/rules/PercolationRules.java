package cellsociety.model.simulation.rules;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.simulation.SimulationRules;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

//For a Percolation cell, there can be 3 states
// A cell with state 0 indicates it's blocked
// A cell with state 1 indicates it's open and water can pass through
// A cell with state 2 indicates it's filled and water has passed through this cell

public class PercolationRules extends SimulationRules {
  public PercolationRules() {
    super();
  }

  /**
   * @param cell  - an individual cell from the grid
   * @param grid - the list of cell objects representing the grid
   * @return -  a list of cell objects representing the neighbors of the cell (adjacent and
   *    * diagonals)
   */
  @Override
  public List<Cell> getNeighbors(Cell cell, Grid grid) {
    List<Cell> neighbors = new ArrayList<>();

    int [][] directions = {
        {-1, 0}, {1, 0}, {0, -1}, {0, 1} // left, up, down, right
    };

    for (int[] direction : directions) {
      Point2D neighborLocation = new Point2D.Double(cell.getRow() + direction[0], cell.getCol() + direction[1]);

      if (grid.cellExists(neighborLocation)){
        Cell neighbor = grid.getCell(neighborLocation);
        neighbors.add(neighbor);
      }
    }
    return neighbors;
  }

  /**
   * Percolation:
   * Water starts form the top row of the grid.
   * Any open cell in the first row becomes filled
   * If a cell below a filled cell is open, it becomes filled
   * If a neighboring cell (not including diagonals) is open and is connected to another filled cell, it becomes filled
   * If a bottom row cell becomes filled, percolation is successful
   *
   * @param cell - an individual cell from the grid
   * @param grid - the list of cell objects representing the grid
   * @return - the next state of a cell based on the rules of percolation
   */
  @Override
  public int getNextState(Cell cell, Grid grid) {
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
}
