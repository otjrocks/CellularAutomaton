package cellsociety.model.simulation.types;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.model.simulation.SimulationRules;
import cellsociety.model.simulation.StateInfo;
import cellsociety.model.simulation.rules.SegregationModelRules;
import cellsociety.model.simulation.rules.WaTorWorldRules;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WaTorWorldTest {
  private WaTorWorld waTorWorld;

  @BeforeEach
  void setUp() {
    Map<String, Double> parameters = new HashMap<>();
    parameters.put("fishReproductionTime", 3.0);
    parameters.put("sharkReproductionTime", 3.0);
    parameters.put("sharkEnergyGain", 3.0);

    SimulationRules waTorWorldRules = new WaTorWorldRules(parameters);
    SimulationMetaData testData = new SimulationMetaData("WaTorWorld", "justin's WTW", "justin", "test");

    waTorWorld = new WaTorWorld(waTorWorldRules, testData);
    waTorWorld.initializeStateMap();
  }

  @Test
  void testStateInitialization() {
    Map<Integer, StateInfo> stateMap = waTorWorld.getStateMap();

    assertNotNull(stateMap);
    assertEquals(Color.BLACK, stateMap.get(0).color());
    assertEquals(Color.GREEN, stateMap.get(1).color());
    assertEquals(Color.BLUE, stateMap.get(2).color());
  }
}