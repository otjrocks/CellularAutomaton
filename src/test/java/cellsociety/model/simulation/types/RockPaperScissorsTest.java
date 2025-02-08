package cellsociety.model.simulation.types;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.model.simulation.SimulationRules;
import cellsociety.model.simulation.StateInfo;
import cellsociety.model.simulation.rules.RockPaperScissorsRules;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RockPaperScissorsTest {
  private RockPaperScissors myRockPaperScissors;

  @BeforeEach
  void setUp() {
    Map<String, Double> parameters = new HashMap<>();
    parameters.put("threshold", 0.5);
    parameters.put("numStates", 3.0);

    SimulationRules rockPaperScissorsRule = new RockPaperScissorsRules(parameters);
    SimulationMetaData testData = new SimulationMetaData("RPSModel", "Justin's RPS", "Justin", "test");

    myRockPaperScissors = new RockPaperScissors(rockPaperScissorsRule, testData);
    myRockPaperScissors.initializeStateMap();
  }

  @Test
  void testStateInitialization() {
    Map<Integer, StateInfo> stateMap = myRockPaperScissors.getStateMap();

    assertNotNull(stateMap);

    assertEquals(Color.WHITE, stateMap.get(0).color());
    assertEquals(Color.RED, stateMap.get(1).color());
    assertEquals(Color.ORANGE, stateMap.get(2).color());
    assertEquals(Color.YELLOW, stateMap.get(3).color());
    assertEquals(Color.GREEN, stateMap.get(4).color());
    assertEquals(Color.CYAN, stateMap.get(10).color());
    assertEquals(Color.SIENNA, stateMap.get(15).color());
    assertEquals(Color.LIGHTGOLDENRODYELLOW, stateMap.get(20).color());
  }
}