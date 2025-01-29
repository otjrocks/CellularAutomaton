package cellsociety.model.simulation;

import cellsociety.model.cell.Cell;
import java.util.List;
import java.util.Map;


public abstract class SimulationRules {
  private Map<String, Double> parameters;

  public SimulationRules(Map<String, Double> parameters) {
    this.parameters = parameters;
  }
  public Double getParameter(String curParameter){
    return parameters.get(curParameter);
  }

  //methods below depend on subclasses
  abstract List<Cell> getNeighbors(Cell cell);
  public abstract int getNextState(Cell cell);

}
