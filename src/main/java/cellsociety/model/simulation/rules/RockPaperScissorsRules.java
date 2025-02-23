package cellsociety.model.simulation.rules;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.simulation.GetNeighbors;
import cellsociety.model.simulation.InvalidParameterException;
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

  private final int myNumStates;
  private final double myMinThreshold;

  /**
   * The default constructor of a RockPaperScissors rules.
   *
   * @param myParameters The required parameters map
   * @throws InvalidParameterException This is thrown for invalid parameters provided.
   */
  public RockPaperScissorsRules(Map<String, Parameter<?>> myParameters, GetNeighbors myGetNeighbors)
      throws InvalidParameterException {
    super(myParameters, myGetNeighbors);
    if (myParameters == null || myParameters.isEmpty()) {
      this.setParameters(setDefaultParameters());
    }
    checkMissingParameterAndThrowException("numStates");
    checkMissingParameterAndThrowException("minThreshold");
    myNumStates = getParameters().get("numStates").getInteger();
    myMinThreshold = getParameters().get("minThreshold").getDouble();
    validateParameterRange();
  }

  /**
   * Get a list of all required parameters for a simulation
   *
   * @return A list of strings representing the required parameter keys for this simulation
   */
  public static List<String> getRequiredParameters() {
    return List.of("minThreshold", "numStates");
  }

  private void validateParameterRange() throws InvalidParameterException {
    if (myNumStates < 1) {
      throwInvalidParameterException("numStates");
    }
    if (myMinThreshold < 0 || myMinThreshold > 1) {
      throwInvalidParameterException("minThreshold");
    }
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

    Map<Integer, Integer> neighborCount = new HashMap<>();
    for (int i = 0; i < myNumStates; i++) {
      neighborCount.put(i, 0);
    }

    List<Cell> neighbors = getNeighbors(cell, grid);
    countNeighbors(neighbors, neighborCount);

    int neighborThreshold = (int) Math.ceil(myMinThreshold * neighbors.size());

    return checkForWinner(myNumStates, currentState, neighborCount, neighborThreshold);
  }

  /**
   * gets the total number of states
   *
   * @return - the total number of states
   */
  @Override
  public int getNumberStates() {
    return myNumStates;
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
