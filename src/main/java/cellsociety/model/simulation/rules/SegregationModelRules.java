package cellsociety.model.simulation.rules;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.simulation.SimulationRules;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

//For a SegregationModel cell, there can be 3 states
// A cell with state 0 indicates it's empty
// A cell with state 1 indicates it's a part of Group 1
// A cell with state 2 indicates it's a part of Group 2

public class SegregationModelRules extends SimulationRules {
  protected final Map<String, Double> parameters;
  private final Random random = new Random();

  public SegregationModelRules() {
    this.parameters = new HashMap<>(setDefaultParameters());
  }

  /**
   * @param cell -  individual cell from grid
   * @param grid - the list of cell objects representing the grid
   * @return -  a list of cell objects representing the neighbors of the cell (adjacent and diagonals)
   */
  @Override
  public List<Cell> getNeighbors(Cell cell, Grid grid) {
    return super.getNeighbors(cell, grid, true);
  }


  /** Schelling's Model of Segregation:
   * There exists a probability tolerance threshold T (0.3) representing the minimum number
   * of neighbors that must be the same state for the cell to be satisfied

   *  If a cell has less than T of similar neighbors, they move to a random empty space
   *  If a cell >= T of similar neighbors, they stay
   *  Cells move until all cells are satisfied or no empty spaces remain
   *
   * @param cell -  individual cell from grid
   * @param grid - the list of cell objects representing the grid
   * @return - the next state of a cell based on the rules of Schelling's segregation model
   */
  @Override
  public int getNextState(Cell cell, Grid grid) {
    List<Cell> neighbors = getNeighbors(cell, grid);
    int currentState = cell.getState();

    int sameType = 0;
    int totalNeighbors = 0;
    for (Cell neighbor : neighbors) {
      if (neighbor.getState() != 0) {
        totalNeighbors++;
        if (neighbor.getState() == currentState) {
          sameType++;
        }
      }
    }
    // No occupied neighbors -> stay
    if (totalNeighbors == 0) {
      return currentState;
    }
    double typePercentage = (double) sameType / totalNeighbors;

    // unsatisfied -> mark as moved (HANDLE MOVEMENT IN GRID)
    if (typePercentage < parameters.get("toleranceThreshold")) {
      return 0;
    }

    return currentState;
  }

  private Map<String, Double> setDefaultParameters () {
    Map<String, Double> parameters = new HashMap<>();
    parameters.put("toleranceThreshold", 0.3);
    return parameters;
  }
}
