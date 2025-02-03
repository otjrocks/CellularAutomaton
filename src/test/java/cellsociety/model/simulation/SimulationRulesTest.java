package cellsociety.model.simulation;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.DefaultCell;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.geom.Point2D.Double;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimulationRulesTest {
  private Grid grid;
  private SimulationRules testSimulationRules;

  @BeforeEach
  void setUp() {
    grid = new Grid(5, 5);

    for (int row = 0; row < 5; row++) {
      for (int col = 0; col < 5; col++) {
        Cell newCell = new DefaultCell(0, new Double(row, col));
        grid.addCell(newCell);
      }
    }


    testSimulationRules = new SimulationRules() {
      public List<Cell> getNeighbors(Cell cell, Grid grid) {
        return new ArrayList<>();
      }

      @Override
      public int getNextState(Cell cell, Grid grid) {
        return cell.getState();
      }
    };
  }
  @Test
  void testDefaultConstructor() {
    assertNull(testSimulationRules.getParameter("noKey"),"Default constructor creates an empty parameter map.");
  }
/*
  @Test
  void testParameterConstructor() {
    Map<String, Double> params = new HashMap<>();
    params.put("fire", 0.7);
    params.put("water", 0.3);
    testSimulationRules = new SimulationRules(params) {
      @Override
      public List<Cell> getNeighbors(Cell cell, Grid grid) {
        return List.of();
      }

      @Override
      public int getNextState(Cell cell, Grid grid) {
        return 0;
      }
    };

    assertEquals(0.7, testSimulationRules.getParameter("fire"),"Parameter constructor creates a parameter map.");
    assertEquals(0.3, testSimulationRules.getParameter("water"),"Parameter constructor creates a parameter map.");
  }
*/
  @Test
  void testSetAndGetParameter() {
    testSimulationRules.setParameter("fire", 0.7);
    assertEquals(0.7, testSimulationRules.getParameter("fire"));
  }

  @Test
  void testNoParameter() {
    assertNull(testSimulationRules.getParameter("noParameter"));
  }


  @Test
  void getNeighbors() {
    Cell cell = grid.getCell(2, 2);

    List<Cell> eightNeighbors = testSimulationRules.getNeighbors(cell, grid, true);
    assertEquals(8, eightNeighbors.size(), "Moore's method should return 8 neighbors.");

    List<Cell> fourNeighbors = testSimulationRules.getNeighbors(cell, grid, false);
    assertEquals(4, fourNeighbors.size(), "Von neumann's method should return 4 neighbors.");
  }

  @Test
  void getNeighborsBounds() {
    Cell cell = grid.getCell(0, 0);

    List<Cell> neighbors = testSimulationRules.getNeighbors(cell, grid, true);
    assertEquals(3, neighbors.size());

    cell = grid.getCell(0, 1);
    neighbors = testSimulationRules.getNeighbors(cell, grid, false);
    assertEquals(3, neighbors.size());

    cell = grid.getCell(0, 2);
    neighbors = testSimulationRules.getNeighbors(cell, grid, true);
    assertEquals(5, neighbors.size());

    cell = grid.getCell(4, 4);
    neighbors = testSimulationRules.getNeighbors(cell, grid, false);
    assertEquals(2, neighbors.size());

  }

  @Test
  void getNextState() {
    Cell cell = new DefaultCell(2, new Double(2, 2));
    assertEquals(2, testSimulationRules.getNextState(cell, grid));
  }
}