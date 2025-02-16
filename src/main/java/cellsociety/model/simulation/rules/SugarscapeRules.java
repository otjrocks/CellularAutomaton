package cellsociety.model.simulation.rules;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.SimulationRules;
import cellsociety.model.simulation.rules.FallingSandRules.State;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A rules class to to implement the sugarscape simulation
 *
 * @Author Justin Aronwald
 */
public class SugarscapeRules extends SimulationRules {

  public SugarscapeRules(Map<String, Parameter<?>> parameters)
    throws InvalidParameterException {
    super(parameters);
  }

  // An enum to store the possible states for the simulation
  public enum State {
    EMPTY, PATCHES, AGENTS;

    public int getValue() {
      return ordinal();
    }
  }



  /**
   * @param cell -  individual cell from grid
   * @param grid - the collection of cell objects representing the grid
   * @return - the next state of a cell based on the rules of Sugarscape Model
   */

  @Override
  public int getNextState(Cell cell, Grid grid) {
    return 0;
  }

  /**
   * @param cell -  individual cell from grid
   * @param grid - the list of cell objects representing the grid
   * @return -  a list of cell objects representing the neighbors of the cell (adjacent and
   * NOT diagonals)
   */
  public List<Cell> getNeighbors(Cell cell, Grid grid) {
    return super.getNeighbors(cell, grid, false);
  }


  /**
   * The simulation has 3 states: EMPTY, PATCH, AGENT
   *
   * @return 3 for number of states
   */
  @Override
  public int getNumberStates() {
    return SugarscapeRules.State.values().length;
  }

  private Map<String, Parameter<?>> setDefaultParameters() {
    Map<String, Parameter<?>> parameters = new HashMap<>();
    parameters.put("pathSugarGrowBackRate", new Parameter<>(4));
    parameters.put("pathSugarGrowBackInteral", new Parameter<>(3));
    parameters.put("agentVision", new Parameter<>(3));
    parameters.put("agentSugar", new Parameter<>(10));
    parameters.put("agentMetabolism", new Parameter<>(2));
    return parameters;
  }

}
