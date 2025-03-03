package cellsociety.model.edge;

import java.awt.geom.Point2D;

public class FixedEdgeStrategy implements EdgeStrategy {

  @Override
  public Point2D adjustCoordinate(Point2D point, int numRows, int numCols) {
    return point; // return the point provided and do not adjust
  }
}
