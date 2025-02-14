package cellsociety.model.simulation.rules;

import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.Parameter.InvalidParameterType;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.cell.WaTorCell;
import cellsociety.model.simulation.SimulationRules;
import java.util.Set;

/**
 * The rules implementation for simulation WaTor World. Handles fish movement, shark movement,
 * reproduction, and energy loss.
 *
 * @author Owen Jennings
 * @author Justin Aronwald
 */
public class WaTorWorldRules extends SimulationRules {

  private final Random random = new Random();
  private final int mySharkEnergyGain;
  private final int mySharkReproductionTime;
  private final int myFishReproductionTime;

  public WaTorWorldRules(Map<String, Parameter<?>> parameters) throws InvalidParameterType {
    super(parameters);
    if (parameters == null || parameters.isEmpty()) {
      setParameters(setDefaultParameters());
    }
    checkMissingParameterAndThrowException("sharkEnergyGain");
    checkMissingParameterAndThrowException("sharkReproductionTime");
    checkMissingParameterAndThrowException("fishReproductionTime");
    mySharkEnergyGain = getParameters().get("sharkEnergyGain").getInteger();
    mySharkReproductionTime = getParameters().get("sharkReproductionTime").getInteger();
    myFishReproductionTime = getParameters().get("fishReproductionTime").getInteger();
  }

  private Map<String, Parameter<?>> setDefaultParameters() {
    Map<String, Parameter<?>> parameters = new HashMap<>();

    parameters.put("fishReproductionTime", new Parameter<>(3));
    parameters.put("sharkReproductionTime", new Parameter<>(4));
    parameters.put("sharkEnergyGain", new Parameter<>(1));

    return parameters;
  }

  // I asked ChatGPT for help with implementing this enum
  public enum State {
    EMPTY(0),
    FISH(1),
    SHARK(2);

    private final int value;

    State(int value) {
      this.value = value;
    }

    /**
     * @return the value of a state
     */
    public int getValue() {
      return value;
    }

    /**
     * @param value - the value of a state
     */
    public static void fromValue(int value) {
      for (State state : values()) {
        if (state.value == value) {
          return;
        }
      }
      throw new IllegalArgumentException("Invalid WaTorState value: " + value);
    }
  }

  /**
   * Retrieves the adjacent non-diagonal neighbors.
   */
  public List<Cell> getNeighbors(Cell cell, Grid grid) {
    return super.getNeighbors(cell, grid, false);
  }

  @Override
  public int getNextState(Cell cell, Grid grid) {
    return -1; // Not used
  }

  @Override
  public int getNumberStates() {
    return 3;
  }


  /**
   * @param grid - the grid object containing the cell objects
   * @return - A list containing the updates that will happen to cells
   */
  // I asked ChatGPT for assistance with implementing the state transitions in this method
  @Override
  public List<CellUpdate> getNextStatesForAllCells(Grid grid) {
    List<CellUpdate> nextStates = new ArrayList<>();
    Set<Point2D> updatedCells = new HashSet<>();

    List<Cell> sharkCells = new ArrayList<>();
    List<Cell> fishCells = new ArrayList<>();

    Iterator<Cell> cellIterator = grid.getCellIterator();
    while (cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      if (cell.getState() == State.SHARK.getValue()) {
        sharkCells.add(cell);
      } else if (cell.getState() == State.FISH.getValue()) {
        fishCells.add(cell);
      }
      // if a cell is neither a shark nor fish, then it must be empty, and we do not need to update
    }

    //process sharks first
    for (Cell shark : sharkCells) {
      processSharkMovement(shark, grid, nextStates, updatedCells, fishCells);
    }

    for (Cell fish : fishCells) {
      processFishMovement(fish, grid, nextStates, updatedCells);
    }

    return nextStates;
  }

  private void processSharkMovement(Cell cell, Grid grid, List<CellUpdate> nextStates,
      Set<Point2D> updatedCells, List<Cell> fishCells) {
    WaTorCell shark = (WaTorCell) cell;
    int health = shark.getHealth() - 1; // decrease health by 1

    if (health <= 0) {
      Cell newEmpty = new WaTorCell(State.EMPTY.getValue(), shark.getLocation());
      nextStates.add(new CellUpdate(shark.getLocation(), newEmpty));
      updatedCells.add(shark.getLocation());
      return;
    }

    boolean shouldReproduce =
        shark.getReproductionEnergy() >= mySharkReproductionTime;
    List<Cell> fishNeighbors = getNeighborsByState(shark, grid, State.FISH.getValue());

    if (!fishNeighbors.isEmpty()) {
      Cell fishCell = fishNeighbors.get(random.nextInt(fishNeighbors.size()));
      if (!updatedCells.contains(fishCell.getLocation())) {
        fishCells.remove(fishCell);
        health += mySharkEnergyGain;
        checkReproductionAndMoveOutSharkCell(nextStates, shark, health, shouldReproduce, fishCell,
            updatedCells, grid);
        return;
      }
    }

    List<Cell> emptyNeighbors = getNeighborsByState(shark, grid, State.EMPTY.getValue());
    if (!emptyNeighbors.isEmpty()) {
      Cell newLocation = emptyNeighbors.get(random.nextInt(emptyNeighbors.size()));
      if (!updatedCells.contains(newLocation.getLocation())) {
        checkReproductionAndMoveOutSharkCell(nextStates, shark, health, shouldReproduce,
            newLocation,
            updatedCells, grid);
      }
    }
  }

  private void checkReproductionAndMoveOutSharkCell(List<CellUpdate> nextStates,
      WaTorCell shark, int health,
      boolean shouldReproduce, Cell newLocation, Set<Point2D> updatedCells, Grid grid) {
    if (!updatedCells.contains(newLocation.getLocation())) {
      // update grid with a shark in a new location that retains the original shark's health and
      // reproductive energy + 1 when it moves
      Cell sharkNewPosition = new WaTorCell(State.SHARK.getValue(), newLocation.getLocation(),
          health, shark.getReproductionEnergy() + 1);
      nextStates.add(new CellUpdate(newLocation.getLocation(), sharkNewPosition));
      updatedCells.add(newLocation.getLocation());
    }

    if (shouldReproduce) {
      // create new shark/offspring at original location of shark
      Cell newShark = new WaTorCell(State.SHARK.getValue(), shark.getLocation(),
          WaTorCell.DEFAULT_HEALTH, 0);
      nextStates.add(new CellUpdate(newShark.getLocation(), newShark));
      // reset reproductive energy of parent
      Cell parentResetReproductive = new WaTorCell(State.SHARK.getValue(),
          newLocation.getLocation(), health, 0);
      nextStates.add(new CellUpdate(newLocation.getLocation(), parentResetReproductive));
      updatedCells.add(shark.getLocation());

    } else if (!newLocation.getLocation().equals(shark.getLocation())) {
      Cell newEmpty = new WaTorCell(State.EMPTY.getValue(), shark.getLocation());
      nextStates.add(new CellUpdate(shark.getLocation(), newEmpty)); // moves out
      updatedCells.add(shark.getLocation());
    }
  }

  private void processFishMovement(Cell cell, Grid grid, List<CellUpdate> nextStates,
      Set<Point2D> updatedCells) {
    WaTorCell fish = (WaTorCell) cell;
    List<Cell> emptyNeighbors = getNeighborsByState(fish, grid, State.EMPTY.getValue());

    if (!emptyNeighbors.isEmpty()) {
      Cell newLocation = emptyNeighbors.get(random.nextInt(emptyNeighbors.size()));

      boolean shouldReproduce =
          fish.getReproductionEnergy() >= myFishReproductionTime;

      if (!updatedCells.contains(newLocation.getLocation())) {
        // set new cell to have original cells reproductive energy + 1 and new location
        Cell newFishLocation = new WaTorCell(State.FISH.getValue(), newLocation.getLocation(),
            WaTorCell.DEFAULT_HEALTH, fish.getReproductionEnergy() + 1);
        nextStates.add(
            new CellUpdate(newLocation.getLocation(), newFishLocation)); // Move in
        updatedCells.add(newLocation.getLocation());
      }

      if (shouldReproduce) {
        Cell offspringFish = new WaTorCell(State.FISH.getValue(), newLocation.getLocation(), fish.getHealth(), 0);
        nextStates.add(new CellUpdate(fish.getLocation(), offspringFish)); // Offspring
        Cell parentFishResetReproductive = new WaTorCell(State.FISH.getValue(), newLocation.getLocation(), fish.getHealth(), 0);
        nextStates.add(new CellUpdate(newLocation.getLocation(), parentFishResetReproductive));
        updatedCells.add(fish.getLocation());
      } else if (!updatedCells.contains(fish.getLocation())) {
        Cell newEmpty = new WaTorCell(State.EMPTY.getValue(), fish.getLocation());
        nextStates.add(new CellUpdate(fish.getLocation(), newEmpty)); // Move out
        updatedCells.add(fish.getLocation());
      }

    }
  }

  private List<Cell> getNeighborsByState(Cell cell, Grid grid, int state) {
    List<Cell> allNeighbors = getNeighbors(cell, grid);
    List<Cell> neighborsByState = new ArrayList<>();

    for (Cell neighbor : allNeighbors) {
      if (neighbor.getState() == state) {
        neighborsByState.add(neighbor);
      }
    }
    return neighborsByState;
  }
}