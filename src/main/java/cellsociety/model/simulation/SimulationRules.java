package cellsociety.model.simulation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class SimulationRules {
  private Map<String, Double> parameters;

  public SimulationRules() {
    this.parameters = new HashMap<>();
  }
  public SimulationRules(Map<String, Double> parameters) {
    this.parameters = parameters;
  }
  public Double getParameter(String curParameter){
    return parameters.get(curParameter);
  }

  public void setParameter(String key, Double value){
    parameters.put(key, value);
  }

  //methods below depend on subclasses
  abstract List<Cell> getNeighbors(Cell cell);
  abstract void getNextState(Cell cell);

}
