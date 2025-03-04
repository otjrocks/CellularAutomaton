package cellsociety.model.simulation.instructions;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.cell.DarwinCell;
import cellsociety.model.cell.DarwinCellRecord;
import cellsociety.model.simulation.Instruction;
import cellsociety.model.simulation.rules.DarwinRules.State;

/**
 * Move Instruction class to handle movement of a cell in Darwin Simulation
 */

public class MoveInstruction implements Instruction {

  /**
   * Move the cell
   *
   * @param darwinCell - the cell that the instruction is executed on
   * @param arguments  - the list of arguments needed - i.e instructions, numMovements
   * @param grid       - the collection of cell objects
   */
  @Override
  public List<CellUpdate> executeInstruction(DarwinCell darwinCell, List<String> arguments,
      Grid grid) {
    Point2D direction = darwinCell.getFrontDirection();
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

      newRow = verifyInBounds(newRow, grid.getRows());
      newCol = verifyInBounds(newCol, grid.getCols());

      Cell curCell;
      try {
        curCell = grid.getCell(newRow, newCol);
      } catch (IndexOutOfBoundsException e) {
        break;
      }
      if (curCell == null || curCell.getState() != State.EMPTY.getValue()) {
        break;
      }

      curLocation = new Point2D.Double(newRow, newCol);
    }

    return updateGridForMovement(darwinCell, grid, curLocation, newRow, newCol);
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

  private static List<CellUpdate> updateGridForMovement(DarwinCell darwinCell, Grid grid,
      Point2D curLocation, int newRow, int newCol) {
    List<CellUpdate> updates = new ArrayList<>();
    if (!curLocation.equals(darwinCell.getLocation())) {
      Cell newEmpty = new DarwinCell(State.EMPTY.getValue(), darwinCell.getLocation());
      Cell newCell = new DarwinCell(
          new DarwinCellRecord(darwinCell.getState(), new Double(newRow, newCol),
              darwinCell.getOrientation(), darwinCell.getInfectionCountdown(),
              darwinCell.getCurInstructionIndex() + 1,
              darwinCell.getAllInstructions(), darwinCell.getInfected(),
              darwinCell.getPrevState()));

      updates.add(new CellUpdate(darwinCell.getLocation(), newEmpty));
      updates.add(new CellUpdate(new Double(newRow, newCol), newCell));
    }
    return updates;
  }


  private int verifyInBounds(int rowOrCol, int numRowsOrCols) {
    if (rowOrCol > numRowsOrCols - 1) {
      rowOrCol = numRowsOrCols - 1;
    }

    if (rowOrCol < 0) {
      rowOrCol = 0;
    }

    return rowOrCol;
  }

}

