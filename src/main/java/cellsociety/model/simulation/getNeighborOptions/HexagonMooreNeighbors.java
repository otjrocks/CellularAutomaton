package cellsociety.model.simulation.getNeighborOptions;

import cellsociety.model.simulation.GetNeighbors;

public class HexagonMooreNeighbors extends GetNeighbors {

  public HexagonMooreNeighbors(int layers) {
    super(layers);
  }

  /**
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
