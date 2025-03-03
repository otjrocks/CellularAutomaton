package cellsociety.model.edge;


import static org.junit.jupiter.api.Assertions.assertEquals;

import cellsociety.model.Grid;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.TestUtils;

class FixedEdgeStrategyTest {

  private final FixedEdgeStrategy myFixedEdgeStrategy = new FixedEdgeStrategy();
  private final int myRows = 3;
  private final int myCols = 3;
  private final Grid myGrid = new Grid(myRows, myCols, myFixedEdgeStrategy);

  @BeforeEach
  public void setUp() {
    TestUtils.initializeEmptyGrid(myGrid);
  }

  @Test
  public void adjustCoordinate_NegativeOneXValue_returnsSameCoordinate() {
    assertEquals(new Double(-1, 0), getAdjustedCoordinate(new Double(-1, 0)));
  }

  @Test
  public void adjustCoordinate_NegativeOneYValue_returnsSameCoordinate() {
    assertEquals(new Double(0, -1),
        getAdjustedCoordinate(new Double(0, -1)));
  }

  @Test
  public void adjustCoordinate_LargeNegativeXValue_returnsSameCoordinate() {
    assertEquals(new Double(-10, 1),
        getAdjustedCoordinate(new Double(-10, 1)));
  }

  @Test
  public void adjustCoordinate_LargeNegativeYValue_returnsSameCoordinate() {
    assertEquals(new Double(0, -10),
        getAdjustedCoordinate(new Double(0, -10)));
  }

  @Test
  public void adjustCoordinate_validPoint_returnsSameCoordinate() {
    assertEquals(new Double(1, 2),
        getAdjustedCoordinate(new Double(1, 2)));
  }

  private Point2D getAdjustedCoordinate(Point2D point) {
    return myFixedEdgeStrategy.adjustCoordinate(point, myRows, myCols);
  }
}