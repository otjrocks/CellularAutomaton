package cellsociety.model.simulation.rules;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cellsociety.config.SimulationConfig;
import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.cell.DarwinCell;
import cellsociety.model.cell.DarwinCellRecord;
import cellsociety.model.simulation.neighbors.GetNeighbors;
import cellsociety.model.simulation.Instruction;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.instructions.ConditionalInstruction;
import cellsociety.model.simulation.instructions.GoInstruction;
import cellsociety.model.simulation.instructions.InfectInstruction;
import cellsociety.model.simulation.instructions.LeftInstruction;
import cellsociety.model.simulation.instructions.MoveInstruction;
import cellsociety.model.simulation.instructions.RightInstruction;

/**
 * Rules class for the Darwin Simulation.
 *
 * @author Justin Aronwald
 */
public class DarwinRules extends SimulationRules {

  public static final Logger LOGGER = LogManager.getLogger();
  private Map<String, Instruction> instructionHandlers;

  /**
   * The default constructor of a simulation rules class.
   *
   * @param parameters   The parameters map for the simulation rule. Each simulation rules
   *                     implementation should validate and ensure that parameters are present and
   *                     correct in the constructor. If no parameters are required for a given rules
   *                     class, then this can be null
   * @param getNeighbors The neighbor configuration for the simulation
   * @throws InvalidParameterException This exception should be thrown whenever a simulation rules
   *                                   instance is created with invalid or missing parameter
   *                                   values.
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
    Map<Point2D, DarwinCell> occupiedCells = new HashMap<>();
    Set<Point2D> movingCells = new HashSet<>();

    Iterator<Cell> cellIterator = grid.getCellIterator();
    while (cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      DarwinCell darwinCell = (DarwinCell) cell;

      handleInfection(grid, darwinCell);
      List<String> arguments = new ArrayList<>(
          Arrays.asList(darwinCell.getInstruction().split(" ")));

      if (arguments.isEmpty()) {
        LOGGER.warn("Empty instruction for cell at {}", darwinCell.getLocation());
        continue;
      }

      Instruction instruction = instructionHandlers.get(arguments.getFirst());
      if (instruction != null) {
        List<CellUpdate> instructionUpdates = instruction.executeInstruction(darwinCell, arguments, grid, occupiedCells, movingCells);
        darwinCell.nextInstruction();
        updates.addAll(instructionUpdates);
      } else if (!(darwinCell.getState() == State.EMPTY.getValue())) {
        LOGGER.warn("No instruction handler found for {}", arguments.getFirst());
      }
    }

    return updates;
  }

  private static void handleInfection(Grid grid, DarwinCell darwinCell) {
    if (darwinCell.getInfected()) {
      darwinCell.handleInfectionDecrease();

      if (darwinCell.getInfectionCountdown() <= 0) {
        DarwinCell previousSpecies = new DarwinCell(
            new DarwinCellRecord(darwinCell.getPrevState(), darwinCell.getLocation(),
                darwinCell.getOrientation(), 0, 0,
                SimulationConfig.assignInstructionsFromState(darwinCell.getPrevState()), false, 0));
        grid.updateCell(previousSpecies);
      }
    }
  }

  /**
   * An enum to store the possible states for this simulation.
   */
  public enum State {
    EMPTY, HOPPER, FLYTRAP, CREEPER, LANDMINE, OODFAY, ROVER, DANCER, SNAKE, RUNNER;

    /**
     * Get the ordinal value of this enum entry.
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
