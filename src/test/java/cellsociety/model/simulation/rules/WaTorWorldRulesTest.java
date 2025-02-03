package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.Grid;
import cellsociety.model.simulation.Simulation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WaTorWorldRulesTest {
  private WaTorWorldRules waTorWorldRules;
  private Grid grid;

  @BeforeEach
  void setUp() {
    grid = new Grid();
    waTorWorldRules = new WaTorWorldRules();
  }

  @Test
  void getNextState() {

  }
}