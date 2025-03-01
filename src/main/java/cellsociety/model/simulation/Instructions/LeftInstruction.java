package cellsociety.model.simulation.Instructions;

import cellsociety.model.Grid;
import cellsociety.model.cell.DarwinCell;
import cellsociety.model.simulation.Instruction;
import java.util.List;

public class LeftInstruction implements Instruction {
  private int stepSize; // Store step size to avoid passing it every time

  @Override
  public void setStepSize(int stepSize) {
    this.stepSize = stepSize;
  }

  /**
   * @param layers - the number of cells to look forward
   */
  @Override
  public void setLayers(int layers) {
    //not needed here
  }

  /**
   * @param darwinCell -  the cell that the instruction is executed on
   * @param arguments - the list of arguments needed - i.e instructions, numMovements
   * @param grid         - the collection of cell objects
   */
  @Override
  public void executeInstruction(DarwinCell darwinCell, List<String> arguments, Grid grid) {
    int degrees = Integer.parseInt(arguments.get(1));
    darwinCell.turnLeft(degrees, stepSize);
  }
}
