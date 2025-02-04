package cellsociety.config;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.DefaultCell;
import cellsociety.model.cell.WaTorCell;
import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.model.simulation.rules.GameOfLifeRules;
import cellsociety.model.simulation.rules.PercolationRules;
import cellsociety.model.simulation.rules.SegregationModelRules;
import cellsociety.model.simulation.rules.SpreadingOfFireRules;
import cellsociety.model.simulation.rules.WaTorWorldRules;
import cellsociety.model.simulation.types.GameOfLife;
import cellsociety.model.simulation.types.Percolation;
import cellsociety.model.simulation.types.SegregationModel;
import cellsociety.model.simulation.types.SpreadingOfFire;
import cellsociety.model.simulation.types.WaTorWorld;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
      "WaTorWorld"
  };

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
      Map<String, java.lang.Double> parameters) {
    return switch (simulationName) {
      case "Percolation" -> new Percolation(new PercolationRules(), simulationMetaData);
      case "Segregation" ->
          new SegregationModel(new SegregationModelRules(parameters), simulationMetaData);
      case "SpreadingOfFire" ->
          new SpreadingOfFire(new SpreadingOfFireRules(parameters), simulationMetaData);
      case "WaTorWorld" -> new WaTorWorld(new WaTorWorldRules(parameters), simulationMetaData);
      default ->
          new GameOfLife(new GameOfLifeRules(), simulationMetaData); // default game is GameOfLife
    };
  }

  /**
   * Get the list of required parameters for a given simulation name/typed
   *
   * @param simulationName: the name of the simulation you are querying for
   * @return A list of strings representing the parameter names
   */
  public static List<String> getParameters(String simulationName) {
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
      default -> {
        return new ArrayList<>();
      }
    }
  }
}
