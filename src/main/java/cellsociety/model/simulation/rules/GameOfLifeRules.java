package cellsociety.model.simulation.rules;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.CellStateUpdate;
import cellsociety.model.simulation.SimulationRules;
import java.util.List;
import java.util.Map;

public class GameOfLifeRules extends SimulationRules {
  private Map<String, Double> parameters;


  public GameOfLifeRules() {
    super();
  }


  /**
   * @param cell - individual cell from grid
   * @return - a list of cell objects representing the neighbors of the cell (adjacent and
   * diagonals)
   */
  public List<Cell> getNeighbors(Cell cell, Grid grid) {
    return super.getNeighbors(cell, grid, true);
  }

  /**
   *
   * @param grid - the class containing the cell objects
   * @return - A list of cell state/information updates from one class to another
   */
  public List<CellStateUpdate> getNextStatesForAllCells(Grid grid) {
    return super.getNextStatesForAllCells(grid);
  }

  /**
   * Game of Life:
   * Any cell with fewer than 2 neighbors dies due to underpopulation
   * Any cell with 2 - 3 neighbors moves on to the next generation
   * Any cell with more than 3 neighbors dies due to overpopulation
   * Any inactive cell with exactly 3 neighbors becomes active
   *
   * @param cell - individual cell from grid
   * @return the next state of a cell based on the rules of game of life
   */
  @Override
  public int getNextState(Cell cell, Grid grid) {

    if (cell.getRow() >= grid.getRows() || cell.getRow() < 0 || cell.getCol() >= grid.getCols() || cell.getCol() < 0) {
      throw new IndexOutOfBoundsException("Cell position out of bounds");
    }

    List<Cell> neighbors = getNeighbors(cell, grid);
    int aliveNeighbors = 0;
    for (Cell neighbor : neighbors) {
      if (neighbor.getState() == 1) {
        aliveNeighbors++;
      }
    }
    if (aliveNeighbors < 2 || aliveNeighbors > 3) {
      return 0;
    } else if (aliveNeighbors == 3) {
      return 1;
    }
    return cell.getState();
  }
}
