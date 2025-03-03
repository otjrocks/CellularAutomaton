package cellsociety.model.edge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cellsociety.model.Grid;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.TestUtils;

class ToroidalEdgeStrategyTest {

  private final EdgeStrategy myEdgeStrategy = new ToroidalEdgeStrategy();
  private final int myRows = 3;
  private final int myCols = 3;
  private final Grid myGrid = new Grid(myRows, myCols, myEdgeStrategy);

  @BeforeEach
  void setUp() {
    TestUtils.initializeEmptyGrid(myGrid);
  }


  @Test
  public void adjustCoordinate_NegativeOneXValue_returnsToroidalEdge() {
    assertEquals(new Double(myRows - 1, 1), getAdjustedCoordinate(new Double(-1, 1)));
  }

  @Test
  public void adjustCoordinate_NegativeOneYValue_returnsToroidalEdge() {
    assertEquals(new Double(1, myCols - 1), getAdjustedCoordinate(new Double(1, -1)));
  }

  @Test
  public void adjustCoordinate_NegativeTwoXValue_returnsToroidalEdgeTwoOver() {
    assertEquals(new Double(myRows - 2, 1), getAdjustedCoordinate(new Double(-2, 1)));
  }

  @Test
  public void adjustCoordinate_NegativeTwoYValue_returnsToroidalEdgeTwoOver() {
    assertEquals(new Double(0, myCols - 2), getAdjustedCoordinate(new Double(0, -2)));
  }

  @Test
  public void adjustCoordinate_NonEdgePoint_returnsSameCoordinate() {
    assertEquals(new Double(2, 2), getAdjustedCoordinate(new Double(2, 2)));
  }

  @Test
  public void adjustCoordinate_XGreaterThanNumRows_returnsToroidalEdge() {
    assertEquals(new Double(0, 2), getAdjustedCoordinate(new Double(myRows, 2)));
  }

  @Test
  public void adjustCoordinate_LargeXGreaterThanNumRows_returnsToroidalEdge() {
    assertEquals(new Double(2, 2), getAdjustedCoordinate(new Double(myRows + 2, 2)));
  }

  @Test
  public void adjustCoordinate_LargeYGreaterThanNumColumns_returnsToroidalEdge() {
    assertEquals(new Double(2, 2), getAdjustedCoordinate(new Double(2, myCols + 2)));
  }

  @Test
  public void adjustCoordinate_BothValuesOutsideGrid_returnsToroidalEdge() {
    assertEquals(new Double(myRows - 1, 0),
        getAdjustedCoordinate(new Double(-1, myCols + myCols)));
  }

  private Point2D getAdjustedCoordinate(Point2D point) {
    return myEdgeStrategy.adjustCoordinate(point, myRows, myCols);
  }
}