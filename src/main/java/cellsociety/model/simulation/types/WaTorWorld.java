package cellsociety.model.simulation.types;

import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.model.simulation.SimulationRules;
import cellsociety.model.simulation.StateInfo;
import javafx.scene.paint.Color;

public class WaTorWorld extends Simulation {


  public WaTorWorld(SimulationRules rules,
      SimulationMetaData data) {
    super(rules, data);
  }

  @Override
  protected void initializeStateMap() {
    stateMap.put(0, new StateInfo("Empty", Color.BLACK));
    stateMap.put(1, new StateInfo("Fish", Color.GREEN));
    stateMap.put(2, new StateInfo("Sharks", Color.BLUE));
  }
}
