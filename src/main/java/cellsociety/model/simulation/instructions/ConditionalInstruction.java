package cellsociety.model.simulation.instructions;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.cell.DarwinCell;
import cellsociety.model.simulation.Instruction;
import cellsociety.model.simulation.rules.DarwinRules.State;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Conditional Instruction class to handle the various conditionals in Darwin Simulation
 */
public class ConditionalInstruction implements Instruction {
  private int layers;
  private final String conditionType;
  private final Random random = new Random();

  /**
   * Creates one instance of a Conditional Instruction
   *
   * @param conditionType - the string name of the condition
   * @param layers - the number of layers that should be searched when getting neighbors
   */
  public ConditionalInstruction(String conditionType, int layers) {
    this.conditionType = conditionType;
    this.layers = layers;
  }

  /**
   * @param darwinCell - the cell that the instruction is executed on
   * @param arguments  - the list of instructions for the given cell
   * @param grid       - the collection of cell objects
   */
  @Override
  public List<CellUpdate> executeInstruction(DarwinCell darwinCell, List<String> arguments, Grid grid) {
    Point2D direction = darwinCell.getFrontDirection();
    int newRow = darwinCell.getRow();
    int newCol = darwinCell.getCol();
    int nextInstruction = Integer.parseInt(arguments.get(1));

    List<CellUpdate> updates = new ArrayList<>();

    List<CellUpdate> updates1 = handleRandomCase(darwinCell,
        nextInstruction, updates);
    if (updates1 != null) {
      return updates1;
    }

    checkAllConditions(darwinCell, grid, newRow, direction, newCol, nextInstruction, updates);
    return updates;
  }

  private void checkAllConditions(DarwinCell darwinCell, Grid grid, int newRow, Point2D direction,
      int newCol, int nextInstruction, List<CellUpdate> updates) {
    for (int i = 0; i < layers; i++) {
      newRow += (int) direction.getX();
      newCol += (int) direction.getY();

      Cell curCell = grid.getCell(newRow, newCol);
      if (curCell == null) {
        continue;
      }

      if (checkIfConditionIsMet(darwinCell, curCell, grid)) {
        darwinCell.setCurInstructionIndex(nextInstruction);
        updates.add(new CellUpdate(darwinCell.getLocation(), darwinCell));
      }
    }
  }

  private List<CellUpdate> handleRandomCase(DarwinCell darwinCell, int nextInstruction,
      List<CellUpdate> updates) {
    if ( conditionType.equals("IFRANDOM") || conditionType.equals("RND?")) {
      if (random.nextBoolean()) {
        darwinCell.setCurInstructionIndex(nextInstruction);
        updates.add(new CellUpdate(darwinCell.getLocation(), darwinCell));
      }
      return updates;
    }
    return null;
  }

  private boolean checkIfConditionIsMet(DarwinCell darwinCell, Cell curCell, Grid grid) {
    return ConditionType.valueOf(conditionType).check(darwinCell, curCell, grid);
  }

  //ChatGPT took my existing logic and made it more simple due to the code being too Complex on the pipeline checker

  /**
   * Enum that handles the various types of conditional statements
   */
  private enum ConditionType {
    IFEMPTY((darwinCell, curCell, grid) -> curCell.getState() == State.EMPTY.getValue()),
    IFWALL((darwinCell, curCell, grid) -> grid.isWall(curCell.getRow(), curCell.getCol())),
    IFSAME((darwinCell, curCell, grid) -> curCell.getState() == darwinCell.getState()),
    IFENEMY((darwinCell, curCell, grid) -> curCell.getState() != State.EMPTY.getValue()
        && curCell.getState() != darwinCell.getState());

    private final ConditionChecker conditionChecker;

    /**
     * Creates an instance of the enum for the current type of the condition
     *
     * @param checker - instance of the functional interface that checks the condition
     */
    ConditionType(ConditionChecker checker) {
      this.conditionChecker = checker;
    }

    /**
     * Calls the checker to validate the condition
     *
     * @param darwinCell - the cell that the instruction is executed on
     * @param curCell - the current cell that is being compared to the darwinCell
     * @param grid - the collection of cell objects
     * @return - a boolean on whether the condition passed
     */
    public boolean check(DarwinCell darwinCell, Cell curCell, Grid grid) {
      return conditionChecker.check(darwinCell, curCell, grid);
    }
  }

  /**
   * A functional interface that checks and validates the condition
   */
  @FunctionalInterface
  private interface ConditionChecker {

    /**
     * Method to check whether or not the condition is validated
     *
     * @param darwinCell - the cell that the instruction is executed on
     * @param curCell - the current cell that is being compared to the darwinCell
     * @param grid - the collection of cell objects
     * @return - a boolean on whether the condition passed
     */
    boolean check(DarwinCell darwinCell, Cell curCell, Grid grid);
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
    this.layers = layers;
  }
}
