package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.DefaultCell;
import java.awt.geom.Point2D.Double;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.geom.Point2D;


class SegregationModelRulesTest {

  private SegregationModelRules segregationModelRules;
  private Grid grid;
  private Map<String, Double> parameters = new HashMap<>();

  @BeforeEach
  void setUp() {
    grid = new Grid(5, 5);
    parameters.put("toleranceThreshold", 0.3);

    segregationModelRules = new SegregationModelRules(parameters);
  }

  @Test
  void getNextStateForCellAboveThreshold() {
    Cell cell = new DefaultCell(1, new Point2D.Double(0, 0));

    grid.addCell(new DefaultCell(1, new Point2D.Double(1, 0)));
    grid.addCell(new DefaultCell(1, new Point2D.Double(0, 1)));
    grid.addCell(new DefaultCell(2, new Point2D.Double(1, 1)));

    assertEquals(1, segregationModelRules.getNextState(cell, grid));
  }

  @Test
  void getNextStateForCellBelowThreshold() {
    Cell cell = new DefaultCell(1, new Point2D.Double(1, 1));

    grid.addCell(new DefaultCell(1, new Point2D.Double(0, 0)));
    grid.addCell(new DefaultCell(2, new Point2D.Double(1, 0)));
    grid.addCell(new DefaultCell(2, new Point2D.Double(0, 1)));
    grid.addCell(new DefaultCell(2, new Point2D.Double(1, 1)));

    assertEquals(1, segregationModelRules.getNextState(cell, grid));
  }

  @Test
  void testCellInBounds() {
    Cell cell = new DefaultCell(1, new Double(50, 50));

    assertThrows(IndexOutOfBoundsException.class, () -> segregationModelRules.getNextState(cell, grid),
        "Calling getNextState() on a cell that is out of bounds should throw OutofBoundsException.");
  }

  @Test
  void testCellHasValidCoordinates() {
    Cell cell = new DefaultCell(1, new Double(-1, -1));

    assertThrows(IndexOutOfBoundsException.class, () -> segregationModelRules.getNextState(cell, grid),
        "Calling getNextState() on an coordinate with an invalid location should throw OutofBoundsException.");
  }
}