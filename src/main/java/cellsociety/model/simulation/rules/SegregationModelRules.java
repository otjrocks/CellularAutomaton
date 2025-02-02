package cellsociety.model.simulation.rules;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.simulation.SimulationRules;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

//For a SegregationModel cell, there can be 3 states
// A cell with state 0 indicates it's empty
// A cell with state 1 indicates it's a part of Group 1
// A cell with state 2 indicates it's a part of Group 2

public class SegregationModelRules extends SimulationRules {
  protected final Map<String, Double> parameters;
  private final Random random = new Random();
  private boolean firstStateUpdate = true; // check to see if getNextState has been run before
  private List<Cell> emptyCells;

  public SegregationModelRules() {
    this.parameters = new HashMap<>(setDefaultParameters());
    emptyCells = new ArrayList<>();
  }

  /**
   * @param cell -  individual cell from grid
   * @param grid - the list of cell objects representing the grid
   * @return -  a list of cell objects representing the neighbors of the cell (adjacent and diagonals)
   */
  @Override
  public List<Cell> getNeighbors(Cell cell, Grid grid) {
    List<Cell> neighbors = new ArrayList<>();
    Point2D curCellPosition = cell.getLocation();

    int[][] directions = {
        {0, -1}, {0, 1},   // left, right
        {-1, 0}, {1, 0},   // up, down
        {-1, -1}, {-1, 1}, // top diagonals (left, right)
        {1, -1}, {1, 1}    // bottom diagonals (left, right)
    };

    for (int[] dir : directions) {
      Point2D neighborLocation = new Point2D.Double(cell.getRow() + dir[0], cell.getCol() + dir[1]);

      if (grid.cellExists(neighborLocation)) {
        Cell neighborCell = grid.getCell(neighborLocation);
        neighbors.add(neighborCell);
      }
    }
    return neighbors;
  }


  /** Schelling's Model of Segregation:
   * There exists a probability tolerance threshold T (0.3) representing the minimum number
   * of neighbors that must be the same state for the cell to be satisfied

   *  If a cell has less than T of similar neighbors, they move to a random empty space
   *  If a cell >= T of similar neighbors, they stay
   *  Cells move until all cells are satisfied or no empty spaces remain
   *
   * @param cell -  individual cell from grid
   * @param grid - the list of cell objects representing the grid
   * @return - the next state of a cell based on the rules of Schelling's segregation model
   */
  @Override
  public int getNextState(Cell cell, Grid grid) {
    if (firstStateUpdate) {
      getEmptyCells(grid);
      firstStateUpdate = false;
    }
    List<Cell> neighbors = getNeighbors(cell, grid);
    int currentState = cell.getState();

    int sameType = 0;
    int totalNeighbors = 0;
    for (Cell neighbor : neighbors) {
      if (neighbor.getState() != 0) {
        totalNeighbors++;
        if (neighbor.getState() == currentState) {
          sameType++;
        }
      }
    }
    // No occupied neighbors -> stay
    if (totalNeighbors == 0) {
      return currentState;
    }
    double typePercentage = (double) sameType / totalNeighbors;

    // unsatisfied
    if (typePercentage < parameters.get("toleranceThreshold")) {
      moveCellToEmptyLocationIfAvailable(cell, grid, currentState);
      return 0;
    }

    return currentState;
  }

  private void moveCellToEmptyLocationIfAvailable(Cell cell, Grid grid, int currentState) {
    if (!emptyCells.isEmpty()) {
      Cell newCell = getRandomEmptyCell();
      newCell.setState(currentState);
      grid.updateCell(newCell);
      emptyCells.remove(newCell);
      emptyCells.add(cell);
    }
  }

  private Map<String, Double> setDefaultParameters () {
    Map<String, Double> parameters = new HashMap<>();
    parameters.put("toleranceThreshold", 0.3);
    return parameters;
  }

  private void getEmptyCells(Grid grid) {
    int rows = grid.getRows();
    int cols = grid.getCols();
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        if (grid.getCell(row, col).getState() == 0) {
          emptyCells.add(grid.getCell(row, col));
        }
      }
    }
  }

  private Cell getRandomEmptyCell() {
    return emptyCells.get(random.nextInt(emptyCells.size()));
  }
}
