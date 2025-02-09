package cellsociety.model.simulation.rules;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.simulation.SimulationRules;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//For RockPaperScissors there can be any number of states up to 20
// Winning depends on the next one in the circle (i.e with 3 states: 1 beats 2, 2 beats 3, 3 beats 1)
//The number of states the current state beats as well as loses to is N/2 (rounded down)
  // This is determined by (current state - opponent state + numStates) % numStates
// A cell with state 0 means it's empty.


public class RockPaperScissorsRules extends SimulationRules {

  public RockPaperScissorsRules(Map<String, Double> myParameters)  {
    if (myParameters == null || myParameters.isEmpty()) {
      this.parameters = setDefaultParameters();
    } else {
      this.parameters = new HashMap<>(myParameters);
    }
  }

  /**
   * @param cell -  individual cell from grid
   * @param grid - the list of cell objects representing the grid
   * @return -  a list of cell objects representing the neighbors of the cell (adjacent and
   * diagonals)
   */
  public List<Cell> getNeighbors(Cell cell, Grid grid) {
    return super.getNeighbors(cell, grid, true);
  }



  /**
   * @param cell -  individual cell from grid
   * @param grid - the collection of cell objects representing the grid
   * @return - the next state of a cell based on the rules of Rock Paper Scissors Model
   */

  @Override
  public int getNextState(Cell cell, Grid grid) {
    if (cell.getRow() >= grid.getRows() || cell.getRow() < 0 || cell.getCol() >= grid.getCols()
        || cell.getCol() < 0) {
      throw new IndexOutOfBoundsException("Cell position out of bounds");
    }

    int currentState = cell.getState();

    int numStates = parameters.get("numStates").intValue();
    double threshold = parameters.get("minThreshold");

    if (currentState == 0) {
      return 0;
    }

    Map<Integer, Integer> neighborCount = new HashMap<>();
    for (int i = 1; i < numStates; i++) {
      neighborCount.put(i, 0);
    }

    List<Cell> neighbors = getNeighbors(cell, grid);
    countNeighbors(neighbors, neighborCount);

    int neighborThreshold = (int) Math.ceil(threshold * neighbors.size());

    return checkForWinner(numStates, currentState, neighborCount, neighborThreshold);
  }

  private static int checkForWinner(int numStates, int currentState, Map<Integer, Integer> neighborCount,
      double threshold) {
    int lastWinnningState = currentState;

    for (int i = 1; i < numStates; i++) {
      int winningState = (currentState + i) % numStates;
      if (winningState == 0) {
        continue;
      }

      if (neighborCount.getOrDefault(winningState, 0) > threshold) {
        lastWinnningState = winningState;
      }
    }
    return lastWinnningState;
  }

  private static void countNeighbors(List<Cell> neighbors, Map<Integer, Integer> neighborCount) {
    for (Cell neighbor : neighbors) {
      int neighborState = neighbor.getState();
      if (neighborState != 0) {
        neighborCount.put(neighborState, neighborCount.getOrDefault(neighborState, 0) + 1);
      }
    }
  }


  private Map<String, Double> setDefaultParameters() {
    Map<String, Double> myParameters = new HashMap<>();
    myParameters.put("minThreshold", 0.5);
    myParameters.put("numStates", 3.0);
    return myParameters;
  }

}
