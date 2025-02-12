package cellsociety;

import cellsociety.model.Grid;
import cellsociety.model.cell.DefaultCell;
import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.model.simulation.types.GameOfLife;
import cellsociety.model.simulation.rules.RockPaperScissorsRules;
import cellsociety.model.simulation.types.RockPaperScissors;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RockPaperScissorsExample {

  public static void main(String[] args) throws InterruptedException {
    int width = 3, height = 3;
    Grid grid = new Grid(width, height);

    // Parameters for the simulation
    Map<String, Double> parameters = new HashMap<>();
    parameters.put("numStates", 5.0);
    parameters.put("minThreshold", 0.49); // At least 50% of neighbors to win

    Random random = new Random();

    /*
    // Populate the grid with random states (excluding 0, which means empty)
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        int state = random.nextInt(5) + 1; // Random state from 1 to 5
        grid.addCell(new DefaultCell(state, new Point2D.Double(x, y)));
      }
    }
    */
    // Add some specific patterns for testing
    grid.addCell(new DefaultCell(3, new Point2D.Double(0, 0)));
    grid.addCell(new DefaultCell(4, new Point2D.Double(0, 1)));
    grid.addCell(new DefaultCell(2, new Point2D.Double(0, 2)));
    grid.addCell(new DefaultCell(1, new Point2D.Double(1, 0)));
    grid.addCell(new DefaultCell(3, new Point2D.Double(1, 1)));
    grid.addCell(new DefaultCell(4, new Point2D.Double(1, 2)));
    grid.addCell(new DefaultCell(2, new Point2D.Double(2, 0)));
    grid.addCell(new DefaultCell(1, new Point2D.Double(2, 1)));
    grid.addCell(new DefaultCell(3, new Point2D.Double(2, 2)));






    RockPaperScissorsRules rules = new RockPaperScissorsRules(parameters);
    Simulation simulation = new RockPaperScissors(
        rules,
        new SimulationMetaData(
            "RockPaperScissors", "RockPaperScissors", "Author", "Rock-Paper-Scissors Simulation"
        )
    );

    for (int i = 0; i < 10; i++) {
      System.out.println("Step: " + i);
      grid.printGrid();
      Thread.sleep(500);
      grid.updateGrid(simulation);
      System.out.println("\n\n\n\n");
    }
  }
}
