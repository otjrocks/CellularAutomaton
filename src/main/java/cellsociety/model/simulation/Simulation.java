package cellsociety.model.simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.scene.paint.Color;

public abstract class Simulation {

  private final SimulationRules myRules;
  private final SimulationMetaData myData;
  protected final Map<Integer, StateInfo> stateMap;

  public Simulation(SimulationRules rules, SimulationMetaData data) {
    myRules = rules;
    myData = data;
    this.stateMap = new HashMap<>();
    initializeStateMap();
  }

  public SimulationRules getRules() {
    return myRules;
  }

  public SimulationMetaData getData() {
    return myData;
  }

  protected abstract void initializeStateMap();

  public StateInfo getStateInfo(int state) {
    return stateMap.getOrDefault(state, new StateInfo("Unknown", Color.GRAY));
  }

  public Map<Integer, StateInfo> getStateMap() {
    return Map.copyOf(stateMap);
  }

  public List<Integer> getStates() {
    return new ArrayList<>(stateMap.keySet());
  }
}
