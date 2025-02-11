package cellsociety.model.simulation.types;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.model.simulation.SimulationRules;
import cellsociety.view.config.StateInfo;
import cellsociety.model.simulation.rules.PercolationRules;
import java.util.Map;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PercolationTest {
  private Percolation percolation;

  @BeforeEach
  void setUp() {
    SimulationRules percolationRules = new PercolationRules();
    SimulationMetaData testData= new SimulationMetaData("Percolation", "justin's P", "justin", "test");

    percolation = new Percolation(percolationRules, testData);
    percolation.initializeStateMap();
  }

  @Test
  void testStateInitialization() {
    Map<Integer, StateInfo> stateMap = percolation.getStateMap();

    assertNotNull(stateMap);
    assertEquals(Color.BLACK, stateMap.get(0).color());
    assertEquals(Color.WHITE, stateMap.get(1).color());
    assertEquals(Color.BLUE, stateMap.get(2).color());
  }

}