package cellsociety.model.simulation.types;

import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationData;
import cellsociety.model.simulation.SimulationRules;
import cellsociety.model.simulation.StateInfo;
import javafx.scene.paint.Color;

public class SpreadingOfFire extends Simulation {

  public SpreadingOfFire(SimulationRules rules, SimulationData data) {
    super(rules, data);
  }

  @Override
  protected void initializeStateMap() {
    stateMap.put(0, new StateInfo("Empty", Color.BLACK));
    stateMap.put(1, new StateInfo("Tree", Color.GREEN));
    stateMap.put(2, new StateInfo("Burning", Color.RED));
  }
}
