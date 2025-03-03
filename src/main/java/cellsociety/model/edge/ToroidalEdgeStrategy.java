package cellsociety.model.edge;

import java.awt.geom.Point2D;

/**
 * An implementation of Toroidal Edge wrapping.
 *
 * @author Owen Jennings
 */
public class ToroidalEdgeStrategy implements EdgeStrategy {

  @Override
  public Point2D adjustCoordinate(Point2D point, int numRows, int numCols) {
    // I used ChatGPT to help get the newX and newY for this edge strategy
    int newX = (((int) point.getX() % numRows) + numRows) % numRows;
    int newY = (((int) point.getY() % numCols) + numCols) % numCols;
    return new Point2D.Double(newX, newY);
  }
}
