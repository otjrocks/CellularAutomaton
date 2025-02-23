package cellsociety.model.simulation;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import java.util.ArrayList;
import java.util.List;

public abstract class GetNeighbors {
  public abstract int[][] getDirections();
  private final int layers;

  public GetNeighbors(int layers) {
    this.layers = layers;
  }

  /**
   * The default implementation of getNeighbors. This provides a list of cells which are neighbors
   * of the provided cell object. This method takes in the directions from the abstracted neighbor classes and then iterates through the number of layers if needed.
   *
   * @param cell              The cell you are querying for neighbors.
   * @param grid              The grid of the simulation you are looking for neighbors in
   * @return A list of cells that are neighbors of the provided cell.
   */
  public List<Cell> getNeighbors(Cell cell, Grid grid) {

    List<Cell> neighbors = new ArrayList<>();

    int[][] directions = getDirections();

    for (int i = 1; i <= layers; i++) {
      for (int[] direction : directions) {
        int newRow = cell.getRow() + i * direction[0];
        int newCol = cell.getCol() + i * direction[1];

        if (newRow < 0 || newRow >= grid.getRows() || newCol < 0 || newCol >= grid.getCols()) {
          break;
        }

        Cell newCell = grid.getCell(newRow, newCol);
        neighbors.add(newCell);
      }
    }
    return neighbors;
  }
}
