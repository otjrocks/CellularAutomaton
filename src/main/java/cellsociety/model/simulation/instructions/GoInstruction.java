package cellsociety.model.simulation.instructions;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cellsociety.model.Grid;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.cell.DarwinCell;
import cellsociety.model.simulation.Instruction;

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
  public List<CellUpdate> executeInstruction(DarwinCell darwinCell, List<String> arguments, Grid grid, Map<Point2D, DarwinCell> occupiedCells, Set<Point2D> movingCells) {
    int instructionIndex = Integer.parseInt(arguments.get(1));
    darwinCell.setCurInstructionIndex(instructionIndex-2);

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
