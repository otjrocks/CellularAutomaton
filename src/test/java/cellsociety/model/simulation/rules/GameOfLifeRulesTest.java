package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.DefaultCell;
import java.awt.geom.Point2D.Double;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameOfLifeRulesTest {
  private Grid grid;
  private GameOfLifeRules gameOfLifeRules;

  @BeforeEach
  void setUp() {
    grid = new Grid(5, 5);
    gameOfLifeRules = new GameOfLifeRules();

  }

  @Test
  void testGetNextStateForTooLittleNeighbors() {
    Cell cell = new DefaultCell(1, new Double(2, 2));
    grid.updateCell(cell);

    Cell neighbor = new DefaultCell(1, new Double(2, 3));
    grid.updateCell(neighbor);

    assertEquals(0, gameOfLifeRules.getNextState(cell, grid), "A cell with less than 2 neighbors dies");

  }

  @Test
  void testGetNextStateForTooManyNeighbors() {
    Cell cell = new DefaultCell(1, new Double(2, 2));

    grid.addCell(new DefaultCell(1, new Double(1, 2)));
    grid.addCell(new DefaultCell(1, new Double(3, 2)));
    grid.addCell(new DefaultCell(1, new Double(2, 3)));
    grid.addCell(new DefaultCell(1, new Double(2, 1)));

    assertEquals(0, gameOfLifeRules.getNextState(cell, grid), "A cell > 3 neighbors should die.");
  }

  @Test
  void testGetNextStateForTooEnoughNeighbors() {
    Cell cell = new DefaultCell(1, new Double(2, 2));

    grid.addCell(new DefaultCell(1, new Double(1, 2)));
    grid.addCell(new DefaultCell(1, new Double(3, 2)));
    grid.addCell(new DefaultCell(1, new Double(2, 3)));

    assertEquals(1, gameOfLifeRules.getNextState(cell, grid), "A cell with 2-3 neighbors should die.");
  }

  @Test
  void testGetNextStateForInactiveCellWithThreeNeighbors() {
    Cell cell = new DefaultCell(0, new Double(2, 2));

    grid.addCell(new DefaultCell(1, new Double(1, 2)));
    grid.addCell(new DefaultCell(1, new Double(3, 2)));
    grid.addCell(new DefaultCell(1, new Double(2, 3)));

    assertEquals(1, gameOfLifeRules.getNextState(cell, grid), "An inactive cell with 3 neighbors becomes active");
  }

  //Needed assistance from ChatGPT on how to run the "negative scenario" tests, so it helped with the testGetNextStateNullCell.
  @Test
  void testGetNextStateNullCell() {
    assertThrows(NullPointerException.class, () -> gameOfLifeRules.getNextState(null, grid),
        "Calling getNextState() on a null cell should throw NullPointerException.");
  }

  @Test
  void testCellInBounds() {
    Cell cell = new DefaultCell(1, new Double(50, 50));

    assertThrows(IndexOutOfBoundsException.class, () -> gameOfLifeRules.getNextState(cell, grid),
        "Calling getNextState() on a cell that is out of bounds should throw OutofBoundsException.");
  }

  @Test
  void testCellHasValidCoordinates() {
    Cell cell = new DefaultCell(1, new Double(-1, -1));

    assertThrows(IndexOutOfBoundsException.class, () -> gameOfLifeRules.getNeighbors(cell, grid),
        "Calling getNeighbors() on an coordinate with an invalid location should throw IllegalArgumentException.");
  }
}