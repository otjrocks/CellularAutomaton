package cellsociety.model.simulation.types;

import static cellsociety.config.MainConfig.MESSAGES;

import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.model.simulation.SimulationRules;
import cellsociety.model.simulation.StateInfo;
import javafx.scene.paint.Color;

public class SegregationModel extends Simulation {

  public SegregationModel(SimulationRules rules, SimulationMetaData data) {
    super(rules, data);
  }

  /**
   *
   */
  @Override
  protected void initializeStateMap() {
    stateMap.put(0, new StateInfo(MESSAGES.getString("EMPTY"), Color.WHITE));
    stateMap.put(1, new StateInfo(MESSAGES.getString("GROUP_ONE"), Color.RED));
    stateMap.put(2, new StateInfo(MESSAGES.getString("GROUP_TWO"), Color.BLUE));
  }
}
