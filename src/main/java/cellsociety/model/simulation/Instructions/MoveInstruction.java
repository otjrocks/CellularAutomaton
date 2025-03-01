package cellsociety.model.simulation.Instructions;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.cell.DarwinCell;
import cellsociety.model.simulation.Instruction;
import cellsociety.model.simulation.rules.DarwinRules.State;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.List;

public class MoveInstruction implements Instruction {

  /**
   * Move the cell
   *
   * @param darwinCell - the cell that the instruction is executed on
   * @param arguments - the list of arguments needed - i.e instructions, numMovements
   * @param grid - the collection of cell objects
   */
  @Override
  public void executeInstruction(DarwinCell darwinCell, List<String> arguments, Grid grid) {
      Point2D direction =  darwinCell.getFrontDirection();
      Point2D curLocation = darwinCell.getLocation();
      int numMovements;
      try {
        numMovements = Integer.parseInt(arguments.get(1));
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Invalid movement number");
      }

      int newRow = darwinCell.getRow();
      int newCol = darwinCell.getCol();

      for (int i = 0; i < numMovements; i++) {
        newRow += (int) direction.getX();
        newCol += (int) direction.getY();

        Cell curCell = grid.getCell(newRow, newCol);
        if (curCell == null || curCell.getState() != State.EMPTY.getValue()) {
          break;
        }

        curLocation = new Point2D.Double(newRow, newCol);
      }

      updateGridForMovement(darwinCell, grid, curLocation, newRow, newCol);
    }

  /**
   * @param stepSize - the number of directions to look towards for each configuration
   */
  @Override
  public void setStepSize(int stepSize) {
    //unneeded here
  }

  /**
   * @param layers - the number of cells to look forward
   */
  @Override
  public void setLayers(int layers) {
    //unneeded here
  }

  private static void updateGridForMovement(DarwinCell darwinCell, Grid grid,
      Point2D curLocation, int newRow, int newCol) {
    if (!curLocation.equals(darwinCell.getLocation())) {
      Cell newEmpty = new DarwinCell(State.EMPTY.getValue(), darwinCell.getLocation());
      Cell newCell = new DarwinCell(darwinCell.getState(), new Double(newRow, newCol),
          darwinCell.getOrientation(), darwinCell.getInfectionCountdown(), darwinCell.getAllInstructions());

      grid.updateCell(newEmpty);
      grid.updateCell(newCell);
    }
  }

}
