package cellsociety.model.simulation.types;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.model.simulation.SimulationRules;
import cellsociety.view.config.StateInfo;
import cellsociety.model.simulation.rules.SpreadingOfFireRules;
import java.util.Map;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SpreadingOfFireTest {
  private SpreadingOfFire spreadingOfFire;

  @BeforeEach
  void setUp() {
    SimulationRules spreadingOfFireRules = new SpreadingOfFireRules();
    SimulationMetaData testData= new SimulationMetaData("Spreading Of Fire", "justin's SOF", "justin", "test");

    spreadingOfFire = new SpreadingOfFire(spreadingOfFireRules, testData);
    spreadingOfFire.initializeStateMap();
  }

  @Test
  void testStateInitialization() {
    Map<Integer, StateInfo> stateMap = spreadingOfFire.getStateMap();

    assertNotNull(stateMap);
    assertEquals(Color.BLACK, stateMap.get(0).color());
    assertEquals(Color.GREEN, stateMap.get(1).color());
    assertEquals(Color.RED, stateMap.get(2).color());
  }
}