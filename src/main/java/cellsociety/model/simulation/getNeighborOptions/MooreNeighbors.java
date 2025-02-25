package cellsociety.model.simulation.getNeighborOptions;

import cellsociety.model.simulation.GetNeighbors;

public class MooreNeighbors extends GetNeighbors {

  public MooreNeighbors(int layers) {
    super(layers);
  }

  /**
   * @return - the coordinate system for the directions to obtain the neighbors (4 adjacent neighbors AND diagonals)
   */
  @Override
  public int[][] getDirections(int row, int column) {
    return new int[][]{
        {-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}
    };
  }
}
