package cellsociety.model.simulation.rules;

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
import cellsociety.model.cell.CellStateUpdate;
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

  public WaTorWorldRules(Map<String, Double> parameters) {
    if (parameters == null || parameters.isEmpty()) {
      this.parameters = setDefaultParameters();
    } else {
      super.parameters = parameters;
    }
  }

  private Map<String, Double> setDefaultParameters() {
    Map<String, Double> parameters = new HashMap<>();

    parameters.put("fishReproductionTime", 3.0);
    parameters.put("sharkReproductionTime", 4.0);
    parameters.put("sharkEnergyGain", 2.0);

    return parameters;
  }

  // I asked ChatGPT for help with implementing this enum
  public enum State {
    MOVE_PENDING(-1),
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
  public List<CellStateUpdate> getNextStatesForAllCells(Grid grid) {
    List<CellStateUpdate> nextStates = new ArrayList<>();
    Set<Point2D> updatedCells = new HashSet<>();

    List<Cell> sharkCells = new ArrayList<>();
    List<Cell> fishCells = new ArrayList<>();

    // First pass: Determine next state or mark cells as MOVE_PENDING
    Iterator<Cell> cellIterator = grid.getCellIterator();
    while (cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      if (cell.getState() == State.SHARK.getValue()) {
        sharkCells.add(cell);
      } else if (cell.getState() == State.FISH.getValue()) {
        fishCells.add(cell);
      } else {
        nextStates.add(new CellStateUpdate(cell.getLocation(), cell.getState()));
      }
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

  private void processSharkMovement(Cell cell, Grid grid, List<CellStateUpdate> nextStates,
      Set<Point2D> updatedCells, List<Cell> fishCells) {
    WaTorCell shark = (WaTorCell) cell;

    shark.decreaseHealth();

    if (shark.getHealth() <= 0) {
      nextStates.add(new CellStateUpdate(shark.getLocation(), State.EMPTY.getValue()));
      updatedCells.add(shark.getLocation());
      return;
    }

    boolean shouldReproduce =
        shark.getReproductionEnergy() >= parameters.get("sharkReproductionTime");
    List<Cell> fishNeighbors = getNeighborsByState(shark, grid, State.FISH.getValue());

    if (!fishNeighbors.isEmpty()) {
      Cell fishCell = fishNeighbors.get(random.nextInt(fishNeighbors.size()));
      if (!updatedCells.contains(fishCell.getLocation())) {
        fishCells.remove(fishCell);
        checkReproductionAndMoveOutSharkCell(nextStates, shark, shouldReproduce, fishCell,
            updatedCells, grid);
        shark.addHealth(parameters.get("sharkEnergyGain").intValue());
        return;
      }
    }

    List<Cell> emptyNeighbors = getNeighborsByState(shark, grid, State.EMPTY.getValue());
    if (!emptyNeighbors.isEmpty()) {
      Cell newLocation = emptyNeighbors.get(random.nextInt(emptyNeighbors.size()));
      if (!updatedCells.contains(newLocation.getLocation())) {
        checkReproductionAndMoveOutSharkCell(nextStates, shark, shouldReproduce, newLocation,
            updatedCells, grid);
      }
    }
  }

  private void checkReproductionAndMoveOutSharkCell(List<CellStateUpdate> nextStates,
      WaTorCell shark,
      boolean shouldReproduce, Cell newLocation, Set<Point2D> updatedCells, Grid grid) {

    if (!updatedCells.contains(newLocation.getLocation())) {
      nextStates.add(
          new CellStateUpdate(newLocation.getLocation(), State.SHARK.getValue())); //moves in
      setCellHealth(grid, newLocation.getLocation(),
          shark.getHealth());  // ensure shark retains its health when it moves
      updatedCells.add(newLocation.getLocation());
    }

    if (shouldReproduce) {
      resetReproductionEnergy(grid,
          newLocation.getLocation()); // ensure old shark that has moved has reproduction energy reset
      nextStates.add(new CellStateUpdate(shark.getLocation(),
          State.SHARK.getValue())); // create new shark at original location
      resetCellHealth(grid, shark.getLocation()); // ensure new shark has correct initial health
      updatedCells.add(shark.getLocation());

    } else if (!newLocation.getLocation().equals(shark.getLocation())) {
      nextStates.add(new CellStateUpdate(shark.getLocation(), State.EMPTY.getValue())); // moves out
      updatedCells.add(shark.getLocation());
    }
  }

  private void processFishMovement(Cell cell, Grid grid, List<CellStateUpdate> nextStates,
      Set<Point2D> updatedCells) {
    WaTorCell fish = (WaTorCell) cell;

    List<Cell> emptyNeighbors = getNeighborsByState(fish, grid, State.EMPTY.getValue());

    if (!emptyNeighbors.isEmpty()) {
      Cell newLocation = emptyNeighbors.get(random.nextInt(emptyNeighbors.size()));

      boolean shouldReproduce =
          fish.getReproductionEnergy() >= parameters.get("fishReproductionTime");

      if (!updatedCells.contains(newLocation.getLocation())) {
        nextStates.add(
            new CellStateUpdate(newLocation.getLocation(), State.FISH.getValue())); // Move in
        updatedCells.add(newLocation.getLocation());
      }

      if (shouldReproduce) {
        resetReproductionEnergy(grid,
            newLocation.getLocation()); // update reproduction energy of old fish, which is in a new location
        nextStates.add(new CellStateUpdate(fish.getLocation(), State.FISH.getValue())); // Offspring
        updatedCells.add(fish.getLocation());
      } else if (!updatedCells.contains(fish.getLocation())) {
        nextStates.add(new CellStateUpdate(fish.getLocation(), State.EMPTY.getValue())); // Move out
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

  // reset reproduction energy at location
  private void resetReproductionEnergy(Grid grid, Point2D location) {
    WaTorCell cell = (WaTorCell) grid.getCell(location);
    cell.resetReproductionEnergy();
  }

  // set health of cell at location
  private void setCellHealth(Grid grid, Point2D location, int health) {
    WaTorCell cell = (WaTorCell) grid.getCell(location);
    cell.setHealth(health);
  }

  // reset health of cell at location
  private void resetCellHealth(Grid grid, Point2D location) {
    WaTorCell cell = (WaTorCell) grid.getCell(location);
    cell.resetHealth();
  }
}