package cellsociety.model.simulation;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class representing a way to get a cell's neighbors in a grid
 *
 * @author Justin Aronwald
 * @author Owen Jennings
 */
public abstract class GetNeighbors {

  private final int layers;

  /**
   * An abstract method to return the directions to use for getting neighbors
   *
   * @param row    The row of the current cell
   * @param column The column of the current cell
   * @return A 2D int array which represents a collection of coordinates (x, y), where x is the
   * amount to add/subtract from the current row and y is the amount to add/subtract from the
   * current column to get one of the neighbors in the collection.
   */
  public abstract int[][] getDirections(int row, int column);

  /**
   * The default constructor of GetNeighbors
   *
   * @param layers The number of layers to include in the get neighbors calculation, if supported by
   *               a specific neighbor policy
   */
  public GetNeighbors(int layers) {
    this.layers = layers;
  }

  /**
   * The default implementation of getNeighbors. This provides a list of cells which are neighbors
   * of the provided cell object. This method takes in the directions from the abstracted neighbor
   * classes and then iterates through the number of layers if needed.
   *
   * @param cell The cell you are querying for neighbors.
   * @param grid The grid of the simulation you are looking for neighbors in
   * @return A list of cells that are neighbors of the provided cell.
   */
  public List<Cell> getNeighbors(Cell cell, Grid grid) {

    List<Cell> neighbors = new ArrayList<>();

    int[][] directions = getDirections(cell.getRow(), cell.getCol());

    for (int i = 1; i <= layers; i++) {
      for (int[] direction : directions) {
        int newRow = cell.getRow() + i * direction[0];
        int newCol = cell.getCol() + i * direction[1];

        if (newRow >= 0 && newRow < grid.getRows() && newCol >= 0 && newCol < grid.getCols()) {
          Cell newCell = grid.getCell(newRow, newCol);
          if (newCell != null) {
            neighbors.add(newCell);
          }
        }
      }
    }
    return neighbors;
  }
}
