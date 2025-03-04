package cellsociety.model.simulation;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;

import cellsociety.model.Grid;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.cell.DarwinCell;

/**
 * Interface defined to create an Instruction instance.
 * Handles all of the different Instructions needed for a Darwin Simulation
 */
public interface Instruction {

  /**
   * Executes the individual instruction
   *
   * @param darwinCell - the cell that the instruction is executed on
   * @param arguments - the list of instructions for the given cell
   * @param grid - the collection of cell objects
   */
  List<CellUpdate> executeInstruction(DarwinCell darwinCell, List<String> arguments, Grid grid, Map<Point2D, DarwinCell> occupiedCells);

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
