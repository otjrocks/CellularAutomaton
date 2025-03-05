package cellsociety.model.simulation.rules;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cellsociety.model.Grid;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.cell.DarwinCell;
import cellsociety.model.edge.FixedEdgeStrategy;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.neighbors.MooreNeighbors;

class DarwinRulesTest {
  private DarwinRules darwinRules;
  private Grid grid;

  @BeforeEach
  void setUp() throws InvalidParameterException {
    grid = new Grid(5, 5, new FixedEdgeStrategy());
    for (int i = 0; i < grid.getRows(); i++) {
      for (int j = 0; j < grid.getCols(); j++) {
        grid.addCell(new DarwinCell(0, new Point2D.Double(i, j)));
      }
    }
    darwinRules = new DarwinRules(null, new MooreNeighbors(1));
  }

  @Test
  void getNextStatesForAllCells_MoveInstruction_ExecuteCorrectly() {
    DarwinCell cell = new DarwinCell(0, new Double(2, 2));
    cell.setInstructions("MOVE 2");

    grid.updateCell(cell);

    List<CellUpdate> updates = darwinRules.getNextStatesForAllCells(grid);
    assertEquals(2, updates.size());
    assertEquals(4, updates.get(1).getNextCell().getRow());
    assertEquals(2, updates.get(1).getNextCell().getCol());
  }

  @Test
  void getNextStatesForAllCells_LeftInstruction_UpdatesExecuteCorrectly() {
    DarwinCell cell = new DarwinCell(0, new Double(2, 2));
    cell.setInstructions("LEFT 90");
    grid.updateCell(cell);

    List<CellUpdate> updates = darwinRules.getNextStatesForAllCells(grid);
    assertEquals(1, updates.size());
  }

  @Test
  void getNextStatesForAllCells_RightInstruction_UpdatesExecuteCorrectlyAndRounds() {
    DarwinCell cell = new DarwinCell(0, new Double(2, 2));
    cell.setInstructions("RIGHT 70");
    grid.updateCell(cell);

    List<CellUpdate> updates = darwinRules.getNextStatesForAllCells(grid);
    assertEquals(1, updates.size());
    assertEquals(90, cell.getOrientation());
  }

  @Test
  void getNextStatesForAllCells_ConditionalIfEmpty_JumpsToCorrectInstruction() {
    DarwinCell cell = new DarwinCell(0, new Double(2, 2));
    cell.setInstructions("IFEMPTY 2");

    grid.updateCell(cell);
    List<CellUpdate> updates = darwinRules.getNextStatesForAllCells(grid);
    assertEquals(0, cell.getCurInstructionIndex());
  }

  @Test
  void getNextStatesForAllCells_ConditionalIfWall_JumpsCorrectly() {
    DarwinCell cell = new DarwinCell(0, new Double(0, 0));
    cell.setInstructions("IFWALL 2");

    grid.updateCell(cell);

    List<CellUpdate> updates = darwinRules.getNextStatesForAllCells(grid);
    assertEquals(0, ((DarwinCell) updates.getFirst().getNextCell()).getCurInstructionIndex());
    assertEquals(0, cell.getCurInstructionIndex());

  }

  @Test
  void getNextStatesForAllCells_ConditionalIfWall_DoesNotJump() {
    DarwinCell cell = new DarwinCell(0, new Double(2, 2));
    cell.setInstructions("IFWALL 2");

    grid.updateCell(cell);

    List<CellUpdate> updates = darwinRules.getNextStatesForAllCells(grid);
    assertEquals(0, (updates.size()));
    assertEquals(0, cell.getCurInstructionIndex());

  }

  @Test
  void getNextStatesForAllCells_ConditionalIfSame_DoesNotJump() {
    DarwinCell cell1 = new DarwinCell(0, new Double(2, 2));
    DarwinCell cell2 = new DarwinCell(10, new Double(3, 2));

    grid.updateCell(cell1);
    grid.updateCell(cell2);

    List<CellUpdate> updates = darwinRules.getNextStatesForAllCells(grid);
    assertEquals(0, updates.size());
    assertEquals(0, cell1.getCurInstructionIndex());
  }

  @Test
  void getNextStatesForAllCells_ConditionalIfSame_Jumps() {
    DarwinCell cell1 = new DarwinCell(0, new Double(2, 2));
    DarwinCell cell2 = new DarwinCell(0, new Double(3, 2));

    cell1.setInstructions("IFSAME 2");

    grid.updateCell(cell1);
    grid.updateCell(cell2);

    assertEquals(0, cell1.getCurInstructionIndex());
    List<CellUpdate> updates = darwinRules.getNextStatesForAllCells(grid);
    assertEquals(0, cell1.getCurInstructionIndex());
  }

  @Test
  void getNextStatesForAllCells_GoInstruction_SetsCorrectly() {
    DarwinCell cell = new DarwinCell(0, new Double(2, 2));

    cell.setInstructions("GO 2");
    grid.updateCell(cell);
    List<CellUpdate> updates = darwinRules.getNextStatesForAllCells(grid);

    assertEquals(0, cell.getCurInstructionIndex());
  }

  @Test
  void getNextStatesForAllCells_InvalidCommands_NothingHappens() {
    DarwinCell cell = new DarwinCell(0, new Double(2, 2));
    cell.setInstructions("INVALID 5");
    grid.addCell(cell);

    List<CellUpdate> updates = darwinRules.getNextStatesForAllCells(grid);
    assertTrue(updates.isEmpty());
  }

}