package cellsociety.model.simulation;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.neighbors.MooreNeighbors;
import cellsociety.model.simulation.rules.GameOfLifeRules;
import cellsociety.view.config.StateDisplayConfig;
import java.util.HashMap;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimulationTest {
  private Simulation testSimulation;
  private SimulationRules testRules;
  private SimulationMetaData testData;

  @BeforeEach
  void setUp() throws InvalidParameterException {
    testRules = new GameOfLifeRules(new HashMap<>(), new MooreNeighbors(1)); // I chose an example rules class
    testData = new SimulationMetaData("GameOfLife", "Tester", "Justin", "Justin's GOL Test", "Moore", 1);
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