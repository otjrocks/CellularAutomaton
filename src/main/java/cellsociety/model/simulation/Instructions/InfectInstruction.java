package cellsociety.model.simulation.Instructions;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.DarwinCell;
import cellsociety.model.simulation.Instruction;
import cellsociety.model.simulation.rules.DarwinRules.State;
import java.awt.geom.Point2D;
import java.util.List;

public class InfectInstruction implements Instruction {
  private int layers;
  /**
   * @param darwinCell - the cell that the instruction is executed on
   * @param arguments  - the list of instructions for the given cell
   * @param grid       - the collection of cell objects
   */
  @Override
  public void executeInstruction(DarwinCell darwinCell, List<String> arguments, Grid grid) {
    Point2D direction =  darwinCell.getFrontDirection();
    int newInfectionCountdown = Integer.parseInt(arguments.get(1));
    int newRow = darwinCell.getRow();
    int newCol = darwinCell.getCol();

    for (int i = 0; i < layers; i++) {
      newRow += (int) direction.getX();
      newCol += (int) direction.getY();

      Cell curCell = grid.getCell(newRow, newCol);
      if (curCell == null || curCell.getState() == State.EMPTY.getValue() || curCell.getState() == darwinCell.getState()) {
        continue;
      }

      DarwinCell speciesCell = (DarwinCell) curCell;

      Cell infectedCell = new DarwinCell(darwinCell.getState(), speciesCell.getLocation(), speciesCell.getOrientation(), newInfectionCountdown, speciesCell.getAllInstructions());
      grid.updateCell(infectedCell);
    }
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
    this.layers = layers;
  }
}
