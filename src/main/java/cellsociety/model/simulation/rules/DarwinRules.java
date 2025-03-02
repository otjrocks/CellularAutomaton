package cellsociety.model.simulation.rules;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.cell.DarwinCell;
import cellsociety.model.simulation.GetNeighbors;
import cellsociety.model.simulation.Instruction;
import cellsociety.model.simulation.instructions.ConditionalInstruction;
import cellsociety.model.simulation.instructions.GoInstruction;
import cellsociety.model.simulation.instructions.InfectInstruction;
import cellsociety.model.simulation.instructions.LeftInstruction;
import cellsociety.model.simulation.instructions.MoveInstruction;
import cellsociety.model.simulation.instructions.RightInstruction;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.SimulationRules;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Rules class for the Darwin Simulation
 * @Author Justin Aronwald
 */
public class DarwinRules  extends SimulationRules {
  public static final Logger LOGGER = LogManager.getLogger();
  private Map<String, Instruction> instructionHandlers;

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
    initializeInstructionHandler();
  }

  private void initializeInstructionHandler() {
    instructionHandlers = new HashMap<>();
    instructionHandlers.put("MOVE", new MoveInstruction());
    instructionHandlers.put("MV", new MoveInstruction());
    instructionHandlers.put("LEFT", new LeftInstruction());
    instructionHandlers.put("LT", new LeftInstruction());
    instructionHandlers.put("RIGHT", new RightInstruction());
    instructionHandlers.put("RT", new RightInstruction());
    instructionHandlers.put("INFECT", new InfectInstruction(getLayers()));
    instructionHandlers.put("INF", new InfectInstruction(getLayers()));
    String IFEMPTY = "IFEMPTY";
    instructionHandlers.put(IFEMPTY, new ConditionalInstruction(IFEMPTY, getLayers()));
    instructionHandlers.put("EMP?", new ConditionalInstruction(IFEMPTY, getLayers()));
    String IFWALL = "IFWALL";
    instructionHandlers.put("IFWALL", new ConditionalInstruction(IFWALL, getLayers()));
    instructionHandlers.put("WL?", new ConditionalInstruction(IFWALL, getLayers()));
    String IFSAME = "IFSAME";
    instructionHandlers.put(IFSAME, new ConditionalInstruction(IFSAME, getLayers()));
    instructionHandlers.put("SM?", new ConditionalInstruction(IFSAME, getLayers()));
    String IFENEMY = "IFENEMY";
    instructionHandlers.put(IFENEMY, new ConditionalInstruction(IFENEMY, getLayers()));
    instructionHandlers.put("EMY?", new ConditionalInstruction(IFENEMY, getLayers()));
    String IFRANDOM = "IFRANDOM";
    instructionHandlers.put(IFRANDOM, new ConditionalInstruction(IFRANDOM, getLayers()));
    instructionHandlers.put("RND?", new ConditionalInstruction(IFRANDOM, getLayers()));
    instructionHandlers.put("GO", new GoInstruction());
  }

  @Override
  public List<CellUpdate> getNextStatesForAllCells(Grid grid) {
    List<CellUpdate> updates = new ArrayList<>();

    Iterator<Cell> cellIterator = grid.getCellIterator();
    while (cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      DarwinCell darwinCell = (DarwinCell) cell;

      if (darwinCell.getState() == State.INFECTED.getValue()) {
        darwinCell.handleInfectionDecrease();

        if (darwinCell.getInfectionCountdown() <= 0) {
          DarwinCell previousSpecies = new DarwinCell(darwinCell.getPrevState(), darwinCell.getLocation(), darwinCell.getOrientation(), 0, darwinCell.getAllInstructions());
          grid.updateCell(previousSpecies);
        }
      }


      List<String> arguments = new ArrayList<>(Arrays.asList(darwinCell.getInstruction().split(" ")));

      if (arguments.isEmpty()) {
        LOGGER.warn("Empty instruction for cell at {}", darwinCell.getLocation());
        continue;
      }

      Instruction instruction = instructionHandlers.get(arguments.getFirst());
      if (instruction != null) {
        List<CellUpdate> instructionUpdates = instruction.executeInstruction(darwinCell, arguments, grid);
        updates.addAll(instructionUpdates);
      } else {
        LOGGER.warn("No instruction handler found for {}", arguments.getFirst());
      }
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

  /**
   * @return - the number of potential states for any given cell
   */
  @Override
  public int getNumberStates() {
    return State.values().length;
  }
}
