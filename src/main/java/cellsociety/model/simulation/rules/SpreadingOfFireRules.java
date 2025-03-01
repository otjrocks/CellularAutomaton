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
import java.util.Random;

/**
 * The implementation of Spreading of Fire Simulation
 * <p>
 * For a Forest Fire cell, there can be 3 states
 * <p>
 * A cell with state 0 indicates it's empty
 * <p>
 * A cell with state 1 indicates it's occupied by a tree
 * <p>
 * A cell with state 2 indicates it's burning
 *
 * @author Justin Aronwald
 */
public class SpreadingOfFireRules extends SimulationRules {

  public static final String GROW_IN_EMPTY_CELL = "growInEmptyCell";
  public static final String IGNITION_WITHOUT_NEIGHBORS = "ignitionWithoutNeighbors";
  private final Random random = new Random();
  private final double myGrowthInEmptyCell;
  private final double myIgnitionWithoutNeighbors;

  /**
   * The default constructor of a Spreading of Fire rules.
   *
   * @param parameters The required parameters map
   * @throws InvalidParameterException This is thrown for invalid parameters provided.
   */
  public SpreadingOfFireRules(Map<String, Parameter<?>> parameters, GetNeighbors myGetNeighbors)
      throws InvalidParameterException {
    super(parameters, myGetNeighbors);
    if (parameters == null || parameters.isEmpty()) {
      setParameters(setDefaultParameters());
    }
    checkMissingParameterAndThrowException(GROW_IN_EMPTY_CELL);
    checkMissingParameterAndThrowException(IGNITION_WITHOUT_NEIGHBORS);
    myGrowthInEmptyCell = getParameters().get(GROW_IN_EMPTY_CELL).getDouble();
    myIgnitionWithoutNeighbors = getParameters().get(IGNITION_WITHOUT_NEIGHBORS).getDouble();
    validateParameterRange();
  }

  private void validateParameterRange() throws InvalidParameterException {
    if (myGrowthInEmptyCell < 0 || myGrowthInEmptyCell > 1) {
      throwInvalidParameterException(GROW_IN_EMPTY_CELL);
    }
    if (myIgnitionWithoutNeighbors < 0 || myIgnitionWithoutNeighbors > 1) {
      throwInvalidParameterException(IGNITION_WITHOUT_NEIGHBORS);
    }
  }

  /**
   * Get a list of all required parameters for a simulation
   *
   * @return A list of strings representing the required parameter keys for this simulation
   */
  public static List<String> getRequiredParameters() {
    return List.of(GROW_IN_EMPTY_CELL, IGNITION_WITHOUT_NEIGHBORS);
  }

  /**
   * Forest Fire: A burning cell (2) turns into an empty cell A tree (1) will burn if at least one
   * neighbor is burning A tree ignites with probability f (0.15) even if no neighbor is burning An
   * empty cell (0) fills with a tree with probability p (0.1)
   *
   * @param cell - individual cell from grid
   * @param grid - the list of cell objects representing the grid
   * @return - the next state of a cell based on the rules of the forest fire model
   */
  @Override
  public int getNextState(Cell cell, Grid grid) {
    int currentState = cell.getState();
    return getNextStateFromCurrentState(cell, grid, currentState);
  }

  private int getNextStateFromCurrentState(Cell cell, Grid grid, int currentState) {
    if (currentState == 2) { // burning to empty
      return 0;
    }
    if (currentState == 0) { // grow tree randomly
      return growTreeRandomly();
    }
    if (treeNeighborIsBurning(cell, grid)) { // burn if neighbor is burning
      return 2;
    }
    return randomIgnitionOfTreeCell(currentState); // randomly ignite if tree
  }

  private boolean treeNeighborIsBurning(Cell cell, Grid grid) {
    List<Cell> neighbors = getNeighbors(cell, grid);
    for (Cell neighbor : neighbors) {
      if (cell.getState() == 1 && neighbor.getState() == 2) { // Neighbor is burning
        return true;
      }
    }
    return false;
  }

  private int randomIgnitionOfTreeCell(int currentState) {
    if (currentState == 1 && (random.nextDouble() < myIgnitionWithoutNeighbors)) {
      return 2;
    }
    return currentState;
  }

  private int growTreeRandomly() {
    if (random.nextDouble() < myGrowthInEmptyCell) {
      return 1;
    }
    return 0;
  }

  /**
   * gets the total number of states
   *
   * @return - 3, the total number of states
   */
  @Override
  public int getNumberStates() {
    return 3;
  }

  private Map<String, Parameter<?>> setDefaultParameters() {
    Map<String, Parameter<?>> parameters = new HashMap<>();
    parameters.put(IGNITION_WITHOUT_NEIGHBORS, new Parameter<>("0.15"));
    parameters.put(GROW_IN_EMPTY_CELL, new Parameter<>("0.1"));
    return parameters;
  }
}
