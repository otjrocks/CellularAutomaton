package cellsociety;

import java.util.Map;
import javafx.scene.control.Cell;

public abstract class SimulationRules {
  private Map<String, Double> parameters;

  abstract void getNeighbors(Cell cell);
  abstract void getNextState(Cell cell);
  abstract void getParameter(String curParameter);
}
