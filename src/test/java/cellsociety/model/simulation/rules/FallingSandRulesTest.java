package cellsociety.model.simulation.rules;

import cellsociety.model.Grid;
import cellsociety.model.cell.DefaultCell;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.rules.FallingSandRules.State;
import java.awt.geom.Point2D.Double;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FallingSandRulesTest {

  private Grid grid;
  private FallingSandRules fallingSandRules;

  @BeforeEach
  void setUp() throws InvalidParameterException {
    // Create a grid with dimensions 3x3 for simplicity
    grid = new Grid(3, 3);
    Map<String, Parameter<?>> parameters = new HashMap<>();
    fallingSandRules = new FallingSandRules(parameters);
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        grid.addCell(new DefaultCell(0, new Double(i, j)));
      }
    }
  }

  @Test
  void grid_SuccessfullyInitialised_EmptyCells() {
    // Test that the grid is empty initially, all cells should have EMPTY state
    for (int row = 0; row < grid.getRows(); row++) {
      for (int col = 0; col < grid.getCols(); col++) {
        assertEquals(FallingSandRules.State.EMPTY.getValue(), grid.getCell(row, col).getState());
      }
    }
  }

  @Test
  void getNextStatesForAllCells_FallingDirectlyDown_Success() {
    // Place sand in the top middle cell
    grid.updateCell(
        new DefaultCell(FallingSandRules.State.SAND.getValue(), grid.getCell(0, 1).getLocation()));

    // Simulate the movement of sand
    fallingSandRules.getNextStatesForAllCells(grid);

    // Check that sand moved down
    assertEquals(FallingSandRules.State.EMPTY.getValue(), grid.getCell(0, 1).getState());
    assertEquals(FallingSandRules.State.SAND.getValue(), grid.getCell(1, 1).getState());
  }

  @Test
  void getNextStatesForAllCells_MustFallDiagonal_Success() {
    // Place sand and a wall in adjacent cells
    grid.updateCell(
        new DefaultCell(FallingSandRules.State.SAND.getValue(), grid.getCell(0, 1).getLocation()));
    grid.updateCell(
        new DefaultCell(FallingSandRules.State.WALL.getValue(), grid.getCell(1, 1).getLocation()));

    // Simulate the movement of sand
    fallingSandRules.getNextStatesForAllCells(grid);

    // Check that sand did not move because it was blocked by the wall
    assertEquals(FallingSandRules.State.EMPTY.getValue(), grid.getCell(0, 1).getState());
    assertEquals(FallingSandRules.State.WALL.getValue(), grid.getCell(1, 1).getState());
    boolean sandExactlyOneDiagonal = grid.getCell(1, 0).getState() == State.SAND.getValue()
        ^ grid.getCell(1, 2).getState() == State.SAND.getValue();
    boolean emptyExactlyOneDiagonal = grid.getCell(1, 0).getState() == State.EMPTY.getValue()
        ^ grid.getCell(1, 2).getState() == State.EMPTY.getValue();
    assertTrue(sandExactlyOneDiagonal && emptyExactlyOneDiagonal);
  }

  @Test
  void getNextStatesForAllCells_MustFallDiagonalRight_Success() {
    // Place sand in the second row, middle cell
    grid.updateCell(
        new DefaultCell(FallingSandRules.State.SAND.getValue(), grid.getCell(1, 1).getLocation()));

    // Place walls on the direct bottom and diagonal left cell
    grid.updateCell(
        new DefaultCell(FallingSandRules.State.WALL.getValue(), grid.getCell(2, 1).getLocation()));
    grid.updateCell(
        new DefaultCell(FallingSandRules.State.WALL.getValue(), grid.getCell(2, 0).getLocation()));

    fallingSandRules.getNextStatesForAllCells(grid);

    // Check that sand moves to the diagonal right position
    assertEquals(FallingSandRules.State.EMPTY.getValue(), grid.getCell(1, 1).getState());
    assertEquals(FallingSandRules.State.SAND.getValue(), grid.getCell(2, 2).getState());
  }

  @Test
  void getNextStatesForAllCells_MustFallDiagonalLeft_Success() {
    // Place sand in the second row, middle cell
    grid.updateCell(
        new DefaultCell(FallingSandRules.State.SAND.getValue(), grid.getCell(1, 1).getLocation()));

    // Place walls on the direct bottom and diagonal right cell
    grid.updateCell(
        new DefaultCell(FallingSandRules.State.WALL.getValue(), grid.getCell(2, 1).getLocation()));
    grid.updateCell(
        new DefaultCell(FallingSandRules.State.WALL.getValue(), grid.getCell(2, 2).getLocation()));

    fallingSandRules.getNextStatesForAllCells(grid);

    // Check that sand moves to the diagonal right position
    assertEquals(FallingSandRules.State.EMPTY.getValue(), grid.getCell(1, 1).getState());
    assertEquals(FallingSandRules.State.SAND.getValue(), grid.getCell(2, 0).getState());
  }

  @Test
  void getNextStatesForAllCells_SandCannotFall_Failure() {
    // Place sand in the second row, middle cell
    grid.updateCell(
        new DefaultCell(FallingSandRules.State.SAND.getValue(), grid.getCell(1, 1).getLocation()));

    // Place walls on the direct left, bottom and diagonal right cell
    grid.updateCell(
        new DefaultCell(FallingSandRules.State.WALL.getValue(), grid.getCell(2, 0).getLocation()));
    grid.updateCell(
        new DefaultCell(FallingSandRules.State.WALL.getValue(), grid.getCell(2, 1).getLocation()));
    grid.updateCell(
        new DefaultCell(FallingSandRules.State.WALL.getValue(), grid.getCell(2, 2).getLocation()));

    fallingSandRules.getNextStatesForAllCells(grid);

    // Check that sand did not move
    assertEquals(State.SAND.getValue(), grid.getCell(1, 1).getState());
    assertEquals(State.WALL.getValue(), grid.getCell(2, 0).getState());
    assertEquals(State.WALL.getValue(), grid.getCell(2, 1).getState());
    assertEquals(State.WALL.getValue(), grid.getCell(2, 2).getState());
  }

  @Test
  void getNextStatesForAllCells_MultipleSandParticles_Success() {
    // Place sand in the top row, middle and right cells
    grid.updateCell(
        new DefaultCell(FallingSandRules.State.SAND.getValue(), grid.getCell(0, 1).getLocation()));
    grid.updateCell(
        new DefaultCell(FallingSandRules.State.SAND.getValue(), grid.getCell(0, 2).getLocation()));

    fallingSandRules.getNextStatesForAllCells(grid);

    // Check that sand moved down in both columns
    assertEquals(FallingSandRules.State.EMPTY.getValue(), grid.getCell(0, 1).getState());
    assertEquals(FallingSandRules.State.EMPTY.getValue(), grid.getCell(0, 2).getState());
    assertEquals(FallingSandRules.State.SAND.getValue(), grid.getCell(1, 1).getState());
    assertEquals(FallingSandRules.State.SAND.getValue(), grid.getCell(1, 2).getState());
    assertEquals(State.EMPTY.getValue(), grid.getCell(1, 0).getState());
    assertEquals(State.EMPTY.getValue(), grid.getCell(2, 0).getState());
    assertEquals(State.EMPTY.getValue(), grid.getCell(2, 1).getState());
    assertEquals(State.EMPTY.getValue(), grid.getCell(2, 2).getState());
  }

  @Test
  void getNumStates_ExpectThreeStates_Success() {
    // Test that the number of states is 3 (EMPTY, WALL, SAND)
    assertEquals(3, fallingSandRules.getNumberStates());
  }
}
