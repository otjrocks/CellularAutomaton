package cellsociety.model.simulation.instructions;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
   * @param arguments  - the list of arguments needed - i.e. instructions, numMovements
   * @param grid       - the collection of cell objects
   */
  @Override
  public List<CellUpdate> executeInstruction(DarwinCell darwinCell, List<String> arguments, Grid grid, Map<Point2D, DarwinCell> occupiedCells, Set<Point2D> movingCells) {
      Point2D direction =  darwinCell.getFrontDirection();
      Point2D curLocation = darwinCell.getLocation();
      int numMovements;
      try {
        numMovements = Integer.parseInt(arguments.get(1));
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Invalid movement number", e);
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

        if (shouldBreakTraversal(curCell, occupiedCells)) {
          break;
        }

        curLocation = new Point2D.Double(newRow, newCol);
      }

      return updateGridForMovement(darwinCell, curLocation, occupiedCells, movingCells);
    }

  private boolean shouldBreakTraversal(Cell curCell, Map<Point2D, DarwinCell> occupiedCells) {
    return curCell == null || curCell.getState() != State.EMPTY.getValue() || occupiedCells.keySet().contains(curCell.getLocation());
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

  private static List<CellUpdate> updateGridForMovement(DarwinCell darwinCell,
      Point2D curLocation, Map<Point2D, DarwinCell> occupiedCells, Set<Point2D> movingCells) {
    List<CellUpdate> updates = new ArrayList<>();
    if (!curLocation.equals(darwinCell.getLocation())) {
      Cell newEmpty = new DarwinCell(State.EMPTY.getValue(), darwinCell.getLocation());
      Cell newCell = new DarwinCell(new DarwinCellRecord(darwinCell.getState(), curLocation,
          darwinCell.getOrientation(), darwinCell.getInfectionCountdown(), darwinCell.getCurInstructionIndex() + 1, 
          darwinCell.getAllInstructions(), darwinCell.getInfected(), darwinCell.getPrevState()));

      occupiedCells.put(newCell.getLocation(), (DarwinCell)newCell);
      movingCells.add(newEmpty.getLocation());


      updates.add(new CellUpdate(darwinCell.getLocation(),  newEmpty));
      updates.add(new CellUpdate(curLocation, newCell));
    }
    return updates;
  }


  private int verifyInBounds(int rowOrCol, int numRowsOrCols) {
    if (valueGreaterThanBound(rowOrCol, numRowsOrCols)) {
      rowOrCol = numRowsOrCols - 1;
    }

    if (rowOrCol < 0) {
      rowOrCol = 0;
    }

    return rowOrCol;
  }

  private static boolean valueGreaterThanBound(int rowOrCol, int numRowsOrCols) {
    return rowOrCol > numRowsOrCols - 1;
  }

}

