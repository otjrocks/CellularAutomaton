package cellsociety.model.simulation.neighbors;

import cellsociety.model.simulation.GetNeighbors;

/**
 * The neighbor rules for a hexagon grid using Von Neumann Technique.
 *
 * @author Owen Jennings
 */
public class HexagonVonNeumannNeighbors extends GetNeighbors {

  /**
   * Create a Hexagon VonNeumann with provided layers.
   *
   * @param layers The number of layers to use
   */
  public HexagonVonNeumannNeighbors(int layers) {
    super(layers);
  }

  /**
   * Get the directions based on hexagon von neumann rules.
   *
   * @return - the coordinate system for the directions to obtain the neighbors (the top and bottom
   * neighbors of the current hexagon)
   */
  @Override
  public int[][] getDirections(int row, int column) {
    return new int[][]{
        {-1, 0}, // top neighbor
        {1, 0}, // bottom neighbor
    };
  }
}
