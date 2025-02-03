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
import java.util.Map;

/**
 * Store all information pertaining to simulations
 *
 * @author Owen Jennings
 */
public class SimulationConfig {

  public static final String[] simulations = new String[]{
      "GameOfLife",
      "Percolation",
      "Segregation",
      "SpreadingOfFire",
      "WaTorWorld"
  };

  public static Cell getNewCell(int row, int col, int state, String simulationName) {
    if (simulationName.equals("WaTorWorld")) {
      return new WaTorCell(state, new Double(row, col));
    }
    return new DefaultCell(state, new Point2D.Double(row, col));
  }

  public static Simulation getNewSimulation(String simulationName,
      SimulationMetaData simulationMetaData,
      Map<String, java.lang.Double> parameters) {
    return switch (simulationName) {
      case "Percolation" -> new Percolation(new PercolationRules(), simulationMetaData);
      case "Segregation" ->
          new SegregationModel(new SegregationModelRules(parameters), simulationMetaData);
      case "SpreadingOfFire" -> new SpreadingOfFire(new SpreadingOfFireRules(), simulationMetaData);
      case "WaTorWorld" -> new WaTorWorld(new WaTorWorldRules(parameters), simulationMetaData);
      default ->
          new GameOfLife(new GameOfLifeRules(), simulationMetaData); // default game is GameOfLife
    };
  }
}
