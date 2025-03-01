package cellsociety.model.simulation.Instructions;

import cellsociety.model.cell.DarwinCell;

public class LeftInstruction extends TurnInstruction {
  @Override
  protected void turn(DarwinCell darwinCell, int degrees, int stepSize) {
    darwinCell.turnLeft(degrees, stepSize);
  }
}