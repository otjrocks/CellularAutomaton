package cellsociety.model.simulation.Instructions;

import cellsociety.model.cell.DarwinCell;

public class RightInstruction extends TurnInstruction {
  @Override
  protected void turn(DarwinCell darwinCell, int degrees, int stepSize) {
    darwinCell.turnRight(degrees, stepSize);
  }
}