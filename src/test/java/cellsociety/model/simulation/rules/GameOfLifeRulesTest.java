package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.DefaultCell;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.getNeighborOptions.MooreNeighbors;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameOfLifeRulesTest {

  private Grid grid;
  private Grid fullGrid;
  private GameOfLifeRules gameOfLifeRules;

  @BeforeEach
  void setUp() throws InvalidParameterException {
    grid = new Grid(5, 5);
    gameOfLifeRules = new GameOfLifeRules(new HashMap<>(), new MooreNeighbors(1));

    int[][] gridPattern = {
        {0, 1, 0, 0, 0},
        {0, 0, 1, 0, 0},
        {1, 1, 1, 0, 0},
        {0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0}
    };

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
  void testGetNextStateForTooLittleNeighbors() {
    Cell cell = new DefaultCell(1, new Double(2, 2));
    grid.updateCell(cell);

    Cell neighbor = new DefaultCell(1, new Double(2, 3));
    grid.updateCell(neighbor);

    assertEquals(0, gameOfLifeRules.getNextState(cell, grid),
        "A cell with less than 2 neighbors dies");

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

    assertEquals(1, gameOfLifeRules.getNextState(cell, grid),
        "A cell with 2-3 neighbors should die.");
  }

  @Test
  void testGetNextStateForInactiveCellWithThreeNeighbors() {
    Cell cell = new DefaultCell(0, new Double(2, 2));

    grid.addCell(new DefaultCell(1, new Double(1, 2)));
    grid.addCell(new DefaultCell(1, new Double(3, 2)));
    grid.addCell(new DefaultCell(1, new Double(2, 3)));

    assertEquals(1, gameOfLifeRules.getNextState(cell, grid),
        "An inactive cell with 3 neighbors becomes active");
  }

  //Needed assistance from ChatGPT on how to run the "negative scenario" tests, so it helped with the testGetNextStateNullCell.
  @Test
  void testGetNextStateNullCell() {
    assertThrows(NullPointerException.class, () -> gameOfLifeRules.getNextState(null, grid),
        "Calling getNextState() on a null cell should throw NullPointerException.");
  }

  @Test
  void testGliderMovements() {
    int steps = 4;
    for (int step = 0; step < steps; step++) {
      int[][] newStates = new int[fullGrid.getRows()][fullGrid.getCols()];

      for (int row = 0; row < fullGrid.getRows(); row++) {
        for (int col = 0; col < fullGrid.getCols(); col++) {
          Cell cell = fullGrid.getCell(row, col);
          newStates[row][col] = gameOfLifeRules.getNextState(cell, fullGrid);
        }
      }

      for (int row = 0; row < fullGrid.getRows(); row++) {
        for (int col = 0; col < fullGrid.getCols(); col++) {
          fullGrid.updateCell(new DefaultCell(newStates[row][col], new Point2D.Double(row, col)));
        }
      }
    }

    assertEquals(1, fullGrid.getCell(1, 2).getState());
    assertEquals(1, fullGrid.getCell(2, 3).getState());
    assertEquals(1, fullGrid.getCell(3, 1).getState());
    assertEquals(1, fullGrid.getCell(3, 2).getState());
    assertEquals(1, fullGrid.getCell(3, 3).getState());

    assertEquals(0, fullGrid.getCell(2, 0).getState());
    assertEquals(0, fullGrid.getCell(2, 1).getState());
    assertEquals(0, fullGrid.getCell(0, 0).getState());


  }
  
}