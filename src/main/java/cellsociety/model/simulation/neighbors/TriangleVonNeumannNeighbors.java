package cellsociety.model.simulation.neighbors;

import cellsociety.model.simulation.GetNeighbors;

/**
 * The implementation of Von Neumann neighbors for Triangle grid
 *
 * @author Owen Jennings
 */
public class TriangleVonNeumannNeighbors extends GetNeighbors {

  /**
   * The default constructor for Von Neumann Neighbors for Triangle Grid
   *
   * @param layers The number of layers required.
   */
  public TriangleVonNeumannNeighbors(int layers) {
    super(layers);
  }

  /**
   * @return - the coordinate system for the directions to obtain the neighbors (the four cells that
   * share an edge with the central triangle)
   */
  @Override
  public int[][] getDirections(int row, int column) {
    return new int[][]{
        {-1, -1}, // top left
        {-1, 1}, // top right
        {1, -1}, // bottom left
        {1, 1}, // bottom right
    };
  }
}
