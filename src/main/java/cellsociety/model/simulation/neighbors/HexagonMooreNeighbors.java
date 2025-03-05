package cellsociety.model.simulation.neighbors;

import cellsociety.model.simulation.GetNeighbors;

/**
 * The neighbor rules for a hexagon grid using Moore Technique.
 *
 * @author Owen Jennings
 */
public class HexagonMooreNeighbors extends GetNeighbors {

  /**
   * Create a HexagonMooreNeighbors with provided layers.
   *
   * @param layers The number of layers to use
   */
  public HexagonMooreNeighbors(int layers) {
    super(layers);
  }

  /**
   * The directions based on Hexagon Moore Neighbors rules.
   *
   * @return - the coordinate system for the directions to obtain the neighbors (6 adjacent
   * neighbors of the current hexagon)
   */
  @Override
  public int[][] getDirections(int row, int column) {
    return new int[][]{
        {-1, 0}, // above
        {1, 0}, // below
        {0, -1}, // left top diagonal
        {1, -1}, // left bottom diagonal
        {0, 1}, // right top diagonal
        {1, 1}, // right bottom diagonal
    };
  }
}
