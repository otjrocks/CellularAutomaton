package cellsociety.model.simulation.rules;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.cell.DarwinCell;
import cellsociety.model.simulation.GetNeighbors;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.SimulationRules;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DarwinRules  extends SimulationRules {
  /**
   * The default constructor of a simulation rules class
   *
   * @param parameters   The parameters map for the simulation rule. Each simulation rules
   *                     implementation should validate and ensure that parameters are present and
   *                     correct in the constructor. If no parameters are required for a given rules
   *                     class, then this can be null
   * @param getNeighbors The neighbor configuration for the simulation
   * @throws InvalidParameterException This exception should be thrown whenever a simulation rules
   *                                   instance is created with invalid or missing parameter values.
   */
  public DarwinRules(Map<String, Parameter<?>> parameters,
      GetNeighbors getNeighbors) throws InvalidParameterException {
    super(parameters, getNeighbors);
  }

  @Override
  public List<CellUpdate> getNextStatesForAllCells(Grid grid) {
    List<CellUpdate> updates = new ArrayList<>();

    Iterator<Cell> cellIterator = grid.getCellIterator();
    while (cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      DarwinCell darwinCell = (DarwinCell) cell;
      String curInstruction = darwinCell.getInstruction();
      handleInstruction(darwinCell, curInstruction, grid, updates);
    }

    return updates;
  }

  /**
   * An enum to store the possible states for this simulation
   */
  public enum State {
    EMPTY, INFECTED, HOPPER, FLYTRAP, CREEPER;

    /**
     * Get the ordinal value of this enum entry
     *
     * @return An int representing the state
     */
    public int getValue() {
      return ordinal();
    }
  }

  private void handleInstruction(DarwinCell darwinCell, String instruction, Grid grid, List<CellUpdate> updates) {
    String[] splitInstructions = instruction.split(" ");
    String command = splitInstructions[0];

    switch (command) {
      case "MOVE":
        handleMove(darwinCell, grid, Integer.parseInt(splitInstructions[1]), updates);
      case "LEFT":
        darwinCell.turnLeft(Integer.parseInt(splitInstructions[1]), getStepSize());
        break;
      case "RIGHT":
        darwinCell.turnRight(Integer.parseInt(splitInstructions[1]), getStepSize());
        break;
      case "INFECT":
        handleInfection(darwinCell, grid, Integer.parseInt(splitInstructions[1]), updates);
      case "IFEMPTY":
        handleConditional(darwinCell, grid, splitInstructions[1], grid.isEmpty(darwinCell.getFrontLocation()), updates);
      case "IFENEMY":
        handleConditional(darwinCell, grid, splitInstructions[1], grid.isEnemy(darwinCell.getFrontLocation(), darwinCell), updates);
      case "GO":
        handleGo(darwinCell, Integer.parseInt(splitInstructions[1]), updates);
      default:
        System.out.println("Unknown instruction: " + instruction);
    }
  }

  private void handleMove(DarwinCell darwinCell, Grid grid, int numMovements, List<CellUpdate> updates) {
    Point2D direction =  darwinCell.getFrontDirection();
    Point2D curLocation = darwinCell.getLocation();
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

    if (!curLocation.equals(darwinCell.getLocation())) {
      Cell newEmpty = new DarwinCell(State.EMPTY.getValue(), darwinCell.getLocation());
      Cell newCell = new DarwinCell(darwinCell.getState(), new Double(newRow, newCol),
          darwinCell.getOrientation(), darwinCell.getInfectionCountdown(), darwinCell.getAllInstructions());

      updates.add(new CellUpdate(darwinCell.getLocation(),  newEmpty));
      updates.add(new CellUpdate(new Point2D.Double(newRow, newCol), newCell));

    }
  }

  private int handleGo(DarwinCell darwinCell, int instructionIndex) {
    darwinCell.setCurInstructionIndex(instructionIndex);
    return darwinCell.getState();
  }

  private int handleConditional(DarwinCell darwinCell, Grid grid, String instruction, boolean condition) {
    if (condition) {
      return handleGo(darwinCell, Integer.parseInt(instruction));
    }
    return darwinCell.getState();
  }



  /**
   * @return
   */
  @Override
  public int getNumberStates() {
    return 0;
  }
}
