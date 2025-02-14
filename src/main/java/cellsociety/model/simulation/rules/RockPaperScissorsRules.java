package cellsociety.model.simulation.rules;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.SimulationRules;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//For RockPaperScissors there can be any number of states up to 20
// Winning depends on the next one in the circle (i.e with 3 states: 1 beats 2, 2 beats 3, 3 beats 1)
//The number of states the current state beats as well as loses to is N/2 (rounded down)
// This is determined by (current state - opponent state + numStates) % numStates


public class RockPaperScissorsRules extends SimulationRules {

  public RockPaperScissorsRules(Map<String, Parameter<?>> myParameters) {
    super(myParameters);
    if (myParameters == null || myParameters.isEmpty()) {
      this.setParameters(setDefaultParameters());
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

    int numStates = getParameters().get("numStates").getInteger();
    double threshold = getParameters().get("minThreshold").getDouble();

    Map<Integer, Integer> neighborCount = new HashMap<>();
    for (int i = 0; i < numStates; i++) {
      neighborCount.put(i, 0);
    }

    List<Cell> neighbors = getNeighbors(cell, grid);
    countNeighbors(neighbors, neighborCount);

    int neighborThreshold = (int) Math.ceil(threshold * neighbors.size());

    return checkForWinner(numStates, currentState, neighborCount, neighborThreshold);
  }

  @Override
  public int getNumberStates() {
    return getParameters().get("numStates").getInteger();
  }

  private static int checkForWinner(int numStates, int currentState,
      Map<Integer, Integer> neighborCount,
      double threshold) {
    int lastWinnningState = currentState;

    for (int i = 0; i < numStates; i++) {
      int winningState = (currentState + i) % numStates;
      if (isInLosingRange(numStates, currentState, winningState)) {
        continue;
      }

      if (neighborCount.getOrDefault(winningState, 0) >= threshold) {
        lastWinnningState = winningState;
      }
    }
    return lastWinnningState;
  }

  //Needed help from ChatGPT to help refine my logic here.
  private static boolean isInLosingRange(int numStates, int currentState, int winningState) {
    int losingStart = (currentState - numStates / 2 + numStates) % numStates;
    int losingEnd = (currentState - 1 + numStates) % numStates;

    // Adjust for circular modular range
    if (losingStart <= losingEnd) {
      return winningState >= losingStart && winningState <= losingEnd;
    } else {
      return winningState >= losingStart || winningState <= losingEnd;
    }
  }

  private static void countNeighbors(List<Cell> neighbors, Map<Integer, Integer> neighborCount) {
    for (Cell neighbor : neighbors) {
      int neighborState = neighbor.getState();
      neighborCount.put(neighborState, neighborCount.getOrDefault(neighborState, 0) + 1);
    }
  }


  private Map<String, Parameter<?>> setDefaultParameters() {
    Map<String, Parameter<?>> parameters = new HashMap<>();
    parameters.put("minThreshold", new Parameter<>(0.5));
    parameters.put("numStates", new Parameter<>(3));
    return parameters;
  }

}
