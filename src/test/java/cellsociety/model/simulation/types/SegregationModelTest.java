package cellsociety.model.simulation.types;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.model.simulation.SimulationRules;
import cellsociety.model.simulation.StateInfo;
import cellsociety.model.simulation.rules.GameOfLifeRules;
import cellsociety.model.simulation.rules.SegregationModelRules;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SegregationModelTest {
  private SegregationModel segregationModel;

  @BeforeEach
  void setUp() {
    Map<String, Double> parameters = new HashMap<>();
    parameters.put("toleranceThreshold", 0.3);

    SimulationRules segregationRules = new SegregationModelRules(parameters);
    SimulationMetaData testData= new SimulationMetaData("SegregationModel", "justin's SM", "justin", "test");

    segregationModel = new SegregationModel(segregationRules, testData);
    segregationModel.initializeStateMap();
  }

  @Test
  void testStateInitialization() {
    Map<Integer, StateInfo> stateMap = segregationModel.getStateMap();

    assertNotNull(stateMap);
    assertEquals(Color.WHITE, stateMap.get(0).color());
    assertEquals(Color.RED, stateMap.get(1).color());
    assertEquals(Color.BLUE, stateMap.get(2).color());
  }
}