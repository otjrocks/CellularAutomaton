package cellsociety.model.simulation.Instructions;

import cellsociety.model.Grid;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.cell.DarwinCell;
import cellsociety.model.simulation.Instruction;
import java.util.ArrayList;
import java.util.List;

/**
 * Go Instruction class to handle setting a new instruction index
 */
public class GoInstruction implements Instruction {

  /**
   * @param darwinCell - the cell that the instruction is executed on
   * @param arguments  - the list of instructions for the given cell
   * @param grid       - the collection of cell objects
   */
  @Override
  public List<CellUpdate> executeInstruction(DarwinCell darwinCell, List<String> arguments, Grid grid) {
    int instructionIndex = Integer.parseInt(arguments.get(1));
    darwinCell.setCurInstructionIndex(instructionIndex);

    List<CellUpdate> updates = new ArrayList<>();
    updates.add(new CellUpdate(darwinCell.getLocation(), darwinCell));
    return updates;

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
