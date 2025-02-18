package cellsociety;

import cellsociety.model.Grid;
import cellsociety.model.cell.AgentCell;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.cell.PatchCell;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.rules.SugarscapeRules;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

  public class sugarscapetester {

    public static void main(String[] args) {
      try {
        // Initialize simulation parameters
        Map<String, Parameter<?>> parameters = new HashMap<>();
        parameters.put("agentVision", new Parameter<>(3));
        parameters.put("agentMetabolism", new Parameter<>(1));
        parameters.put("agentSugar", new Parameter<>(5));
        parameters.put("pathSugarGrowBackRate", new Parameter<>(2));
        parameters.put("pathSugarGrowBackInteral", new Parameter<>(3));

        // Create Sugarscape rules
        SugarscapeRules rules = new SugarscapeRules(parameters);

        // Create a small 5x5 grid
        Grid grid = new Grid(5, 5);

        // Add Patch Cells with sugar
        for (int row = 0; row < 5; row++) {
          for (int col = 0; col < 5; col++) {
            PatchCell patch = new PatchCell(SugarscapeRules.State.PATCHES.getValue(),
                (int) (Math.random() * 5 + 1), 3, 2, new Point2D.Double(row, col));
            grid.addCell(patch);
          }
        }

        // Add Agent Cells
        AgentCell agent1 = new AgentCell(SugarscapeRules.State.AGENTS.getValue(),
            new Point2D.Double(2, 2), 2, 1, 10);
        AgentCell agent2 = new AgentCell(SugarscapeRules.State.AGENTS.getValue(),
            new Point2D.Double(4, 4), 1, 1, 8);
        grid.updateCell(agent1);
        grid.updateCell(agent2);

        // Run the simulation for 10 steps
        for (int step = 0; step < 10; step++) {
          System.out.println("Step " + step);
          printGrid(grid);

          // Get next states
          List<CellUpdate> updates = rules.getNextStatesForAllCells(grid);

          // Apply updates
          for (CellUpdate update : updates) {
            grid.updateCell(update.getNextCell());
          }
        }

      } catch (InvalidParameterException e) {
        System.err.println("Error initializing Sugarscape: " + e.getMessage());
      }
    }

    // Helper method to print the grid to console
    private static void printGrid(Grid grid) {
      for (int row = 0; row < grid.getRows(); row++) {
        for (int col = 0; col < grid.getCols(); col++) {
          Cell state = grid.getCell(row, col);
          if (state.getState() == SugarscapeRules.State.AGENTS.getValue()) {
            System.out.print("A "); // Agent
          } else if (state.getState() == SugarscapeRules.State.PATCHES.getValue()) {
            PatchCell patch = (PatchCell) state;
            System.out.print(patch.getSugar() + " "); // Patch
          } else {
            System.out.print(" . "); // Empty
          }
        }
        System.out.println();
      }
      System.out.println("------------------------------------------------");
    }
  }

