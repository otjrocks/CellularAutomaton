package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.DefaultCell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.geom.Point2D.Double;


class PercolationRulesTest {
  private PercolationRules percolationRules;
  private Grid grid;

  @BeforeEach
  void setUp() {
    percolationRules = new PercolationRules();
    grid = new Grid(5, 5);
  }

  @Test
  void testGetNextStateForTopRow() {
    Cell cell = new DefaultCell(1, new Double(0, 0));
    grid.addCell(cell);
    assertEquals(2, percolationRules.getNextState(cell, grid));
  }

  @Test
  void testGetNextStateForFilledNeighbor() {
    Cell cell = new DefaultCell(2, new Double(0, 0));
    grid.addCell(cell);
    Cell cell2 = new DefaultCell(1, new Double(0, 0));

    assertEquals(2, percolationRules.getNextState(cell2, grid));
  }
  @Test
  void testCellInBounds() {
    Cell cell = new DefaultCell(1, new Double(50, 50));

    assertThrows(IndexOutOfBoundsException.class, () -> percolationRules.getNextState(cell, grid),
        "Calling getNextState() on a cell that is out of bounds should throw an OutofBoundsException.");
  }

}