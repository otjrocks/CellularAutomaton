package cellsociety.model.simulation.Instructions;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.DarwinCell;
import cellsociety.model.simulation.Instruction;
import cellsociety.model.simulation.rules.DarwinRules.State;
import java.awt.geom.Point2D;
import java.util.List;

public class ConditionalInstruction implements Instruction {
  private int layers;
  private final String conditionType;

  public ConditionalInstruction(String conditionType) {
    this.conditionType = conditionType;
  }

  /**
   * @param darwinCell - the cell that the instruction is executed on
   * @param arguments  - the list of instructions for the given cell
   * @param grid       - the collection of cell objects
   */
  @Override
  public void executeInstruction(DarwinCell darwinCell, List<String> arguments, Grid grid) {
    Point2D direction = darwinCell.getFrontDirection();
    int newRow = darwinCell.getRow();
    int newCol = darwinCell.getCol();
    int nextInstruction = Integer.parseInt(arguments.get(1));


    for (int i = 0; i < layers; i++) {
      newRow += (int) direction.getX();
      newCol += (int) direction.getY();

      Cell curCell = grid.getCell(newRow, newCol);
      if (curCell == null) {
        continue;
      }

      if (checkIfConditionIsMet(darwinCell, curCell, grid)) {
        darwinCell.setCurInstructionIndex(nextInstruction);
      }
    }
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
