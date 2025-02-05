package cellsociety.model.simulation.types;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.model.simulation.SimulationRules;
import cellsociety.model.simulation.StateInfo;
import cellsociety.model.simulation.rules.GameOfLifeRules;
import java.util.Map;
import javafx.scene.paint.Color;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameOfLifeTest {
  private GameOfLife gameOfLife;

  @BeforeEach
  void setUp() {
    SimulationRules gameOfLifeRules= new GameOfLifeRules();
    SimulationMetaData testData= new SimulationMetaData("GOL", "justin's GOL", "justin", "test");

    gameOfLife = new GameOfLife(gameOfLifeRules, testData);
    gameOfLife.initializeStateMap();
  }

  @Test
  void testStateInitialization() {
    Map<Integer, StateInfo> stateMap = gameOfLife.getStateMap();

    assertNotNull(stateMap);
    assertEquals(Color.WHITE, stateMap.get(0).color());
    assertEquals(Color.BLACK, stateMap.get(1).color());
  }


}