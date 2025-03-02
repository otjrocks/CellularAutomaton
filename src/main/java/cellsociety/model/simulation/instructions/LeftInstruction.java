package cellsociety.model.simulation.instructions;

import cellsociety.model.cell.DarwinCell;

/**
 * Subclass to handle the turning of a cell left
 */
public class LeftInstruction extends TurnInstruction {

  /**
   * The method that calls the cell method to turn the cell
   *
   * @param darwinCell - the cell being executed on
   * @param degrees - the degrees being shifted left
   * @param stepSize - the number of directions used to determine neighbors
   */
  @Override
  protected void turn(DarwinCell darwinCell, int degrees, int stepSize) {
    darwinCell.turnLeft(degrees, stepSize);
  }
}