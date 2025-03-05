package cellsociety.model.simulation.neighbors;

import cellsociety.model.simulation.GetNeighbors;

/**
 * The default implementation of VonNeumann Neighbor policy (for a rectangle/square grid setup).
 *
 * @author Owen Jennings
 */
public class VonNeumannNeighbors extends GetNeighbors {

  /**
   * The default constructor of this neighbor policy.
   *
   * @param layers The number of layers to include
   */
  public VonNeumannNeighbors(int layers) {
    super(layers);
  }

  /**
   * The directions based on Von Neumann neighbor rules.
   *
   * @return - the coordinate system for the directions to obtain the neighbors (4 adjacent
   * neighbors, no diagonals)
   */
  @Override
  public int[][] getDirections(int row, int column) {
    return new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
  }
}
