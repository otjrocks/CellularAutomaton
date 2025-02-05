package cellsociety.model;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.DefaultCell;

class GridTest {

  private Grid myGrid;
  private DefaultCell myCell;
  int myNumRows, myNumCols;

  @BeforeEach
  void setUp() {
    myNumRows = 3;
    myNumCols = 3;
    myGrid = new Grid(myNumRows, myNumCols);
    myCell = new DefaultCell(0, new Double(1, 1));
    myGrid.addCell(myCell);

  }

  @Test
  void getCell() {
    assertNull(myGrid.getCell(0, 0)); // returns null if cell does not exist
    assertEquals(myCell, myGrid.getCell(1, 1));
  }

  @Test
  void testGetCell() {
    assertEquals(myCell, myGrid.getCell(new Point2D.Double(1, 1)));
  }

  @Test
  void addCell() {
    Cell newCell = new DefaultCell(0, new Double(1, 2));
    assertTrue(myGrid.addCell(newCell));
    assertEquals(newCell, myGrid.getCell(new Point2D.Double(1, 2)));
    assertFalse(myGrid.addCell(
        myCell)); // ensure returns false if you try to add a cell where a cell already exists
    Cell replicateCell = new DefaultCell(0, new Double(1, 2));
    assertFalse(myGrid.addCell(
        replicateCell));  // ensure returns false if you try to add a cell where a cell already exists

    // try edge cases for adding new border
    assertFalse(myGrid.addCell(new DefaultCell(0, new Double(myNumRows, 2))));
    assertTrue(myGrid.addCell(new DefaultCell(0, new Double(myNumCols - 1, 0))));
    assertFalse(myGrid.addCell(new DefaultCell(0, new Double(0, myNumCols))));
    assertTrue(myGrid.addCell(new DefaultCell(0, new Double(0, myNumCols - 1))));


  }

  @Test
  void cellExists() {
    assertTrue(myGrid.cellExists(new Point2D.Double(1, 1)));
    assertFalse(myGrid.cellExists(new Point2D.Double(2, 2)));
    assertFalse(myGrid.cellExists(new Point2D.Double(3, 3)));
  }

  @Test
  void updateGrid() {
  }

  @Test
  void getNextStatesForAllCells() {
  }
}