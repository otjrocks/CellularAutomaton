package cellsociety.model.simulation.getNeighborOptions;

import cellsociety.model.simulation.GetNeighbors;

public class HexagonVonNeumannNeighbors extends GetNeighbors {

  public HexagonVonNeumannNeighbors(int layers) {
    super(layers);
  }

  /**
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
