package cellsociety;

import cellsociety.model.Grid;
import cellsociety.model.cell.DefaultCell;
import cellsociety.model.simulation.SimulationData;
import cellsociety.model.simulation.types.GameOfLife;
import cellsociety.model.simulation.types.GameOfLifeRules;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class GameOfLifeExample {

  public static void main(String[] args) throws InterruptedException {
    int width = 10, height = 10;
    Grid grid = new Grid(width, height);

    // Add a glider pattern: Asked ChatGPT for helping make the glider for the simulation
    grid.addCell(new DefaultCell(1, new Point2D.Double(1, 0)));
    grid.addCell(new DefaultCell(1, new Point2D.Double(2, 1)));
    grid.addCell(new DefaultCell(1, new Point2D.Double(0, 2)));
    grid.addCell(new DefaultCell(1, new Point2D.Double(1, 2)));
    grid.addCell(new DefaultCell(1, new Point2D.Double(2, 2)));
    // Initialize every point in the grid to a DefaultCell with state 0
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        grid.addCell(new DefaultCell(0, new Point2D.Double(x, y)));
      }
    }

    GameOfLife simulation = new GameOfLife(
        new GameOfLifeRules(),
        new SimulationData("GOL", "GOL", "Author", "description", new ArrayList<>())
    );

    // Run multiple frames of the simulation
    for (int i = 0; i < 40; i++) {
      System.out.println("Generation: " + i);
      grid.printGrid();
      Thread.sleep(500);
      grid.updateGrid(simulation);
      System.out.println("\n\n\n\n");
    }
  }
}
