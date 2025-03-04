package cellsociety.config;

import cellsociety.view.config.StateDisplayConfig;
import cellsociety.view.config.StateInfo;
import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static cellsociety.config.MainConfig.LOGGER;
import static cellsociety.config.MainConfig.getMessage;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.DefaultCell;
import cellsociety.model.simulation.GetNeighbors;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.model.simulation.SimulationRules;
import cellsociety.utility.FileUtility;

/**
 * Store all information pertaining to simulations
 *
 * @author Owen Jennings
 */
public class SimulationConfig {

  private static final String SIMULATION_RULES_PACKAGE = "cellsociety.model.simulation.rules.";
  public static final String INSTRUCTIONS_FILE_PATH = "cellsociety.darwin instructions.DInstructions";
  public static final String SIMULATION_RULES_RELATIVE_PATH = "src/main/java/cellsociety/model/simulation/rules/";
  private static final ResourceBundle myInstructions = ResourceBundle.getBundle(
      INSTRUCTIONS_FILE_PATH);


  /**
   * List of all the simulation names Note: The simulation rules must follow the naming convention
   * NameRules.java, to be included. Additionally, the rules file must be located in the correct
   * rules package
   */
  public static final String[] SIMULATIONS = FileUtility.getFileNamesInDirectory(
      SIMULATION_RULES_RELATIVE_PATH, "Rules.java").toArray(new String[0]);

  /**
   * Get the list of required parameters for a given simulation name/typed Calls the static method
   * getRequiredParameters() from a simulation rules class if it exists, or returns an empty list if
   * no required parameters method is available for a class.
   *
   * @param simulationName the name of the simulation you are querying for
   * @return A list of strings representing the parameter names
   */
  public static List<String> getParameters(String simulationName) {
    validateSimulation(simulationName);
    try {
      return getRequiredParametersForSimulationRulesClass(simulationName);
    } catch (NoSuchMethodException e) {
      // if class does not have getRequiredParameters method, just return empty list (no required parameters)
      return new ArrayList<>();
    } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
      LOGGER.warn(e.getMessage());
      throw new RuntimeException("Error fetching parameters: " + e);
    }
  }

  /**
   * Get the appropriate cell type for a simulation type
   *
   * @param row            row location for created cell
   * @param col            col location for created cell
   * @param state          initial state for created cell
   * @param simulationName name/type of simulation to create cell for
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
   * @param simulationName     Type/name of the simulation you want to create
   * @param simulationMetaData MetaData for your simulation
   * @param parameters         Map of parameter values String (parameter name) -> Double (parameter
   *                           value)
   * @return the appropriate simulation object for the specified simulation name or Game of Life if
   * the simulation does not exist
   */
  public static Simulation getNewSimulation(String simulationName,
      SimulationMetaData simulationMetaData,
      Map<String, Parameter<?>> parameters)
      throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvalidParameterException {
    validateSimulation(simulationName);
    GetNeighbors myGetNeighbors = createGetNeighborInstance(simulationMetaData.neighborType(),
        simulationMetaData.layers());
    return new Simulation(getRules(simulationName, parameters, myGetNeighbors), simulationMetaData);
  }

  /**
   * Return a StateInfo with the provided display name and from the provided simulation
   *
   * @param simulation The simulation you are querying for
   * @param name       The name of the state you are querying for
   * @return The state info for the simulation and display name you are querying for
   */
  public static StateInfo getStateInfoFromDisplayName(Simulation simulation, String name) {
    int numStates = simulation.rules().getNumberStates();
    // check all possible state info for all languages until you find the state info with a display name matching the provided name
    for (String language : MainConfig.fetchLanguages()) {
      for (int i = 0; i < numStates; i++) {
        StateInfo currentStateInfo = StateDisplayConfig.getStateInfo(simulation, i, language);
        if (currentStateInfo.displayName().equalsIgnoreCase(name)) {
          return currentStateInfo;
        }
      }
    }
    return null;
  }

  private static List<String> getRequiredParametersForSimulationRulesClass(String simulationName)
      throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    // Get class name for simulation queried
    String className = String.format("%s%sRules", SIMULATION_RULES_PACKAGE, simulationName);
    Class<?> ruleClass = Class.forName(className);
    Method method = ruleClass.getDeclaredMethod("getRequiredParameters");
    @SuppressWarnings("unchecked") // call static method to get parameters
    List<String> parameters = (List<String>) method.invoke(null);
    return parameters;
  }

  private static SimulationRules getRules(String simulationName,
      Map<String, Parameter<?>> parameters, GetNeighbors myGetNeighbors)
      throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    validateSimulation(simulationName);
    String className = String.format("cellsociety.model.simulation.rules.%s%s", simulationName,
        "Rules");
    SimulationRules simulationRules;
    simulationRules = (SimulationRules) Class.forName(className)
        .getConstructor(Map.class, GetNeighbors.class)
        .newInstance(parameters, myGetNeighbors);
    return simulationRules;
  }

  private static void validateSimulation(String simulationName) {
    if (!List.of(SIMULATIONS).contains(simulationName)) {
      throw new IllegalArgumentException(
          String.format(getMessage("INVALID_SIMULATION_TYPE_ERROR"), simulationName));
    }
  }

  private static GetNeighbors createGetNeighborInstance(String neighborType, int layers)
      throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    if (neighborType == null || layers <= 0) {
      throw new IllegalArgumentException("Invalid neighbor configuration.");
    }
    String className = String.format("cellsociety.model.simulation.neighbors.%s%s",
        neighborType,
        "Neighbors");
    GetNeighbors getNeighbors;
    try {
      Class<?> neighborClass = Class.forName(className);
      getNeighbors = (GetNeighbors) neighborClass.getConstructor(int.class).newInstance(layers);
      return getNeighbors;
    } catch (ClassNotFoundException e) {
      LOGGER.warn(e.getMessage());
      throw new IllegalArgumentException(
          String.format("Invalid neighbor configuration: %s", e));
    }
  }

  /**
   * Gets instructions for predefined species from the properties file
   *
   * @param state Integer representing the species of the animal (or empty)
   */
  public static List<String> assignInstructionsFromState(int state) {
    String[] instrArray = myInstructions.getString(String.valueOf(state)).split(",");
    return new ArrayList<>(Arrays.asList(instrArray));
  }

}
