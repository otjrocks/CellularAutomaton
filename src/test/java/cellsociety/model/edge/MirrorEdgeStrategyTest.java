package cellsociety.model.edge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cellsociety.model.Grid;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.TestUtils;

class MirrorEdgeStrategyTest {

  private final EdgeStrategy myEdgeStrategy = new MirrorEdgeStrategy();
  private final int myRows = 10;
  private final int myCols = 10;
  private final Grid myGrid = new Grid(myRows, myCols, myEdgeStrategy);

  @BeforeEach
  void setUp() {
    TestUtils.initializeEmptyGrid(myGrid);
  }


  @Test
  public void adjustCoordinate_NegativeOneXValue_returnsMirrorEdge() {
    assertEquals(new Double(1, 1), getAdjustedCoordinate(new Double(-1, 1)));
  }

  @Test
  public void adjustCoordinate_NegativeOneYValue_returnsMirrorEdge() {
    assertEquals(new Double(1, 1), getAdjustedCoordinate(new Double(1, -1)));
  }

  @Test
  public void adjustCoordinate_NegativeTwoXValue_returnsMirrorEdgeTwoOver() {
    assertEquals(new Double(2, 1), getAdjustedCoordinate(new Double(-2, 1)));
  }

  @Test
  public void adjustCoordinate_NegativeTwoYValue_returnsMirrorEdgeTwoOver() {
    assertEquals(new Double(0, 2), getAdjustedCoordinate(new Double(0, -2)));
  }

  @Test
  public void adjustCoordinate_NonEdgePoint_returnsSameCoordinate() {
    assertEquals(new Double(2, 2), getAdjustedCoordinate(new Double(2, 2)));
  }

  @Test
  public void adjustCoordinate_XGreaterThanNumRows_returnsMirrorEdge() {
    assertEquals(new Double(myRows - 2, 2), getAdjustedCoordinate(new Double(myRows, 2)));
  }

  @Test
  public void adjustCoordinate_LargeXGreaterThanNumRows_returnsMirrorEdge() {
    assertEquals(new Double(myRows - 4, 2), getAdjustedCoordinate(new Double(myRows + 2, 2)));
  }

  @Test
  public void adjustCoordinate_LargeYGreaterThanNumColumns_returnsMirrorEdge() {
    assertEquals(new Double(2, myCols - 4), getAdjustedCoordinate(new Double(2, myCols + 2)));
  }

  @Test
  public void adjustCoordinate_BothValuesOutsideGrid_returnsMirrorEdge() {
    assertEquals(new Double(1, myCols - 6),
        getAdjustedCoordinate(new Double(-1, myCols + 4)));
  }

  private Point2D getAdjustedCoordinate(Point2D point) {
    return myEdgeStrategy.adjustCoordinate(point, myRows, myCols);
  }
}