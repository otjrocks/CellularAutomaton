package cellsociety.model.simulation.Instructions;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.cell.DarwinCell;
import cellsociety.model.simulation.Instruction;
import cellsociety.model.simulation.rules.DarwinRules.State;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConditionalInstruction implements Instruction {
  private int layers;
  private final String conditionType;
  private final Random random = new Random();

  public ConditionalInstruction(String conditionType) {
    this.conditionType = conditionType;
  }

  /**
   * @param darwinCell - the cell that the instruction is executed on
   * @param arguments  - the list of instructions for the given cell
   * @param grid       - the collection of cell objects
   */
  @Override
  public List<CellUpdate> executeInstruction(DarwinCell darwinCell, List<String> arguments, Grid grid) {
    Point2D direction = darwinCell.getFrontDirection();
    int newRow = darwinCell.getRow();
    int newCol = darwinCell.getCol();
    int nextInstruction = Integer.parseInt(arguments.get(1));

    List<CellUpdate> updates = new ArrayList<>();

    List<CellUpdate> updates1 = handleRandomCase(darwinCell,
        nextInstruction, updates);
    if (updates1 != null) {
      return updates1;
    }

    checkAllConditions(darwinCell, grid, newRow, direction, newCol, nextInstruction, updates);
    return updates;
  }

  private void checkAllConditions(DarwinCell darwinCell, Grid grid, int newRow, Point2D direction,
      int newCol, int nextInstruction, List<CellUpdate> updates) {
    for (int i = 0; i < layers; i++) {
      newRow += (int) direction.getX();
      newCol += (int) direction.getY();

      Cell curCell = grid.getCell(newRow, newCol);
      if (curCell == null) {
        continue;
      }

      if (checkIfConditionIsMet(darwinCell, curCell, grid)) {
        darwinCell.setCurInstructionIndex(nextInstruction);
        updates.add(new CellUpdate(darwinCell.getLocation(), darwinCell));
      }
    }
  }

  private List<CellUpdate> handleRandomCase(DarwinCell darwinCell, int nextInstruction,
      List<CellUpdate> updates) {
    if ( conditionType.equals("IFRANDOM") || conditionType.equals("RND?")) {
      if (random.nextBoolean()) {
        darwinCell.setCurInstructionIndex(nextInstruction);
        updates.add(new CellUpdate(darwinCell.getLocation(), darwinCell));
      }
      return updates;
    }
    return null;
  }

  private boolean checkIfConditionIsMet(DarwinCell darwinCell, Cell curCell, Grid grid) {
    return switch (conditionType) {
      case "IFEMPTY", "EMP?" -> curCell.getState() == State.EMPTY.getValue();
      case "IFWALL", "WL?" -> grid.isWall(curCell.getRow(), curCell.getCol());
      case "IFSAME", "SM?" -> curCell.getState() == darwinCell.getState();
      case "IFENEMY", "EMY?" -> curCell.getState() != State.EMPTY.getValue()
          && curCell.getState() != darwinCell.getState();
      default -> false;
    };
  }

  /**
   * @param stepSize - the number of directions to look towards for each configuration
   */
  @Override
  public void setStepSize(int stepSize) {

  }

  /**
   * @param layers - the number of cells to look forward
   */
  @Override
  public void setLayers(int layers) {
    this.layers = layers;
  }
}
