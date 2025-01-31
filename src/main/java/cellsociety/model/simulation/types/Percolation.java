package cellsociety.model.simulation.types;

import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationData;
import cellsociety.model.simulation.SimulationRules;

public class Percolation extends Simulation {

  public Percolation(SimulationRules rules, SimulationData data) {
    super(rules, data);
  }
  /**
   *
   */
  @Override
  public void getNextSimulationState() {
    return;
  }
}
