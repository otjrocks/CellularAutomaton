package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.DefaultCell;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RockPaperScissorsRulesTest {

  private RockPaperScissorsRules rockPaperScissorsRules;
  private Grid grid;
  private final Map<String, Parameter<?>> parameters = new HashMap<>();

  @BeforeEach
  void setUp() throws InvalidParameterException {
    grid = new Grid(5, 5);
    parameters.put("minThreshold", new Parameter<>(0.5));
    parameters.put("numStates", new Parameter<>(3));

    rockPaperScissorsRules = new RockPaperScissorsRules(parameters);

    int[][] gridPattern = {
        {1, 2, 3, 4, 5},
        {2, 3, 4, 5, 1},
        {3, 4, 5, 1, 2},
        {4, 5, 1, 2, 3},
        {5, 1, 2, 3, 4}
    };

    initializeGrid(gridPattern);
  }

  private void initializeGrid(int[][] states) {
    int rows = states.length;
    int cols = states[0].length;
    Grid grid = new Grid(rows, cols);

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        grid.addCell(new DefaultCell(states[row][col], new Point2D.Double(row, col)));
      }
    }
  }

  @Test
  void getNextState_CellOutOfBounds_ThrowsIndexOutOfBoundsException() {
    Cell cell = new DefaultCell(1, new Point2D.Double(50, 50));

    assertThrows(IndexOutOfBoundsException.class,
        () -> rockPaperScissorsRules.getNextState(cell, grid),
        "Calling getNextState() on a cell that is out of bounds should throw OutofBoundsException.");
  }

  @Test
  void constructor_DefaultParameters_InitializesWithDefaultThreshold() throws InvalidParameterException {
    assertEquals(0.5, rockPaperScissorsRules.getParameters().get("minThreshold").getDouble());
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

  @Test
  void getNextState_LotOfStates_ReturnWinner() throws InvalidParameterException {
    parameters.put("numStates", new Parameter<>(20));
    parameters.put("minThreshold", new Parameter<>(0.3));
    rockPaperScissorsRules = new RockPaperScissorsRules(parameters);

    Cell cell = new DefaultCell(10, new Point2D.Double(2, 2));
    grid.addCell(cell);

    grid.addCell(new DefaultCell(13, new Point2D.Double(1, 2)));
    grid.addCell(new DefaultCell(13, new Point2D.Double(2, 3)));
    grid.addCell(new DefaultCell(13, new Point2D.Double(3, 2)));
    grid.addCell(new DefaultCell(14, new Point2D.Double(3, 3)));

    assertEquals(13, rockPaperScissorsRules.getNextState(cell, grid));
  }

  @Test
  void getNextState_LotOfStates_ReturnCurrentState()
      throws InvalidParameterException {
    parameters.put("numStates", new Parameter<>(20));
    parameters.put("minThreshold", new Parameter<>(0.25));
    rockPaperScissorsRules = new RockPaperScissorsRules(parameters);

    Cell cell = new DefaultCell(15, new Point2D.Double(2, 2));
    grid.addCell(cell);

    grid.addCell(new DefaultCell(14, new Point2D.Double(1, 2)));
    grid.addCell(new DefaultCell(11, new Point2D.Double(2, 3)));
    grid.addCell(new DefaultCell(11, new Point2D.Double(3, 2)));
    grid.addCell(new DefaultCell(11, new Point2D.Double(3, 3)));

    assertEquals(15, rockPaperScissorsRules.getNextState(cell, grid));
  }

  @Test
  void getNextState_TenStates_EdgeCaseWrapAroundLogic() throws InvalidParameterException {
    parameters.put("numStates", new Parameter<>(10));
    parameters.put("minThreshold", new Parameter<>(0.3));
    rockPaperScissorsRules = new RockPaperScissorsRules(parameters);

    Cell cell = new DefaultCell(9, new Point2D.Double(2, 2));
    grid.addCell(cell);

    grid.addCell(new DefaultCell(1, new Point2D.Double(1, 2)));
    grid.addCell(new DefaultCell(0, new Point2D.Double(2, 3)));
    grid.addCell(new DefaultCell(1, new Point2D.Double(3, 2)));
    grid.addCell(new DefaultCell(2, new Point2D.Double(3, 3)));

    assertEquals(1, rockPaperScissorsRules.getNextState(cell, grid));
  }



}
