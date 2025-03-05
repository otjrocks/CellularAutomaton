package cellsociety.model.simulation.rules;

import cellsociety.model.simulation.neighbors.GetNeighbors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;

/**
 * The implementation of Game of Life.
 *
 * @author Justin Aronwald
 * @author Owen Jennings
 * @author Troy Ludwig
 */
public class GameOfLifeRules extends SimulationRules {

  private static final String backSlash = "/";
  private static final String B = "B";
  private static final String S = "S";
  private static final int aliveState = 1;
  public static final String RULE_STRING = "ruleString";
  private String myRuleString;
  private List<Integer> birthValues;
  private List<Integer> surviveValues;

  /**
   * A default constructor for Game Of Life.
   *
   * @param parameters     The parameters provided to initialize this simulation
   * @param myGetNeighbors The neighbor policy to use
   * @throws InvalidParameterException An exception if invalid parameters are provided or parameters
   *                                   are missing
   */
  public GameOfLifeRules(Map<String, Parameter<?>> parameters, GetNeighbors myGetNeighbors)
      throws InvalidParameterException {
    super(parameters, myGetNeighbors);
    if (parameters == null || parameters.isEmpty()) {
      myRuleString = "B3/S23";
      birthValues = new ArrayList<>(List.of(3));
      surviveValues = new ArrayList<>(Arrays.asList(2, 3));
    } else {
      checkMissingParameterAndThrowException(RULE_STRING);
      myRuleString = getParameters().get(RULE_STRING).getString();
      validateParameterRange();
      initializeBSValues();
    }
  }

  private void validateParameterRange() throws InvalidParameterException {
    if (myRuleString.isEmpty()) {
      myRuleString = "B3/S23";
    }
    if (checkInvalidRuleString()) {
      throwInvalidParameterException(RULE_STRING);
    }
  }

  private boolean checkInvalidRuleString() {
    return !myRuleString.contains(backSlash) ||
        !myRuleString.contains(B) ||
        !myRuleString.contains(S);
  }

  /**
   * Get a list of all required parameters for a simulation.
   *
   * @return A list of strings representing the required parameter keys for this simulation
   */
  public static List<String> getRequiredParameters() {
    return List.of(RULE_STRING);
  }


  /**
   * Game of Life: Any cell with fewer than 2 neighbors dies due to underpopulation Any cell with 2
   * - 3 neighbors moves on to the next generation Any cell with more than 3 neighbors dies due to
   * overpopulation Any inactive cell with exactly 3 neighbors becomes active.
   *
   * @param cell - individual cell from grid
   * @return the next state of a cell based on the rules of game of life
   */
  @Override
  public int getNextState(Cell cell, Grid grid) {
    int aliveNeighbors = calculateAliveNeighbors(cell, grid);
    return getNextStateBasedOnAliveNeighbors(cell, aliveNeighbors);
  }

  private int getNextStateBasedOnAliveNeighbors(Cell cell, int aliveNeighbors) {
    if (birthValues.contains(aliveNeighbors) && cell.getState() == 0) {
      return 1;
    } else if (!surviveValues.contains(aliveNeighbors)) {
      return 0;
    }
    return cell.getState();
  }

  private int calculateAliveNeighbors(Cell cell, Grid grid) {
    List<Cell> neighbors = getNeighbors(cell, grid);
    int aliveNeighbors = 0;
    for (Cell neighbor : neighbors) {
      if (neighbor.getState() == aliveState) {
        aliveNeighbors++;
      }
    }
    return aliveNeighbors;
  }

  private void initializeBSValues() {

    String[] bStrings = myRuleString.split("/")[0].substring(1).split("");
    String[] sStrings = myRuleString.split("/")[1].substring(1).split("");

    Integer[] bValues = convertStringArray(bStrings);
    Integer[] sValues = convertStringArray(sStrings);

    surviveValues = new ArrayList<>(Arrays.asList(sValues));
    birthValues = new ArrayList<>(Arrays.asList(bValues));
  }

  private Integer[] convertStringArray(String[] strings) {
    Integer[] values = new Integer[strings.length];
    for (int i = 0; i < strings.length; i++) {
      values[i] = Integer.valueOf(strings[i]);
    }
    return values;
  }

  @Override
  public int getNumberStates() {
    return 2;
  }
}
