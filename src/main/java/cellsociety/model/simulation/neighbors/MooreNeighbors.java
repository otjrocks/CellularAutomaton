package cellsociety.model.simulation.neighbors;

import cellsociety.model.simulation.GetNeighbors;

/**
 * The default implementation of moore neighbors (for square/rectangle neighbors
 *
 * @author Justin Aronwald
 */
public class MooreNeighbors extends GetNeighbors {

  /**
   * The default constructor
   *
   * @param layers The number of layers of neighbors to include
   */
  public MooreNeighbors(int layers) {
    super(layers);
  }

  /**
   * @return - the coordinate system for the directions to obtain the neighbors (4 adjacent
   * neighbors AND diagonals)
   */
  @Override
  public int[][] getDirections(int row, int column) {
    return new int[][]{
        {-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}
    };
  }
}
