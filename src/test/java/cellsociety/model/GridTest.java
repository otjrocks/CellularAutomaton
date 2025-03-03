package cellsociety.model;

import cellsociety.model.edge.FixedEdgeStrategy;
import cellsociety.model.edge.MirrorEdgeStrategy;
import cellsociety.model.edge.ToroidalEdgeStrategy;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.model.simulation.getNeighborOptions.MooreNeighbors;
import cellsociety.model.simulation.rules.GameOfLifeRules;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Iterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.DefaultCell;
import util.TestUtils;

class GridTest {

  private Grid myGrid;
  private DefaultCell myCell;
  private Simulation myGameOfLifeSimulation;
  int myNumRows, myNumCols;

  @BeforeEach
  void setUp() throws InvalidParameterException {
    myNumRows = 4;
    myNumCols = 4;
    myGrid = new Grid(myNumRows, myNumCols, new FixedEdgeStrategy());
    myCell = new DefaultCell(0, new Double(1, 1));
    myGrid.addCell(myCell);
    myGameOfLifeSimulation = new Simulation(
        new GameOfLifeRules(new HashMap<>(), new MooreNeighbors(1)),
        new SimulationMetaData("GameOfLife", "", "", "", "Moore", 1));
  }

  @Test
  void getRows() {
    assertEquals(myNumRows, myGrid.getRows());
  }

  @Test
  void getCols() {
    assertEquals(myNumCols, myGrid.getCols());
  }

  @Test
  void getCell() {
    assertNull(myGrid.getCell(0, 0)); // returns null if cell does not exist
    assertEquals(myCell, myGrid.getCell(1, 1));
  }

  @Test
  void getCellOutOfBounds() {
    assertNull(myGrid.getCell(0, -1));
  }

  @Test
  void getCellOutOfGridNumColumns() {
    assertNull(myGrid.getCell(0, myNumCols));
  }

  @Test
  void getCellOutOfGridNumRows() {
    assertNull(myGrid.getCell(myNumRows, 0));
  }

  @Test
  void getCellOutOfGridPoint() {
    assertNull(myGrid.getCell(new Double(-1, 1)));
  }

  @Test
  void getNonExistentCell() {
    assertNull(myGrid.getCell(3, 3));
  }

  @Test
  void getNonExistentCellFromPoint() {
    assertNull(myGrid.getCell(new Point2D.Double(3, 3)));
  }


  @Test
  void testGetCellPoint() {
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
    // create a sample blank GOF grid. Then assert that update grid provides correct results based on game
    for (int row = 0; row < myNumRows; row++) {
      for (int col = 0; col < myNumCols; col++) {
        myGrid.addCell(new DefaultCell(0, new Double(row, col)));
      }
    }
    myGrid.updateCell(new DefaultCell(1, new Double(0, 0)));
    myGrid.updateCell(new DefaultCell(1, new Double(2, 2)));
    myGrid.updateCell(new DefaultCell(1, new Double(2, 3)));
    myGrid.updateCell(new DefaultCell(1, new Double(3, 2)));
    myGrid.updateCell(new DefaultCell(1, new Double(3, 3)));
    myGrid.updateGrid(myGameOfLifeSimulation);
    assertEquals(1, myGrid.getCell(3, 3).getState());
    assertEquals(0, myGrid.getCell(0, 0).getState());
  }

  @Test
  void updateCell() {
    myGrid.addCell(new DefaultCell(0, new Double(1, 1)));
    myGrid.updateCell(new DefaultCell(1, new Double(1, 1)));
    assertEquals(1, myGrid.getCell(1, 1).getState());
  }

  @Test
  void getCellIterator() {
    Cell one = new DefaultCell(0, new Double(0, 0));
    Cell two = new DefaultCell(0, new Double(1, 1));
    myGrid.addCell(one);
    myGrid.addCell(two);
    Iterator<Cell> cellIterator = myGrid.getCellIterator();
    assertEquals(0, cellIterator.next().getState());
    assertEquals(0, cellIterator.next().getState());
    cellIterator.remove(); // removing from iterator should not impact grid data
    assertTrue(myGrid.cellExists(new Point2D.Double(1, 1)));
  }

  @Test
  void testSetState() {
    myGrid.addCell(new DefaultCell(0, new Double(0, 0)));
    myGrid.setState(0, 0, 1, myGameOfLifeSimulation);
    assertEquals(1, myGrid.getCell(0, 0).getState());
  }

  @Test
  void setNewEdgeStrategy_SwitchEdgePolicy_ValidCellsReturned() {
    TestUtils.initializeEmptyGrid(myGrid);
    myGrid.setEdgeStrategy(new FixedEdgeStrategy());
    assertNull(myGrid.getCell(-1, 0));
    myGrid.setEdgeStrategy(new ToroidalEdgeStrategy());
    assertEquals(
        new ToroidalEdgeStrategy().adjustCoordinate(new Double(-1, 0), myNumRows, myNumCols),
        myGrid.getCell(-1, 0).getLocation());
    myGrid.setEdgeStrategy(new MirrorEdgeStrategy());
    assertEquals(
        new MirrorEdgeStrategy().adjustCoordinate(new Double(1, myNumCols + 1), myNumRows, myNumCols),
        myGrid.getCell(1, myNumCols + 1).getLocation());
  }

  @Test
  void isWall_CheckIfCellIsWall_Success() {
    assertTrue(myGrid.isWall(0, 1));
    assertTrue(myGrid.isWall(1, 0));
    assertTrue(myGrid.isWall(myNumRows - 1, 1));
    assertTrue(myGrid.isWall( 1, myNumCols - 1));
    assertFalse(myGrid.isWall(-1, 0));
    assertFalse(myGrid.isWall(1, 1)); // valid point that isn't a wall
  }
}