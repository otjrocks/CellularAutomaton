package cellsociety.model.simulation;

import cellsociety.model.Grid;
import cellsociety.model.cell.DarwinCell;
import java.util.List;

public interface Instruction {

  /**
   * Executes the individual instruction
   *
   * @param darwinCell - the cell that the instruction is executed on
   * @param arguments - the list of instructions for the given cell
   * @param grid - the collection of cell objects
   */
  void executeInstruction(DarwinCell darwinCell, List<String> arguments, Grid grid);

  /**
   * Sets the step size for neighbor configuration
   *
   * @param stepSize - the number of directions to look towards for each configuration
   */
  void setStepSize(int stepSize);

  /**
   * Sets the number of layers ahead to search
   *
   * @param layers - the number of cells to look forward
   */
  void setLayers(int layers);
}
