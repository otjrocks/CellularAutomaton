package cellsociety.model.simulation;

import static cellsociety.config.MainConfig.getMessage;

import cellsociety.model.cell.DefaultCell;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.CellUpdate;

/**
 * An abstract class to handle the rules of a cellular automaton simulation.
 *
 * @author Owen Jennings, Troy Ludwig, Justin Aronwald
 */
public abstract class SimulationRules {

  private Map<String, Parameter<?>> myParameters;
  private final GetNeighbors myGetNeighbors;

  /**
   * The default constructor of a simulation rules class
   *
   * @param parameters The parameters map for the simulation rule. Each simulation rules
   *                   implementation should validate and ensure that parameters are present and
   *                   correct in the constructor. If no parameters are required for a given rules
   *                   class, then this can be nulll
   * @throws InvalidParameterException This exception should be thrown whenever a simulation rules
   *                                   instance is created with invalid or missing parameter
   *                                   values.
   */
  public SimulationRules(Map<String, Parameter<?>> parameters, GetNeighbors getNeighbors)
      throws InvalidParameterException {
    myParameters = parameters;
    myGetNeighbors = getNeighbors;
  }

  /**
   * Get the neighbors for a provided cell and grid
   *
   * @param cell The cell you are querying for
   * @param grid The grid you are querying in
   * @return A list of cell's representing the provided cell's neighbors
   */
  public List<Cell> getNeighbors(Cell cell, Grid grid) {
    return myGetNeighbors.getNeighbors(cell, grid);
  }

  /**
   * Getter for the layers of the neighbor configuration
   *
   * @return the number of layers to look for in a direction
   */
  public int getLayers() {
    return myGetNeighbors.getLayers();
  }

  /**
   * Get the total amount of directions to look in - the step size
   *
   * @return - the number of directions to look towards for each configuration
   */
  public int getStepSize() {
    return myGetNeighbors.getStepSize();
  }

  /**
   * Set the parameters for the current simulation rules.
   *
   * @param parameters A map where the key is the parameter name and the value is a parameter value
   * @see Parameter for implementation of the parameter value wrapper class.
   */
  public void setParameters(Map<String, Parameter<?>> parameters) {
    myParameters = parameters;
  }

  /**
   * Get the parameters map for the current simulation rules class.
   *
   * @return A map where the key is the parameter name and the value is a parameter value
   * @see Parameter for implementation of the parameter value wrapper class.
   */
  public Map<String, Parameter<?>> getParameters() {
    return Map.copyOf(myParameters);
  }

  /**
   * Get the GetNeighbor object for the active simulation rules class
   *
   * @return - the configuration of neighbor and layers
   */
  public GetNeighbors getNeighborConfig() {
    return myGetNeighbors;
  }

  /**
   * Get a list of all required parameters for a simulation. If a simulation rules set requires
   * parameters, then this method should be overwritten to declare all required parameters for the
   * simulation. If no parameters are required, then you do not need to implement this static
   * method.
   *
   * @return A list of strings representing the required parameter keys for this simulation
   */
  public static List<String> getRequiredParameters() {
    return new ArrayList<>();
  }


  /*
  A simulation's state is updated through the following 2 methods: getNextStates and getNextStatesForAllCells.
  By default, getNextState returns the current state of a cell.
  getNextState is used by the default implementation of getNextStatesForAllCells
  which returns a list of CellUpdates indicating the cells that need to be updated in the grid on the next step update.
  A specific implementation of SimulationRules must overwrite or implement one or both of these methods to function.
  By overwriting getNextStates, you indicate all the next states for the simulation step, which will be used by getNextStatesForAllCells.
  If a specific simulation rules requires updating grid state in a specific order or updating cell state while determining next state (such as changing a cells health), then
  getNextStatesForAllCells can be overwritten.
  */

  /**
   * Get the next state of a cell based on its neighbors which can be found using getNeighbors or
   * through interaction with the entire grid. This method should be overwritten if the default
   * implementation of getNextStatesForAllCells is used. If a given simulation rules class decides
   * to overwrite the getNextStatesForAllCells method and finds the next states in that, then this
   * does not need to be overwritten. This method requires that a cell be in bounds of the grid.
   *
   * @param cell The cell you are trying to find the next state for.
   * @param grid The grid that the cell is a part of
   * @return An int representing the next cell state for a provided cell.
   */
  public int getNextState(Cell cell, Grid grid) {
    return cell.getState();
  }

  /**
   * A default implementation of the algorithm to get the cellUpdates for all cells in the grid.
   * Note, this default implementation will call getNextState to get the next state values for all
   * cells in the grid. The CellUpdate will then create a cellUpdate with a default cell to provide
   * state update.
   * <p>
   * If a specific simulation uses a different cell type or needs to modify the state of cells at
   * the same time as determining the next states of the entire grid, this method should be
   * overwritten.
   *
   * @param grid The grid that you wish to get the next states for
   * @return A list of cell updates that should occur
   * @see CellUpdate
   */
  public List<CellUpdate> getNextStatesForAllCells(Grid grid) {
    List<CellUpdate> nextStates = new ArrayList<>(); // calculate next states in first pass, then update all next states in second pass
    Iterator<Cell> cellIterator = grid.getCellIterator();
    while (cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      // next cell is a new default cell with the next state based on rules
      // if specific simulation has a specific cell type, it should override this default implementation
      // only add cell state update if the cell acquires a new state
      createCellUpdateIfCellStateChanged(grid, cell, nextStates);
    }
    return nextStates;
  }

  /**
   * Get the total number of states for a simulation rule. By design, the first state must be 0 and
   * the last state must be the total number of states - 1. For example, if a simulation rules has 4
   * states, then the integer representation of the four states must be the sequential [0, 1, 2, 3]
   *
   * @return An int representing the total number of states for a simulation rule.
   */
  public abstract int getNumberStates();

  /**
   * A helper method to check for a missing parameter for a simulation
   *
   * @param parameter The parameter you want to check if is missing.
   */
  public void checkMissingParameterAndThrowException(String parameter) {
    if (!getParameters().containsKey(parameter)) {
      throw new IllegalArgumentException(
          String.format(getMessage("MISSING_SIMULATION_PARAMETER_ERROR"), parameter));
    }
  }

  /**
   * Throw an invalid parameter exception that includes the provided string.
   *
   * @param exceptionMessage The message you want to include in the exceptions message.
   * @throws InvalidParameterException The exception that is thrown by this method.
   */
  public static void throwInvalidParameterException(String exceptionMessage)
      throws InvalidParameterException {
    throw new InvalidParameterException(
        String.format(getMessage("INVALID_PARAMETER"), exceptionMessage));
  }

  private void createCellUpdateIfCellStateChanged(Grid grid, Cell cell,
      List<CellUpdate> nextStates) {
    if (getNextState(cell, grid) != grid.getCell(cell.getLocation()).getState()) {
      Cell nextCell = new DefaultCell(getNextState(cell, grid), cell.getLocation());
      nextStates.add(new CellUpdate(cell.getLocation(), nextCell));
    }
  }
}
