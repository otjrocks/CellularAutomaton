package cellsociety.model.edge;

import java.awt.geom.Point2D;

/**
 * Defines a strategy for adjusting grid coordinates based on different edge-handling policies
 * (fixed bounds, toroidal wrapping, mirroring).
 *
 * @author Owen Jennings
 */
public interface EdgeStrategy {

  /**
   * Computes the adjusted coordinate for a given point based on the selected edge-handling
   * strategy. If the point is within bounds, it may remain unchanged.
   *
   * @param point   The point being adjusted.
   * @param numRows The number of rows in the grid.
   * @param numCols The number of columns in the grid.
   * @return A Point2D representing the adjusted coordinate based on the specific logic of an edge
   * strategy.
   */
  Point2D adjustCoordinate(Point2D point, int numRows, int numCols);
}
