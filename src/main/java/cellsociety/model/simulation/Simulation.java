package cellsociety.model.simulation;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.paint.Color;

public abstract class Simulation {
  private SimulationRules myRules;
  private SimulationData myData;
  protected final Map<Integer, StateInfo> stateMap;

  public Simulation(SimulationRules rules, SimulationData data) {
    myRules = rules;
    myData = data;
    this.stateMap = new HashMap<>();
    initializeStateMap();
  }

  public SimulationRules getRules() {
    return myRules;
  }
  public SimulationData getData() {
    return myData;
  }
  public abstract void getNextSimulationState();

  protected abstract void initializeStateMap();
  public StateInfo getStateInfo(int state) {
    return stateMap.getOrDefault(state, new StateInfo("Unknown", Color.GRAY));
  }
  public Map<Integer, StateInfo> getStateMap() {
    return Map.copyOf(stateMap);
  }
}
