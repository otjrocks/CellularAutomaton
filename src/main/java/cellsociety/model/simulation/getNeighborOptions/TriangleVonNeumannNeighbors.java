package cellsociety.model.simulation.getNeighborOptions;

import cellsociety.model.simulation.GetNeighbors;

public class TriangleVonNeumannNeighbors extends GetNeighbors {

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
