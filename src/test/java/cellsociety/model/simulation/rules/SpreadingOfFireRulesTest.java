package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.DefaultCell;
import cellsociety.model.edge.FixedEdgeStrategy;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.getNeighborOptions.VonNeumannNeighbors;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SpreadingOfFireRulesTest {

  private SpreadingOfFireRules spreadingOfFireRules;
  private Grid grid;

  @BeforeEach
  void setUp() throws InvalidParameterException {
    spreadingOfFireRules = new SpreadingOfFireRules(null, new VonNeumannNeighbors(1));
    grid = new Grid(5, 5, new FixedEdgeStrategy());
  }

  @Test
  void getNextStateForBurningCell() {
    Cell cell = new DefaultCell(2, new Double(1, 1));

    assertEquals(0, spreadingOfFireRules.getNextState(cell, grid),
        "A burning cell turns to an empty cell");
  }

  @Test
  void getNextStateForTreeCellWithBurningNeighbors() {
    Cell cell = new DefaultCell(1, new Double(1, 1));
    grid.addCell(new DefaultCell(2, new Double(1, 2)));

    assertEquals(2, spreadingOfFireRules.getNextState(cell, grid),
        "A tree cell will burn if at least one neighbor is burning.");
  }

  @Test
  void testGetNeighborsMiddleCell() {
    Cell cell = new DefaultCell(1, new Point2D.Double(1, 1));
    grid.addCell(cell);

    grid.addCell(new DefaultCell(2, new Point2D.Double(0, 1))); // Top
    grid.addCell(new DefaultCell(2, new Point2D.Double(2, 1))); // Bottom
    grid.addCell(new DefaultCell(2, new Point2D.Double(1, 0))); // Left
    grid.addCell(new DefaultCell(2, new Point2D.Double(1, 2))); // Right

    List<Cell> neighbors = spreadingOfFireRules.getNeighbors(cell, grid);
    assertEquals(4, neighbors.size(),
        "Middle cell should only have 4 neighbors. Does not include diagonal neighbors");
  }


}