package cellsociety.model.simulation;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameOfLifeRules extends SimulationRules {

  private final int numOfGridRows = 8;
  private final int numOfGridCols = 8;
  private Map<String, Double> parameters;


  public GameOfLifeRules() {
    super();
  }


  /**
   * @param cell - individual cell from grid
   * @return - a list of cell objects representing the neighbors of the cell (adjacent and
   * diagonals)
   */
  @Override
  List<Cell> getNeighbors(Cell cell, Grid grid) {
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

  /**
   * @param cell - individual cell from grid
   * @return
   */
  @Override
  public int getNextState(Cell cell) {
    return 0;
  }
}
