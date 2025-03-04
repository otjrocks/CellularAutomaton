package cellsociety.model.edge;

import java.awt.geom.Point2D;

/**
 * An implementation of a Fixed Boundary grid edge strategy.
 *
 * @author Owen Jennings
 */
public class FixedEdgeStrategy implements EdgeStrategy {

  @Override
  public Point2D adjustCoordinate(Point2D point, int numRows, int numCols) {
    return point; // return the point provided and do not adjust
  }
}
