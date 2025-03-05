package cellsociety.model.simulation;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.DefaultCell;
import cellsociety.model.edge.FixedEdgeStrategy;
import cellsociety.model.simulation.neighbors.MooreNeighbors;
import cellsociety.model.simulation.neighbors.VonNeumannNeighbors;
import java.util.HashMap;
import java.util.List;
import java.awt.geom.Point2D.Double;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimulationRulesTest {

  private Grid grid;
  private SimulationRules testSimulationRules;

  @BeforeEach
  void setUp() throws InvalidParameterException {
    grid = new Grid(5, 5, new FixedEdgeStrategy());

    for (int row = 0; row < 5; row++) {
      for (int col = 0; col < 5; col++) {
        Cell newCell = new DefaultCell(0, new Double(row, col));
        grid.addCell(newCell);
      }
    }

    testSimulationRules = new SimulationRules(new HashMap<>(), new VonNeumannNeighbors(1)) {
      @Override
      public int getNumberStates() {
        return 0;
      }
    };
  }

  @Test
  void getNeighbors_VonNeumann_FourNeighbors() throws InvalidParameterException {
    Cell cell = grid.getCell(2, 2);
    testSimulationRules = new SimulationRules(new HashMap<>(), new VonNeumannNeighbors(1)) {
      @Override
      public int getNumberStates() {
        return 0;
      }
    };

    List<Cell> fourNeighbors = testSimulationRules.getNeighbors(cell, grid);
    assertEquals(4, fourNeighbors.size());
  }

  @Test
  void getNeighbors_Moore_EightNeighbors() throws InvalidParameterException {
    Cell cell = grid.getCell(2, 2);
    testSimulationRules = new SimulationRules(new HashMap<>(), new MooreNeighbors(1)) {
      @Override
      public int getNumberStates() {
        return 0;
      }
    };

    List<Cell> eightNeighbors = testSimulationRules.getNeighbors(cell, grid);
    assertEquals(8, eightNeighbors.size());
  }

  @Test
  void getNeighbors_VonNeumann_TwoLayers() throws InvalidParameterException {
    Cell cell = grid.getCell(2, 2);

    testSimulationRules = new SimulationRules(new HashMap<>(), new VonNeumannNeighbors(2)) {
      @Override
      public int getNumberStates() {
        return 0;
      }
    };

    List<Cell> neighbors = testSimulationRules.getNeighbors(cell, grid);
    assertEquals(8, neighbors.size());
  }

  @Test
  void getNeighbors_Moore_TwoLayers() throws InvalidParameterException {
    Cell cell = grid.getCell(2, 2);

    testSimulationRules = new SimulationRules(new HashMap<>(), new MooreNeighbors(2)) {
      @Override
      public int getNumberStates() {
        return 0;
      }
    };

    List<Cell> neighbors = testSimulationRules.getNeighbors(cell, grid);
    assertEquals(16, neighbors.size());
  }



  @Test
  void getNeighbors_MooreOneLayer_BoundsDifferentAreas() throws InvalidParameterException {
    Cell cell = grid.getCell(0, 0);

    testSimulationRules = new SimulationRules(new HashMap<>(), new MooreNeighbors(1)) {
      @Override
      public int getNumberStates() {
        return 0;
      }
    };

    List<Cell> neighbors = testSimulationRules.getNeighbors(cell, grid);
    assertEquals(3, neighbors.size());

    cell = grid.getCell(0, 1);
    neighbors = testSimulationRules.getNeighbors(cell, grid);
    assertEquals(5, neighbors.size());

    cell = grid.getCell(1, 0);
    neighbors = testSimulationRules.getNeighbors(cell, grid);
    assertEquals(5, neighbors.size());

    cell = grid.getCell(4, 4);
    neighbors = testSimulationRules.getNeighbors(cell, grid);
    assertEquals(3, neighbors.size());
  }


  @Test
  void getNeighbors_VonNeumannOneLayer_BoundsDifferentAreas() throws InvalidParameterException {
    Cell cell = grid.getCell(0, 0);

    testSimulationRules = new SimulationRules(new HashMap<>(), new VonNeumannNeighbors(1)) {
      @Override
      public int getNumberStates() {
        return 0;
      }
    };

    List<Cell> neighbors = testSimulationRules.getNeighbors(cell, grid);
    assertEquals(2, neighbors.size());

    cell = grid.getCell(0, 1);
    neighbors = testSimulationRules.getNeighbors(cell, grid);
    assertEquals(3, neighbors.size());

    cell = grid.getCell(1, 0);
    neighbors = testSimulationRules.getNeighbors(cell, grid);
    assertEquals(3, neighbors.size());

    cell = grid.getCell(4, 4);
    neighbors = testSimulationRules.getNeighbors(cell, grid);
    assertEquals(2, neighbors.size());
  }

  @Test
  void getNeighbors_MultiLayerAtCornerMoore_CorrectNeighbors() throws InvalidParameterException {
    testSimulationRules = new SimulationRules(new HashMap<>(), new MooreNeighbors(3)) {
      @Override
      public int getNumberStates() {
        return 0;
      }
    };

    Cell cell = grid.getCell(0, 0);
    List<Cell> neighbors = testSimulationRules.getNeighbors(cell, grid);
    assertEquals(9, neighbors.size());
  }

  @Test
  void getNeighbors_MultiLayerAtCornerVonNeumann_CorrectNeighbors() throws InvalidParameterException {
    testSimulationRules = new SimulationRules(new HashMap<>(), new VonNeumannNeighbors(3)) {
      @Override
      public int getNumberStates() {
        return 0;
      }
    };

    Cell cell = grid.getCell(0, 0);
    List<Cell> neighbors = testSimulationRules.getNeighbors(cell, grid);
    assertEquals(6, neighbors.size());
  }




  @Test
  void getNextState() {
    Cell cell = new DefaultCell(2, new Double(2, 2));
    assertEquals(2, testSimulationRules.getNextState(cell, grid));
  }
}