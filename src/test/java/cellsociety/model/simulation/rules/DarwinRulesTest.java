package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.Grid;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.cell.DarwinCell;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.getNeighborOptions.MooreNeighbors;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DarwinRulesTest {
  private DarwinRules darwinRules;
  private Grid grid;

  @BeforeEach
  void setUp() throws InvalidParameterException {
    grid = new Grid(5, 5);
    for (int i = 0; i < grid.getRows(); i++) {
      for (int j = 0; j < grid.getCols(); j++) {
        grid.addCell(new DarwinCell(0, new Point2D.Double(i, j)));
      }
    }
    darwinRules = new DarwinRules(null, new MooreNeighbors(1));
  }

  @Test
  void getNextStatesForAllCells_MoveInstruction_ExecuteCorrectly() {
    DarwinCell cell = new DarwinCell(2, new Double(2, 2));
    cell.setInstructions("MOVE 2");

    grid.updateCell(cell);

    List<CellUpdate> updates = darwinRules.getNextStatesForAllCells(grid);
    assertEquals(2, updates.size());
    assertEquals(4, updates.get(1).getNextCell().getRow());
    assertEquals(2, updates.get(1).getNextCell().getCol());
  }

  @Test
  void getNextStatesForAllCells_LeftInstruction_UpdatesExecuteCorrectly() {
    DarwinCell cell = new DarwinCell(2, new Double(2, 2));
    cell.setInstructions("LEFT 90");
    grid.updateCell(cell);

    List<CellUpdate> updates = darwinRules.getNextStatesForAllCells(grid);
    assertEquals(1, updates.size());
  }

  @Test
  void getNextStatesForAllCells_RightInstruction_UpdatesExecuteCorrectly() {
    DarwinCell cell = new DarwinCell(2, new Double(2, 2));
    cell.setInstructions("RIGHT 90");
    grid.updateCell(cell);

    List<CellUpdate> updates = darwinRules.getNextStatesForAllCells(grid);
    assertEquals(1, updates.size());
  }

  @Test
  void testConditionalInstruction_IfEmpty_JumpsToCorrectInstruction() {
    DarwinCell cell = new DarwinCell(2, new Double(2, 2));
    cell.setInstructions("IFEMPTY 2");
    cell.setInstructions("MOVE 1");
    cell.setInstructions("MOVE 1");

    grid.updateCell(cell);

    List<CellUpdate> updates = darwinRules.getNextStatesForAllCells(grid);
    assertEquals(2, ((DarwinCell) updates.getFirst().getNextCell()).getCurInstructionIndex());
  }

}