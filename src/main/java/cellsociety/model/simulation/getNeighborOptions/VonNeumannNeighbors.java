package cellsociety.model.simulation.getNeighborOptions;

import cellsociety.model.simulation.rules.GetNeighbors;

public class VonNeumannNeighbors extends GetNeighbors {

  public VonNeumannNeighbors(int layers) {
    super(layers);
  }

  /**
   * @return - the coordinate system for the directions to obtain the neighbors (4 adjacent neighbors, no diagonals)
   */
  @Override
  public int[][] getDirections() {
    return new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
  }
}
