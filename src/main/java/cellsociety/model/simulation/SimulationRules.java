package cellsociety.model.simulation;

import cellsociety.model.Grid;
import cellsociety.model.cell.CellStateUpdate;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import cellsociety.model.cell.Cell;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public abstract class SimulationRules {

  protected Map<String, Double> parameters;

  public SimulationRules() {
    this.parameters = new HashMap<>();
  }

  public SimulationRules(Map<String, Double> parameters) {
    this.parameters = parameters;
  }

  public Map<String, Double> getParameters() {
    return Map.copyOf(parameters);
  }

  public Double getParameter(String curParameter) {
    return parameters.get(curParameter);
  }

  public void setParameter(String key, Double value) {
    parameters.put(key, value);
  }

  //only two options, so moved the getNeighbors here and actually defined it.
  public List<Cell> getNeighbors(Cell cell, Grid grid, boolean includesDiagonals) {

    List<Cell> neighbors = new ArrayList<>();

    int[][] directions = {
        {0, -1}, {0, 1},   // left, right
        {-1, 0}, {1, 0},   // up, down
        {-1, -1}, {-1, 1}, // top diagonals (left, right)
        {1, -1}, {1, 1}    // bottom diagonals (left, right)
    };

    if (!includesDiagonals) {
      directions = new int[][]{{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
    }

    for (int[] dir : directions) {
      Point2D neighborLocation = new Point2D.Double(cell.getRow() + dir[0], cell.getCol() + dir[1]);

      if (grid.cellExists(neighborLocation)) {
        Cell neighborCell = grid.getCell(neighborLocation);
        neighbors.add(neighborCell);
      }
    }
    return neighbors;

  }

  public List<CellStateUpdate> getNextStatesForAllCells(Grid grid) {
    List<CellStateUpdate> nextStates = new ArrayList<>(); // calculate next states in first pass, then update all next states in second pass
    Iterator<Cell> cellIterator = grid.getCellIterator();
    while (cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      nextStates.add(new CellStateUpdate(cell.getLocation(), getNextState(cell, grid)));
    }
    return nextStates;
  }

  //methods below depend on subclasses
  public abstract int getNextState(Cell cell, Grid grid);

}
