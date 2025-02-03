package cellsociety.model.simulation;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.rules.GameOfLifeRules;
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
    testData = new SimulationMetaData("Game of Life", "Tester", "Justin", "Justin's GOL Test", null);
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
    assertEquals(Color.WHITE, testSimulation.getStateInfo(0).color());
    assertEquals(Color.BLACK, testSimulation.getStateInfo(1).color());
  }
}