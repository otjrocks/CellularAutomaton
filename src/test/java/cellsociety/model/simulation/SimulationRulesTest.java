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

  /**
   * Mock subclass of SimulationRules for testing.
   */
  private static class TestSimulationRules extends SimulationRules {
    public TestSimulationRules() {
      super();
    }

    @Override
    public List<Cell> getNeighbors(Cell cell, Grid grid) {
      return super.getNeighbors()

    public TestSimulationRules(Map<String, Double> parameters) {
      super(parameters);
    }

    @Override
    public int getNextState(Cell cell, Grid grid) {
      return cell.getState(); // Just return current state for mock testing
    }
  }

  @BeforeEach
  void setUp() {
    grid = new Grid(5, 5);
    testSimulationRules = new SimulationRules() {
      @Override
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
    Grid grid = new Grid(5, 5);
    Cell cell = new Cell(1, new Double(1, 1)) {
    };

    List<Cell> neighbors = testSimulationRules.getNeighbors(cell, grid);
    assertTrue(neighbors.isEmpty(), "Neighbor list is empty.");
  }

  @Test
  void getNextState() {
  }
}