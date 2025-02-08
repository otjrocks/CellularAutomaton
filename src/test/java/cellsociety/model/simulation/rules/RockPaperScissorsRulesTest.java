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
  private Map<String, Double> parameters = new HashMap<>();

  @BeforeEach
  void setUp() {
    grid = new Grid(5, 5);
    parameters.put("minThreshold", 0.5);
    parameters.put("numStates", 3.0);

    rockPaperScissorsRules = new RockPaperScissorsRules(parameters);
  }

  @Test
  void getNextState_CellOutOfBounds_ThrowsIndexOutOfBoundsException() {
    Cell cell = new DefaultCell(1, new Point2D.Double(50, 50));

    assertThrows(IndexOutOfBoundsException.class,
        () -> rockPaperScissorsRules.getNextState(cell, grid),
        "Calling getNextState() on a cell that is out of bounds should throw OutofBoundsException.");
  }

  @Test
  void getNextState_EmptyCell_ReturnsEmptyState() {
    Cell cell = new DefaultCell(0, new Point2D.Double(2, 2));

    assertEquals(0, rockPaperScissorsRules.getNextState(cell, grid));
  }

  @Test
  void constructor_DefaultParameters_InitializesWithDefaultThreshold() {
    assertEquals(0.5, rockPaperScissorsRules.getParameters().get("minThreshold"));
  }

  @Test
  void getNextState_NeighborsExceedThreshold_ReturnWinnersState() {
    Cell cell = new DefaultCell(1, new Point2D.Double(2, 2));
    grid.addCell(cell);

    grid.addCell(new DefaultCell(2, new Point2D.Double(2, 3)));
    grid.addCell(new DefaultCell(2, new Point2D.Double(1, 2)));
    grid.addCell(new DefaultCell(0, new Point2D.Double(2, 1)));
    grid.addCell(new DefaultCell(3, new Point2D.Double(3, 2)));
    grid.addCell(new DefaultCell(2, new Point2D.Double(3, 3)));
    grid.addCell(new DefaultCell(2, new Point2D.Double(1, 1)));

    assertEquals(2, rockPaperScissorsRules.getNextState(cell, grid));
  }


  @Test
  void getNextState_NeighborsBelowThreshold_ReturnCurrentState() {
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
  void getNextState_NeighborsAreEmpty_ReturnCurrentState() {
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




}
