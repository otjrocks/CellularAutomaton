package cellsociety;

import cellsociety.model.Grid;
import cellsociety.model.cell.DefaultCell;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.model.simulation.types.Percolation;
import cellsociety.model.simulation.rules.PercolationRules;
import java.awt.geom.Point2D;
import java.util.Random;

public class PercolationExample {

  public static void main(String[] args) throws InterruptedException {
    int width = 10, height = 10;
    Grid grid = new Grid(width, height);

    //randomness aspect generated from ChatGPT
    Random random = new Random();
    double openProbability = 0.7; // 70% chance to be open, 30% chance to be blocked

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        int state = (random.nextDouble() < openProbability) ? 1 : 0;
        grid.addCell(new DefaultCell(state, new Point2D.Double(x, y)));
      }
    }

    grid.addCell(new DefaultCell(0, new Point2D.Double(4, 3)));
    grid.addCell(new DefaultCell(0, new Point2D.Double(5, 3)));
    grid.addCell(new DefaultCell(0, new Point2D.Double(4, 6)));
    grid.addCell(new DefaultCell(0, new Point2D.Double(5, 6)));

    Percolation percolation = new Percolation(
        new PercolationRules(),
        new SimulationMetaData("Percolation", "Percolation", "Author", "Percolation Simulation")
    );

    for (int i = 0; i < 40; i++) {
      System.out.println("Step: " + i);
      grid.printGrid();
      Thread.sleep(500);
      grid.updateGrid(percolation);
      System.out.println("\n\n\n\n");
    }
  }
}