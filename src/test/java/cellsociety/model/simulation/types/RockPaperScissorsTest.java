package cellsociety.model.simulation.types;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.SimulationRules;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RockPaperScissorsTest {
  private RockPaperScissors myRockPaperScissors;

  @BeforeEach
  void setUp() {
    Map<String, Double> parameters = new HashMap<>();
    parameters.put("threshold", 0.5);
    parameters.put("numStates", 3.0);
  }
}