package cellsociety.model.simulation.types;

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
    stateMap.put(0, new StateInfo("Empty", Color.WHITE));
    stateMap.put(1, new StateInfo("Group1", Color.RED));
    stateMap.put(2, new StateInfo("Group2", Color.BLUE));
  }
}
