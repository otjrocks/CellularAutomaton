package cellsociety.model.edge;

import java.awt.geom.Point2D;

/**
 * An implementation of the Mirror edge policy.
 *
 * @author Owen Jennings
 */
public class MirrorEdgeStrategy implements EdgeStrategy {

  @Override
  public Point2D adjustCoordinate(Point2D point, int numRows, int numCols) {
    int x = (int) point.getX();
    int y = (int) point.getY();

    if (x < 0) {
      x = -x;
    }
    if (x >= numRows) {
      x = 2 * numRows - x - 2;
    }
    if (y < 0) {
      y = -y;
    }
    if (y >= numCols) {
      y = 2 * numCols - y - 2;
    }

    return new Point2D.Double(x, y);
  }
}
