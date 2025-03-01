package cellsociety.model.simulation.Instructions;

import cellsociety.model.Grid;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.cell.DarwinCell;
import cellsociety.model.simulation.Instruction;
import java.util.List;

public abstract class TurnInstruction implements Instruction {
  private int stepSize;

  @Override
  public void setStepSize(int stepSize) {
    this.stepSize = stepSize;
  }

  @Override
  public void setLayers(int layers) {
    // Not needed
  }

  @Override
  public List<CellUpdate> executeInstruction(DarwinCell darwinCell, List<String> arguments, Grid grid) {
    int degrees = Integer.parseInt(arguments.get(1));
    turn(darwinCell, degrees, stepSize);
    return List.of();
  }

  /**
   * Abstract method for turning the cell.
   */
  protected abstract void turn(DarwinCell darwinCell, int degrees, int stepSize);
}