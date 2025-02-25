package cellsociety.model.simulation.getNeighborOptions;

import cellsociety.model.simulation.GetNeighbors;

public class TriangleMooreNeighbors extends GetNeighbors {

  public TriangleMooreNeighbors(int layers) {
    super(layers);
  }

  /**
   * @return - the coordinate system for the directions to obtain the neighbors (the 12 cells that
   * touch the central triangle)
   */
  @Override
  public int[][] getDirections(int row, int column) {
    boolean isUpwardFacingTriangle = (row + column) % 2 == 0;
    // if sum of coordinates is even then triangle is upward facing based on setup on grid
    return isUpwardFacingTriangle ?
        getUpwardFacingDirections() :
        getDownwardFacingDirections();
  }

  private static int[][] getUpwardFacingDirections() {
    /*
    Upward facing triangle
    Neighbors where CC is the center cell/triangle
    -- 01 02 03 --
    04 05 CC 06 07
    08 09 10 11 12
     */
    return new int[][]{
        {-1, -1}, // 1
        {-1, 0}, // 2
        {-1, 1}, // 3
        {0, -2}, // 4
        {0, -1}, // 5
        {0, 1}, // 6
        {0, 2}, // 7
        {1, -2}, // 8
        {1, -1}, // 9
        {1, 0}, // 10
        {1, 1}, // 11
        {1, 2}, // 12
    };
  }

  private static int[][] getDownwardFacingDirections() {
    /*
    Downward facing triangle
    Neighbors where CC is the center cell
    01 02 03 04 05
    06 07 CC 08 09
    -- 10 11 12 --
     */
    return new int[][]{
        {-1, -2}, // 1
        {-1, -1}, // 2
        {-1, 0}, // 3
        {-1, 1}, // 4
        {-1, 2}, // 5
        {0, -2}, // 6
        {0, -1}, // 7
        {0, 1}, // 8
        {0, 2}, // 9
        {1, -1}, // 10
        {1, 0}, // 11
        {1, 1}, // 12
    };
  }
}
