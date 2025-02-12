package cellsociety.config;

import static cellsociety.config.MainConfig.getMessages;

import cellsociety.model.simulation.rules.RockPaperScissorsRules;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.DefaultCell;
import cellsociety.model.cell.WaTorCell;
import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.model.simulation.SimulationRules;
import cellsociety.model.simulation.rules.GameOfLifeRules;
import cellsociety.model.simulation.rules.PercolationRules;
import cellsociety.model.simulation.rules.SegregationModelRules;
import cellsociety.model.simulation.rules.SpreadingOfFireRules;
import cellsociety.model.simulation.rules.WaTorWorldRules;

/**
 * Store all information pertaining to simulations
 *
 * @author Owen Jennings
 */
public class SimulationConfig {

  /**
   * List of all the simulation names
   */
  public static final String[] simulations = new String[]{
      "GameOfLife",
      "Percolation",
      "Segregation",
      "SpreadingOfFire",
      "WaTorWorld",
      "RockPaperScissors"
  };

  private static Point2D getParameterRange(String parameter) {
    switch (parameter) {
      case "sharkReproductionTime", "fishReproductionTime", "sharkEnergyGain" -> {
        return new Point2D.Double(0, 10);
      }
      case "numStates" -> {  // number of states for rock paper scissors should be in range [3,20]
        return new Point2D.Double(3, 20);
      }
      default -> {
        return new Point2D.Double(0, 1);
      }
      // unless specified otherwise above,
      // parameters must be a double in the range [0, 1]
      // representing a probability of some event or thing occurring.
    }
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
    if (simulationName.equals("WaTorWorld")) {
      return new WaTorCell(state, new Double(row, col));
    }
    return new DefaultCell(state, new Point2D.Double(row, col));
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
      Map<String, String> parameters) {
    validateSimulation(simulationName);
    validateParameters(simulationName, parameters);
    if (simulationName.equals("GameOfLife")) {
      return new Simulation(new GameOfLifeRules(parameters), simulationMetaData);
    }
    return new Simulation(getRules(simulationName, convertMap(parameters)), simulationMetaData);
  }

  private static SimulationRules getRules(String simulationName,
      Map<String, java.lang.Double> parameters) {
    validateSimulation(simulationName);
    switch (simulationName) {
      case "Percolation" -> {
        return new PercolationRules();
      }
      case "Segregation" -> {
        return new SegregationModelRules(parameters);
      }
      case "SpreadingOfFire" -> {
        return new SpreadingOfFireRules();
      }
      case "WaTorWorld" -> {
        return new WaTorWorldRules(parameters);
      }
      case "RockPaperScissors" -> {
        return new RockPaperScissorsRules(parameters);
      }
      default -> { // default is game of life
        return new GameOfLifeRules();
      }
    }
  }

  /**
   * Get the list of required parameters for a given simulation name/typed
   *
   * @param simulationName: the name of the simulation you are querying for
   * @return A list of strings representing the parameter names
   */
  public static List<String> getParameters(String simulationName) {
    validateSimulation(simulationName);
    switch (simulationName) {
      case "Segregation" -> {
        return List.of("toleranceThreshold");
      }
      case "SpreadingOfFire" -> {
        return List.of("growInEmptyCell", "ignitionWithoutNeighbors");
      }
      case "WaTorWorld" -> {
        return List.of("sharkReproductionTime", "sharkEnergyGain", "fishReproductionTime");
      }
      case "RockPaperScissors" -> {
        return List.of("minThreshold", "numStates");
      }
      default -> {
        return new ArrayList<>();
      }
    }
  }

  private static void validateSimulation(String simulationName) {
    if (!List.of(simulations).contains(simulationName)) {
      throw new IllegalArgumentException(
          String.format(getMessages().getString("INVALID_SIMULATION_TYPE_ERROR"), simulationName));
    }
  }

  private static void validateParameters(String simulationName,
      Map<String, String> parameters) {
    List<String> requiredParameters = getParameters(simulationName);
    for (String parameter : requiredParameters) {
      if (parameters == null) {
        throw new NullPointerException(
            getMessages().getString("NULL_SIMULATION_PARAMETERS_ERROR"));
      }
      if (!parameters.containsKey(parameter)) {
        throw new IllegalArgumentException(
            String.format(getMessages().getString("MISSING_SIMULATION_PARAMETER_ERROR"), parameter));
      }
      validateParameterRange(parameter, java.lang.Double.valueOf(parameters.get(parameter)));
    }
  }

  private static Map<String, java.lang.Double> convertMap(Map<String, String> stringMap) {
    Map<String, java.lang.Double> paramMap = new HashMap<>();
    for (String key : stringMap.keySet()) {
      String value = stringMap.get(key);
      if (value != null) {
        try {
          paramMap.put(key, java.lang.Double.valueOf(value));
        } catch (NumberFormatException e) {
          System.err.println("Warning: Invalid number format for key: " + key);
        }
      }
    }
    return paramMap;
  }

  // ensure that a specified parameter is within a valid range for the parameter
  private static void validateParameterRange(String parameter, java.lang.Double value) {
    Point2D validRange = getParameterRange(parameter);
    if (value < validRange.getX()) {
      throw new IllegalArgumentException(
          String.format(getMessages().getString("PARAMETER_TOO_SMALL"), parameter, validRange.getX()));
    }
    if (value > validRange.getY()) {
      throw new IllegalArgumentException(
          String.format(getMessages().getString("PARAMETER_TOO_LARGE"), parameter, validRange.getY()));
    }
  }

}
