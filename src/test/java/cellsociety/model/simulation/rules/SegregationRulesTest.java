package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.cell.DefaultCell;
import cellsociety.model.edge.FixedEdgeStrategy;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.neighbors.MooreNeighbors;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class SegregationRulesTest {

  private SegregationRules segregationRules;
  private Grid grid;
  private final Map<String, Parameter<?>> parameters = new HashMap<>();

  @BeforeEach
  void setUp() throws InvalidParameterException {
    grid = new Grid(5, 5, new FixedEdgeStrategy());
    parameters.put("toleranceThreshold", new Parameter<>(0.3));

    segregationRules = new SegregationRules(parameters, new MooreNeighbors(1));
  }

  @Test
  void getNextStateForCellAboveThreshold() {
    Cell cell = new DefaultCell(1, new Point2D.Double(0, 0));

    grid.addCell(new DefaultCell(1, new Point2D.Double(1, 0)));
    grid.addCell(new DefaultCell(1, new Point2D.Double(0, 1)));
    grid.addCell(new DefaultCell(2, new Point2D.Double(1, 1)));

    assertEquals(1, segregationRules.getNextState(cell, grid));
  }

  @Test
  void getNextStateForCellBelowThreshold() {
    Cell cell = new DefaultCell(1, new Point2D.Double(1, 1));

    grid.addCell(new DefaultCell(1, new Point2D.Double(0, 0)));
    grid.addCell(new DefaultCell(2, new Point2D.Double(1, 0)));
    grid.addCell(new DefaultCell(2, new Point2D.Double(0, 1)));
    grid.addCell(new DefaultCell(2, new Point2D.Double(1, 1)));

    assertEquals(1, segregationRules.getNextState(cell, grid));
  }

  @Test
  void testEmptyCell() {
    Cell cell = new DefaultCell(0, new Point2D.Double(2, 2));

    assertEquals(0, segregationRules.getNextState(cell, grid));
  }

  @Test
  void testCellMarkedToBeMoved() {
    Cell cell = new DefaultCell(1, new Point2D.Double(1, 1));

    grid.addCell(cell);
    grid.addCell(new DefaultCell(2, new Point2D.Double(0, 1)));
    grid.addCell(new DefaultCell(2, new Point2D.Double(1, 0)));
    grid.addCell(new DefaultCell(2, new Point2D.Double(2, 1)));
    grid.addCell(new DefaultCell(2, new Point2D.Double(1, 2)));

    assertEquals(-1, segregationRules.getNextState(cell, grid));
  }

  @Test
  void testSetDefaultParameters() throws InvalidParameterException {
    assertEquals(0.3, segregationRules.getParameters().get("toleranceThreshold").getDouble());
  }

  @Test
  void testEdgeCellNeighbors() {
    Cell cell = new DefaultCell(1, new Point2D.Double(0, 2));
    grid.addCell(cell);

    grid.addCell(new DefaultCell(1, new Point2D.Double(0, 1))); // Left
    grid.addCell(new DefaultCell(1, new Point2D.Double(0, 3))); // Right
    grid.addCell(new DefaultCell(1, new Point2D.Double(1, 2))); // Below

    List<Cell> neighbors = segregationRules.getNeighbors(cell, grid);
    assertEquals(3, neighbors.size(), "Edge cell should have 3 neighbors.");
  }

  @Test
  void testAllSatisfiedCellsStay() {
    for (int row = 0; row < 5; row++) {
      for (int col = 0; col < 5; col++) {
        grid.addCell(new DefaultCell(0, new Point2D.Double(row, col)));
      }
    }

    grid.updateCell(new DefaultCell(1, new Point2D.Double(0, 0)));
    grid.updateCell(new DefaultCell(1, new Point2D.Double(0, 1)));
    grid.updateCell(new DefaultCell(2, new Point2D.Double(1, 0)));

    List<CellUpdate> updates = segregationRules.getNextStatesForAllCells(grid);

    boolean moved = false;
    for (CellUpdate update : updates) {
      if (update.getState() == -1) {
        moved = true;
        break;
      }
    }

    assertFalse(moved);
  }


  @Test
  void testUnsatisfiedCellMoves() {
    for (int row = 0; row < 5; row++) {
      for (int col = 0; col < 5; col++) {
        grid.addCell(new DefaultCell(0, new Point2D.Double(row, col)));
      }
    }

    grid.updateCell(new DefaultCell(1, new Point2D.Double(0, 0))); // Only one cell, should be unsatisfied
    grid.updateCell(new DefaultCell(0, new Point2D.Double(1, 1))); // Empty cell for movement

    List<CellUpdate> updates = segregationRules.getNextStatesForAllCells(grid);

    boolean cellMoved = false;
    for (CellUpdate update : updates) {
      if (update.getState() == 1 && !(update.getRow() == 0) || !(update.getCol() == 0)) {
        cellMoved = true;
        break;
      }
    }

    assertTrue(cellMoved);
  }


  //needed help with ChatGPT on this one
  @Test
  void testMoveCellToEmptyLocationIfAvailable() {
    List<CellUpdate> updates = new ArrayList<>();
    List<Cell> emptyCells = new ArrayList<>();

    Cell movingCell = new DefaultCell(1, new Point2D.Double(2, 2));
    grid.addCell(movingCell);

    Cell emptyCell = new DefaultCell(0, new Point2D.Double(3, 3));
    grid.addCell(emptyCell);
    emptyCells.add(emptyCell);

    segregationRules.moveCellToEmptyLocationIfAvailable(movingCell, emptyCells, updates);

    boolean cellMoved = false;
    boolean oldLocationEmpty = false;
    for (CellUpdate update : updates) {
      if (update.getRow() == 3 && update.getCol() == 3 && update.getState() == 1) {
        cellMoved = true;
      }
      if (update.getRow() == 2 && update.getCol() == 2 && update.getState() == 0) {
        oldLocationEmpty = true;
      }
    }
    assertTrue(cellMoved, "Cell should move to empty location.");
    assertTrue(oldLocationEmpty, "Old location should be empty.");
  }

  @Test
  void testGetAndRemoveRandomEmptyCell() {
    List<Cell> emptyCells = new ArrayList<>();
    emptyCells.add(new DefaultCell(0, new Point2D.Double(1, 1)));
    emptyCells.add(new DefaultCell(0, new Point2D.Double(2, 2)));
    emptyCells.add(new DefaultCell(0, new Point2D.Double(3, 3)));

    Cell removedCell = segregationRules.getAndRemoveRandomEmptyCell(emptyCells);

    assertNotNull(removedCell, "Removed cell should not be null");
    assertEquals(2, emptyCells.size(), "List size should decrease after removal");
    assertFalse(emptyCells.contains(removedCell), "Removed cell should not be in the list");
  }

  @Test
  void testGetEmptyCells() {
    for (int row = 0; row < 5; row++) {
      for (int col = 0; col < 5; col++) {
        grid.addCell(new DefaultCell(0, new Point2D.Double(row, col)));
      }
    }

    List<Cell> emptyCells = new ArrayList<>();
    segregationRules.getEmptyCells(grid, emptyCells);

    assertEquals(25, emptyCells.size(), "Should get all the empty cells");
    for (Cell cell : emptyCells) {
      assertEquals(0, cell.getState(), "All cells should be empty.");
    }
  }

}