package cellsociety.config;

import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static cellsociety.config.MainConfig.getMessage;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.DefaultCell;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.model.simulation.SimulationRules;

/**
 * Store all information pertaining to simulations
 *
 * @author Owen Jennings
 */
public class SimulationConfig {

  public static final String VALUES_FILE_PATH = "cellsociety.state values.StateValues";
  private static final ResourceBundle myValues = ResourceBundle.getBundle(
      VALUES_FILE_PATH);
  /**
   * List of all the simulation names
   */
  public static final String[] SIMULATIONS = new String[]{
      "GameOfLife",
      "Percolation",
      "Segregation",
      "SpreadingOfFire",
      "WaTorWorld",
      "RockPaperScissors",
      "FallingSand",
      "ForagingAnts"
  };

  /**
   * Map of all the required parameters for a given simulation
   */
  private static final Map<String, List<String>> PARAMETERS = Map.of(
      "GameOfLife", List.of("ruleString"),
      "Segregation", List.of("toleranceThreshold"),
      "SpreadingOfFire", List.of("growInEmptyCell", "ignitionWithoutNeighbors"),
      "WaTorWorld", List.of("sharkReproductionTime", "sharkEnergyGain", "fishReproductionTime"),
      "RockPaperScissors", List.of("minThreshold", "numStates"),
      "ForagingAnts", List.of("pheromoneDecayRate","maxPheromoneAmount", "antReproductionTime")
  );

  /**
   * Get the list of required parameters for a given simulation name/typed
   *
   * @param simulationName: the name of the simulation you are querying for
   * @return A list of strings representing the parameter names
   */
  public static List<String> getParameters(String simulationName) {
    if (!PARAMETERS.containsKey(simulationName)) {
      return new ArrayList<>();
    }
    return PARAMETERS.get(simulationName);
  }

  /**
   * Get the appropriate cell type for a simulation type
   *
   * @param row:            row location for created cell
   * @param col:            col location for created cell
   * @param state:          initial state for created cell
   * @param simulationName: name/type of simulation to create cell for
   * @return the appropriate cell for a given simulation or default cell if the simulation does not
   * exist
   */
  public static Cell getNewCell(int row, int col, int state, String simulationName) {
    validateSimulation(simulationName);
    String className = String.format("cellsociety.model.cell.%sCell", simulationName);
    try {
      Class<?> cellClass = Class.forName(className);
      return (Cell) cellClass.getConstructor(int.class, Point2D.class)
          .newInstance(state, new Point2D.Double(row, col));
    } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
             IllegalAccessException | InvocationTargetException e) {
      // If specialized cell type does not exist for a simulation or an error occurs
      // while trying to get specialized cell, fallback to default cell type
      return new DefaultCell(state, new Point2D.Double(row, col));
    }
  }

  /**
   * Get the appropriate simulation class for a given simulation name. Construct the simulation with
   * the required parameters and metadata
   *
   * @param simulationName:     Type/name of the simulation you want to create
   * @param simulationMetaData: MetaData for your simulation
   * @param parameters:         Map of parameter values String (parameter name) -> Double (parameter
   *                            value)
   * @return the appropriate simulation object for the specified simulation name or Game of Life if
   * the simulation does not exist
   */
  public static Simulation getNewSimulation(String simulationName,
      SimulationMetaData simulationMetaData,
      Map<String, Parameter<?>> parameters)
      throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvalidParameterException {
    validateSimulation(simulationName);
    return new Simulation(getRules(simulationName, parameters), simulationMetaData);
  }

  private static SimulationRules getRules(String simulationName,
      Map<String, Parameter<?>> parameters)
      throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, InvalidParameterException {
    validateSimulation(simulationName);
    String className = String.format("cellsociety.model.simulation.rules.%s%s", simulationName,
        "Rules");
    SimulationRules simulationRules;
    simulationRules = (SimulationRules) Class.forName(className).getConstructor(Map.class)
        .newInstance(parameters);
    return simulationRules;
  }

  private static void validateSimulation(String simulationName) {
    if (!List.of(SIMULATIONS).contains(simulationName)) {
      throw new IllegalArgumentException(
          String.format(getMessage("INVALID_SIMULATION_TYPE_ERROR"), simulationName));
    }
  }

  public static int returnStateValueBasedOnName(String simType, String name){
    String stateKey = simType + "_VALUE_" + name;
    try {
      String valueStr = myValues.getString(stateKey.toUpperCase());
      Integer value = Integer.valueOf(valueStr);
      return value;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return 0;
    }
  }

}
