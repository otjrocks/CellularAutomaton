package cellsociety.model.simulation.rules;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.DarwinCell;
import cellsociety.model.simulation.GetNeighbors;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.SimulationRules;
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
  public int getNextState(Cell cell, Grid grid) {
    DarwinCell darwinCell = (DarwinCell) cell;
    String curInstruction = darwinCell.getInstruction();

    int state = handleInstruction(darwinCell, curInstruction, grid);
  }

  private int handleInstruction(DarwinCell darwinCell, String instruction, Grid grid) {
    String[] splitInstructions = instruction.split(" ");
    String command = splitInstructions[0];

    switch (command) {
      case "MOVE":
        return handleMove(darwinCell, grid);
      case "LEFT":
        darwinCell.turnLeft(Integer.parseInt(splitInstructions[1]));
        break;
      case "RIGHT":
        darwinCell.turnRight(Integer.parseInt(splitInstructions[1]));
        break;
      case "INFECT":
        return handleInfection(darwinCell, grid, Integer.parseInt(splitInstructions[1]));
      case "IFEMPTY":
        return handleConditional(darwinCell, grid, splitInstructions[1], grid.isEmpty(darwinCell.getFrontLocation()));
      case "IFENEMY":
        return handleConditional(darwinCell, grid, splitInstructions[1], grid.isEnemy(darwinCell.getFrontLocation(), darwinCell));
      case "GO":
        return handleGo(darwinCell, Integer.parseInt(splitInstructions[1]));
      default:
        System.out.println("Unknown instruction: " + instruction);
    }
    return darwinCell.getState();
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
