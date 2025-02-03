package cellsociety.model.simulation.rules;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.simulation.SimulationRules;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


//For a Forest Fire cell, there can be 3 states
// A cell with state 0 indicates it's empty
// A cell with state 1 indicates it's occupied by a tree
// A cell with state 2 indicates it's burning

public class SpreadingOfFireRules extends SimulationRules {
  protected final Map<String, Double> parameters;
  private final Random random = new Random();

  public SpreadingOfFireRules() {
    this.parameters = new HashMap<>(setDefaultParameters());
  }

  public SpreadingOfFireRules(Map<String, Double> myParameters) {
    this.parameters = myParameters;
  }

  /**
   * @param cell - individual cell from grid
   * @param grid - the list of cell objects representing the grid
   * @return - a list of cell objects representing the neighbors of the cell (adjacent and not diagonals)
   *
   */
  public List<Cell> getNeighbors(Cell cell, Grid grid) {
    return super.getNeighbors(cell, grid, false);
  }


  /** Forest Fire:
   * A burning cell (2) turns into an empty cell
   * A tree (1) will burn if at least one neighbor is burning
   * A tree ignites with probability f (0.15) even if no neighbor is burning
   * An empty cell (0) fills with a tree with probability p (0.1)
   *
   * @param cell - individual cell from grid
   * @param grid - the list of cell objects representing the grid
   * @return - the next state of a cell based on the rules of the forest fire model
   */
  @Override
  public int getNextState(Cell cell, Grid grid) {

    if (cell.getRow() >= grid.getRows() || cell.getRow() < 0 || cell.getCol() >= grid.getCols() || cell.getCol() < 0) {
      throw new IndexOutOfBoundsException("Cell position out of bounds");
    }

    int currentState = cell.getState();

    // burning -> empty
    if (currentState == 2){
      return 0;
    }

    //empty to tree
    if (currentState == 0) {
      if (random.nextDouble() < parameters.get("growInEmptyCell")){
        return 1;
      }
      return 0;
    }

    // neighbor burning -> you burn
    List<Cell> neighbors = getNeighbors(cell, grid);
    for (Cell neighbor : neighbors){
      if (neighbor.getState() == 2 && cell.getState() == 1) { // you must be a tree to burn
        return 2;
      }
    }

    //random ignition of a tree cell
    if (currentState == 1 && (random.nextDouble() < parameters.get("ignitionWithoutNeighbors"))){
      return 2;
    }

    return currentState;
  }

  private Map<String, Double> setDefaultParameters() {
    Map<String, Double> parameters = new HashMap<>();
    parameters.put("ignitionWithoutNeighbors", 0.15);
    parameters.put("growInEmptyCell", 0.1);
    return parameters;
  }
}
