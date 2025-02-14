package cellsociety.model.simulation;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.config.SimulationConfig;
import cellsociety.model.simulation.rules.GameOfLifeRules;
import cellsociety.view.config.StateDisplayConfig;
import cellsociety.view.config.StateInfo;
import java.util.HashMap;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimulationTest {
  private Simulation testSimulation;
  private SimulationRules testRules;
  private SimulationMetaData testData;

  @BeforeEach
  void setUp() {
    testRules = new GameOfLifeRules(new HashMap<>()); // I chose an example rules class
    testData = new SimulationMetaData("GameOfLife", "Tester", "Justin", "Justin's GOL Test");
    testSimulation = new Simulation(testRules, testData);
  }

  @Test
  void getRules() {
    assertEquals(testRules, testSimulation.rules());
  }

  @Test
  void getData() {
    assertEquals(testData, testSimulation.data());
  }

  @Test
  void getStateInfo() {
    assertEquals(Color.WHITE, StateDisplayConfig.getStateInfo(testSimulation, 0).color());
    assertEquals(Color.BLACK, StateDisplayConfig.getStateInfo(testSimulation, 1).color());
  }
}