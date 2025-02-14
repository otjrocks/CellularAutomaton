package cellsociety.model.simulation;

import static cellsociety.config.MainConfig.getMessages;

import cellsociety.model.cell.DefaultCell;
import cellsociety.model.simulation.Parameter.InvalidParameterType;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.CellUpdate;

public abstract class SimulationRules {
  private Map<String, Parameter<?>> myParameters;

  public SimulationRules(Map<String, Parameter<?>> parameters) throws InvalidParameterType {
    myParameters = parameters;
  }

  public void setParameters(Map<String, Parameter<?>> parameters) {
    myParameters = parameters;
  }

  public Map<String, Parameter<?>> getParameters() {
    return myParameters;
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

  /**
   * A default implementation of algorithm to get the cell updates for all cells Note, this will
   * call get next state to get next state values for all cells. The CellUpdate will use a default
   * cell to provide state update.
   * <p>
   * If a specific simulation uses a different cell type, this method should be overwritten.
   *
   * @param grid: The grid that you wish to get the next states for
   * @return A list of cell updates that should occur
   */
  public List<CellUpdate> getNextStatesForAllCells(Grid grid) {
    List<CellUpdate> nextStates = new ArrayList<>(); // calculate next states in first pass, then update all next states in second pass
    Iterator<Cell> cellIterator = grid.getCellIterator();
    while (cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      // next cell is a new default cell with the next state based on rules
      // if specific simulation has a specific cell type, it should override this default implementation
      Cell nextCell = new DefaultCell(getNextState(cell, grid), cell.getLocation());
      nextStates.add(new CellUpdate(cell.getLocation(), nextCell));
    }
    return nextStates;
  }

  public abstract int getNextState(Cell cell, Grid grid);
  public abstract int getNumberStates();

  public void checkMissingParameterAndThrowException(String threshold) {
    if (!getParameters().containsKey(threshold)) {
      throw new IllegalArgumentException(
          String.format(getMessages().getString("MISSING_SIMULATION_PARAMETER_ERROR"), threshold));
    }
  }
}
