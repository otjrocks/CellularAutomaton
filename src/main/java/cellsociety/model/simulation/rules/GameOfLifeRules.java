package cellsociety.model.simulation.rules;

import cellsociety.model.simulation.getNeighborOptions.MooreNeighbors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.SimulationRules;

public class GameOfLifeRules extends SimulationRules {

  private String myRuleString;
  private ArrayList<Integer> birthValues;
  private ArrayList<Integer> surviveValues;
  private GetNeighbors myGetNeighbors;

  public GameOfLifeRules(Map<String, Parameter<?>> parameters, GetNeighbors myGetNeighbors) throws InvalidParameterException {
    super(parameters, myGetNeighbors);
    if (parameters == null || parameters.isEmpty()) {
      myRuleString = "B3/S23";
      birthValues = new ArrayList<>(List.of(3));
      surviveValues = new ArrayList<>(Arrays.asList(2, 3));
    } else {
      checkMissingParameterAndThrowException("ruleString");
      myRuleString = getParameters().get("ruleString").getString();
      validateParameterRange();
      initializeBSValues();
    }
  }

  private void validateParameterRange() throws InvalidParameterException {
    if (myRuleString.equals("")) {
      myRuleString = "B3/S23";
    }
    if (!myRuleString.contains("/") || !myRuleString.contains("B") || !myRuleString.contains("S")) {
      throwInvalidParameterException("ruleString");
    }
  }

  /**
   * Get a list of all required parameters for a simulation
   *
   * @return A list of strings representing the required parameter keys for this simulation
   */
  public static List<String> getRequiredParameters() {
    return List.of("ruleString");
  }


  /**
   * Game of Life: Any cell with fewer than 2 neighbors dies due to underpopulation Any cell with 2
   * - 3 neighbors moves on to the next generation Any cell with more than 3 neighbors dies due to
   * overpopulation Any inactive cell with exactly 3 neighbors becomes active
   *
   * @param cell - individual cell from grid
   * @return the next state of a cell based on the rules of game of life
   */
  @Override
  public int getNextState(Cell cell, Grid grid) {

    if (cell.getRow() >= grid.getRows() || cell.getRow() < 0 || cell.getCol() >= grid.getCols()
        || cell.getCol() < 0) {
      throw new IndexOutOfBoundsException("Cell position out of bounds");
    }

    List<Cell> neighbors = getNeighbors(cell, grid);
    int aliveNeighbors = 0;
    for (Cell neighbor : neighbors) {
      if (neighbor.getState() == 1) {
        aliveNeighbors++;
      }
    }
    if (birthValues.contains(aliveNeighbors) && cell.getState()==0) {
      return 1;
    } else if (!surviveValues.contains(aliveNeighbors)) {
      return 0;
    }
    return cell.getState();
  }

  private void initializeBSValues()
      throws InvalidParameterException {

    String[] bStrings = myRuleString.split("/")[0].substring(1).split("");
    String[] sStrings = myRuleString.split("/")[1].substring(1).split("");

    Integer[] bValues = convertStringArray(bStrings);
    Integer[] sValues = convertStringArray(sStrings);

    surviveValues = new ArrayList<>(Arrays.asList(sValues));
    birthValues = new ArrayList<>(Arrays.asList(bValues));
  }

  private Integer[] convertStringArray(String[] strings) {
    Integer[] ints = new Integer[strings.length];
    for (int i = 0; i < strings.length; i++) {
      ints[i] = Integer.valueOf(strings[i]);
    }
    return ints;
  }

  @Override
  public int getNumberStates() {
    return 2;
  }
}
