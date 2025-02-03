package cellsociety.model.simulation.rules;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.simulation.SimulationRules;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// For a WaTorWOrld cell, there can be 3 states
// A cell with state -1 indicates that it needs to be moved
// A cell with state 0 indicates it's empty
// A cell with state 1 indicates it's a fish
// A cell with state 2 indicates it's a shark
public class WaTorWorldRules extends SimulationRules {
  private final Random random = new Random();

  /**
   * @param cell - individual cell from grid
   * @param grid - the list of cell objects representing the grid
   * @return - a list of cell objects representing the neighbors of the cell (adjacent and not diagonals)
   *
   */
  public List<Cell> getNeighbors(Cell cell, Grid grid) {
    return super.getNeighbors(cell, grid, false);
  }

  /**
   * Wa-Tor World
   * A fish cell (1) will randomly move to an empty adjacent squares if some exist, otherwise it will stay in place.
   * After a certain number of rounds, a fish may reproduce. This occurs when a fish moves to a neighboring square, it leaves a new fish in the old time and the reproduction time is set to 0.
   * A shark cell (2) will randomly move to an adjacent square occupied by a fish. If none, it will move to a random empty space. If none, it won't move
   * After each step, a shark is deprived of 1 energy
   * if a shark moves to a square occupied by a fish, it eats the fish and earns a certain amount of energy
   * After a certain number of rounds, a shark may reproduce in the same way as a fish
   *
   * @param cell - individual cell from grid
   * @param grid - the list of cell objects representing the grid
   * @return - the next state of a cell based on the rules of the forest fire model
   */

  @Override
  public int getNextState(Cell cell, Grid grid) {
    int currentState = cell.getState();

    if (currentState == 1) {
      return handleFishCell(cell, grid);
    } else if (currentState == 2) {
      return handleSharkCell(cell, grid);
    }

    return currentState;
  }

  //returning a negative 1 will be handled by the grid class
  private int handleFishCell(Cell cell, Grid grid) {
    List<Cell> emptyNeighbors = getNeighborsByState(cell, grid, 0);
    if (!emptyNeighbors.isEmpty()) {
      return -1;
    }
    return 1;
  }

  private int handleSharkCell(Cell cell, Grid grid) {
    List<Cell> fishNeighbors = getNeighborsByState(cell, grid, 1);
    if (!fishNeighbors.isEmpty()) {
      return -1;
    }
    List<Cell> emptyNeighbors = getNeighborsByState(cell, grid, 0);
    if (!emptyNeighbors.isEmpty()) {
      return -1;
    }
    return 2;
  }

  private List<Cell> getNeighborsByState(Cell cell, Grid grid, int state) {
    List<Cell> allNeighbors = getNeighbors(cell, grid);
    List<Cell> neighborsByState = new ArrayList<>();

    for (Cell neighbor : allNeighbors) {
      if (neighbor.getState() == state) {
        neighborsByState.add(neighbor);
      }
    }
    return neighborsByState;
  }

}
