package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.DefaultCell;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RockPaperScissorsRulesTest {

  private RockPaperScissorsRules rockPaperScissorsRules;
  private Grid grid;
  private Grid fullGrid;
  private Map<String, Double> parameters = new HashMap<>();

  @BeforeEach
  void setUp() {
    grid = new Grid(5, 5);
    parameters.put("minThreshold", 0.5);
    parameters.put("numStates", 3.0);

    rockPaperScissorsRules = new RockPaperScissorsRules(parameters);

    int[][] gridPattern = {
        {1, 2, 3, 4, 5},
        {2, 3, 4, 5, 1},
        {3, 4, 5, 1, 2},
        {4, 5, 1, 2, 3},
        {5, 1, 2, 3, 4}
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
  void getNextState_CellOutOfBounds_ThrowsIndexOutOfBoundsException() {
    Cell cell = new DefaultCell(1, new Point2D.Double(50, 50));

    assertThrows(IndexOutOfBoundsException.class,
        () -> rockPaperScissorsRules.getNextState(cell, grid),
        "Calling getNextState() on a cell that is out of bounds should throw OutofBoundsException.");
  }

  @Test
  void constructor_DefaultParameters_InitializesWithDefaultThreshold() {
    assertEquals(0.5, rockPaperScissorsRules.getParameters().get("minThreshold"));
  }

  @Test
  void getNextState_NeighborsExceedThresholdAndWin_ReturnWinnersState() {
    Cell cell = new DefaultCell(1, new Point2D.Double(2, 2));
    grid.addCell(cell);

    grid.addCell(new DefaultCell(2, new Point2D.Double(2, 3)));
    grid.addCell(new DefaultCell(2, new Point2D.Double(1, 2)));
    grid.addCell(new DefaultCell(2, new Point2D.Double(2, 1)));
    grid.addCell(new DefaultCell(3, new Point2D.Double(3, 2)));
    grid.addCell(new DefaultCell(2, new Point2D.Double(3, 3)));
    grid.addCell(new DefaultCell(2, new Point2D.Double(1, 1)));

    assertEquals(2, rockPaperScissorsRules.getNextState(cell, grid));
  }


  @Test
  void getNextState_NeighborsBelowThresholdAndLose_ReturnCurrentState() {
    Cell cell = new DefaultCell(1, new Point2D.Double(2, 2));
    grid.addCell(cell);

    grid.addCell(new DefaultCell(3, new Point2D.Double(2, 3)));
    grid.addCell(new DefaultCell(4, new Point2D.Double(1, 2)));
    grid.addCell(new DefaultCell(5, new Point2D.Double(2, 1)));
    grid.addCell(new DefaultCell(6, new Point2D.Double(3, 2)));
    grid.addCell(new DefaultCell(1, new Point2D.Double(3, 3)));
    grid.addCell(new DefaultCell(2, new Point2D.Double(1, 1)));

    assertEquals(1, rockPaperScissorsRules.getNextState(cell, grid));
  }

  @Test
  void getNextState_NeighborsExceedThresholdButLose_ReturnCurrentState() {
    Cell cell = new DefaultCell(1, new Point2D.Double(2, 2));
    grid.addCell(cell);

    grid.addCell(new DefaultCell(0, new Point2D.Double(1, 1)));
    grid.addCell(new DefaultCell(0, new Point2D.Double(1, 2)));
    grid.addCell(new DefaultCell(0, new Point2D.Double(1, 3)));
    grid.addCell(new DefaultCell(0, new Point2D.Double(2, 1)));
    grid.addCell(new DefaultCell(0, new Point2D.Double(2, 3)));
    grid.addCell(new DefaultCell(0, new Point2D.Double(3, 1)));
    grid.addCell(new DefaultCell(0, new Point2D.Double(3, 2)));
    grid.addCell(new DefaultCell(0, new Point2D.Double(3, 3)));

    assertEquals(1, rockPaperScissorsRules.getNextState(cell, grid));
  }

  @Test
  void getNextState_CornerCell_ReturnWinnerState() {
    Cell cell = new DefaultCell(1, new Point2D.Double(0, 0));
    grid.addCell(cell);

    grid.addCell(new DefaultCell(2, new Point2D.Double(0, 1)));
    grid.addCell(new DefaultCell(2, new Point2D.Double(1, 0)));
    grid.addCell(new DefaultCell(2, new Point2D.Double(1, 1)));

    assertEquals(2, rockPaperScissorsRules.getNextState(cell, grid));
  }

}
