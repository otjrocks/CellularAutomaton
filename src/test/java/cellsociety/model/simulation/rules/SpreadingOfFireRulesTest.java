package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.DefaultCell;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SpreadingOfFireRulesTest {
  private SpreadingOfFireRules spreadingOfFireRules;
  private Grid grid;

  @BeforeEach
  void setUp() {
    spreadingOfFireRules = new SpreadingOfFireRules();
    grid = new Grid(5, 5);
  }

  @Test
  void getNextStateForBurningCell() {
    Cell cell = new DefaultCell(2, new Double(1, 1));

    assertEquals(0, spreadingOfFireRules.getNextState(cell, grid), "A burning cell turns to an empty cell");
  }

  @Test
  void getNextStateForTreeCellWithBurningNeighbors() {
    Cell cell = new DefaultCell(1, new Double(1, 1));
    grid.addCell(new DefaultCell(2, new Double(1, 2)));

    assertEquals(2, spreadingOfFireRules.getNextState(cell, grid), "A tree cell will burn if at least one neighbor is burning.");
  }

  @Test
  void testCellInBounds() {
    Cell cell = new DefaultCell(1, new Point2D.Double(50, 50));

    assertThrows(IndexOutOfBoundsException.class, () -> spreadingOfFireRules.getNextState(cell, grid),
        "Calling getNextState() on a cell that is out of bounds should throw OutofBoundsException.");
  }

  @Test
  void testCellHasValidCoordinates() {
    Cell cell = new DefaultCell(1, new Point2D.Double(-1, -1));

    assertThrows(IndexOutOfBoundsException.class, () -> spreadingOfFireRules.getNextState(cell, grid),
        "Calling getNextState() on an coordinate with an invalid location should throw OutofBoundsException.");
  }
}