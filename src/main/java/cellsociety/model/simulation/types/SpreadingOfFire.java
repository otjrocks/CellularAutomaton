package cellsociety.model.simulation.types;

import static cellsociety.config.MainConfig.MESSAGES;

import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.model.simulation.SimulationRules;
import cellsociety.model.simulation.StateInfo;
import javafx.scene.paint.Color;

public class SpreadingOfFire extends Simulation {

  public SpreadingOfFire(SimulationRules rules, SimulationMetaData data) {
    super(rules, data);
  }

  @Override
  protected void initializeStateMap() {
    stateMap.put(0, new StateInfo(MESSAGES.getString("EMPTY"), Color.BLACK));
    stateMap.put(1, new StateInfo(MESSAGES.getString("TREE"), Color.GREEN));
    stateMap.put(2, new StateInfo(MESSAGES.getString("BURNING"), Color.RED));
  }
}
