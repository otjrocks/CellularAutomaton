package cellsociety.model.simulation.Instructions;

import cellsociety.model.Grid;
import cellsociety.model.cell.DarwinCell;
import cellsociety.model.simulation.Instruction;
import java.util.List;

public class GoInstruction implements Instruction {

  /**
   * @param darwinCell - the cell that the instruction is executed on
   * @param arguments  - the list of instructions for the given cell
   * @param grid       - the collection of cell objects
   */
  @Override
  public void executeInstruction(DarwinCell darwinCell, List<String> arguments, Grid grid) {
    int instructionIndex = Integer.parseInt(arguments.get(1));
    darwinCell.setCurInstructionIndex(instructionIndex);
  }

  /**
   * @param stepSize - the number of directions to look towards for each configuration
   */
  @Override
  public void setStepSize(int stepSize) {
  //not needed here
  }

  /**
   * @param layers - the number of cells to look forward
   */
  @Override
  public void setLayers(int layers) {
    //not needed here
  }
}
