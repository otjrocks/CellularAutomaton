package cellsociety.model.simulation;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.config.SimulationConfig;
import cellsociety.model.simulation.rules.GameOfLifeRules;
import cellsociety.view.config.StateDisplayConfig;
import cellsociety.view.config.StateInfo;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimulationTest {
  private Simulation testSimulation;
  private SimulationRules testRules;
  private SimulationMetaData testData;

  @BeforeEach
  void setUp() {
    testRules = new GameOfLifeRules(); // I chose an example rules class
    testData = new SimulationMetaData("Game of Life", "Tester", "Justin", "Justin's GOL Test");
    testSimulation = new Simulation(testRules, testData) {
      @Override
      protected void initializeStateMap() {
        stateMap.put(0, new StateInfo("Dead", Color.WHITE));
        stateMap.put(1, new StateInfo("Alive", Color.BLACK));
      }
    };
  }

  @Test
  void getRules() {
    assertEquals(testRules, testSimulation.getRules());
  }

  @Test
  void getData() {
    assertEquals(testData, testSimulation.getData());
  }

  @Test
  void getStateInfo() {
    assertEquals(Color.WHITE, StateDisplayConfig.getStateInfo(testSimulation, 0).color());
    assertEquals(Color.BLACK, StateDisplayConfig.getStateInfo(testSimulation, 1).color());
  }
}