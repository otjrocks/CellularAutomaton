package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.DefaultCell;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.geom.Point2D.Double;


class PercolationRulesTest {
  private PercolationRules percolationRules;
  private Grid grid;
  private Grid fullGrid;

  @BeforeEach
  void setUp() {
    percolationRules = new PercolationRules();
    int[][] gridPattern = {
        {1, 0, 1, 0, 1},
        {1, 1, 1, 0, 1},
        {0, 1, 0, 1, 1},
        {1, 1, 1, 1, 0},
        {0, 1, 0, 1, 1}
    };

    grid = new Grid(5,5);

    fullGrid = initializeGrid(gridPattern);
  }

  private Grid initializeGrid(int[][] states) {
    int rows = states.length;
    int cols = states[0].length;
    Grid grid = new Grid(rows, cols);

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
       grid.addCell(new DefaultCell(states[row][col], new Point2D.Double(row, col)));
      }
    }
    return grid;
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

  //Debugged a little bit of this function using ChatGPT, as it originally didn't run properly
  @Test
  void testFullPercolationSimulation() {
    int steps = 6;
    for (int step = 0; step < steps; step++) {
      int[][] newStates = new int[fullGrid.getRows()][fullGrid.getCols()];

      for (int row = 0; row < fullGrid.getRows(); row++) {
        for (int col = 0; col < fullGrid.getCols(); col++) {
          Cell cell = fullGrid.getCell(row, col);
          newStates[row][col] = percolationRules.getNextState(cell, fullGrid);
        }
      }

      for (int row = 0; row < fullGrid.getRows(); row++) {
        for (int col = 0; col < fullGrid.getCols(); col++) {
          fullGrid.updateCell(new DefaultCell(newStates[row][col], new Point2D.Double(row, col)));
        }
      }
    }

    // Check if percolation reached the bottom row
    boolean percolated = false;
    for (int col = 0; col < fullGrid.getCols(); col++) {
      if (fullGrid.getCell(fullGrid.getRows() - 1, col).getState() == 2) {
        percolated = true;
        break;
      }
    }

    assertTrue(percolated, "Water should percolate to the bottom row.");
  }

}