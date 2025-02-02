package cellsociety.model.simulation.types;

import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationData;
import cellsociety.model.simulation.SimulationRules;
import cellsociety.model.simulation.StateInfo;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.paint.Color;

public class GameOfLife extends Simulation {

  public GameOfLife(SimulationRules rules, SimulationData data) {
    super(rules, data);
  }

  @Override
  protected void initializeStateMap() {
    stateMap.put(0, new StateInfo("Dead", Color.WHITE));
    stateMap.put(1, new StateInfo("Alive", Color.BLACK));
  }

}

