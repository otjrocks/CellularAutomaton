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
 * @author Owen Jennixfngs
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

  private Map<String, Double> setDefaultParameters () {
    Map<String, Double> parameters = new HashMap<>();

    parameters.put("fishReproductionTime", 0.3);
    parameters.put("sharkReproductionTime", 0.3);
    parameters.put("sharkEnergyGain", 0.3);

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

    public int getValue() {
      return value;
    }

    public static State fromValue(int value) {
      for (State state : values()) {
        if (state.value == value) {
          return state;
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
    State currentState = State.fromValue(cell.getState());

    switch (currentState) {
      case FISH -> {
        return shouldFishMove(cell, grid) ? State.MOVE_PENDING.getValue() : State.FISH.getValue();
      }
      case SHARK -> {
        return getSharkNextState(cell, grid);
      }
      case EMPTY -> {
        return State.EMPTY.getValue();
      }
      default -> {
        return cell.getState();
      }
    }
  }

  private int getSharkNextState(Cell cell, Grid grid) {
    if (isSharkDead(cell)) {
      return State.EMPTY.getValue();
    }
    return shouldSharkMove(cell, grid) ? State.MOVE_PENDING.getValue() : State.SHARK.getValue();
  }

  private boolean shouldFishMove(Cell cell, Grid grid) {
    return !getNeighborsByState(cell, grid, State.EMPTY.getValue()).isEmpty();
  }

  private boolean isSharkDead(Cell cell) {
    return ((WaTorCell) cell).getHealth() <= 0;
  }

  private boolean shouldSharkMove(Cell cell, Grid grid) {
    WaTorCell sharkCell = (WaTorCell) cell;
    if (sharkCell.getHealth() <= 0) {
      return false; // Shark is dead
    }
    return !getNeighborsByState(cell, grid, State.FISH.getValue()).isEmpty() ||
        !getNeighborsByState(cell, grid, State.EMPTY.getValue()).isEmpty();
  }

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

  private void processSharkMovement(Cell cell, Grid grid, List<CellStateUpdate> nextStates, Set<Point2D> updatedCells, List<Cell> fishCells) {
    WaTorCell shark = (WaTorCell) cell;

    shark.decreaseHealth();
    System.out.println("Shark at " + shark.getLocation() + " has health: " + shark.getHealth());

    if (shark.getHealth() <= 0) {
      System.out.println("Shark at " + shark.getLocation() + " DIED.");
      nextStates.add(new CellStateUpdate(shark.getLocation(), State.EMPTY.getValue()));
      updatedCells.add(shark.getLocation());
      return;
    }

    boolean shouldReproduce = shark.getReproductionEnergy() >= parameters.get("sharkReproductionTime");
    List<Cell> fishNeighbors = getNeighborsByState(shark, grid, State.FISH.getValue());

    if (!fishNeighbors.isEmpty()) {
      Cell fishCell = fishNeighbors.get(random.nextInt(fishNeighbors.size()));
      if (!updatedCells.contains(fishCell.getLocation())) {
        fishCells.remove(fishCell);
        checkReproductionAndMoveOutSharkCell(nextStates, shark, shouldReproduce, fishCell, updatedCells);
        shark.addHealth(parameters.get("sharkEnergyGain").intValue());
        return;
      }
    }

    List<Cell> emptyNeighbors = getNeighborsByState(shark, grid, State.EMPTY.getValue());
    if (!emptyNeighbors.isEmpty()) {
      Cell newLocation = emptyNeighbors.get(random.nextInt(emptyNeighbors.size()));
      if (!updatedCells.contains(newLocation.getLocation())) {
        checkReproductionAndMoveOutSharkCell(nextStates, shark, shouldReproduce, newLocation, updatedCells);
      }
    }
  }

  private void checkReproductionAndMoveOutSharkCell(List<CellStateUpdate> nextStates, WaTorCell shark,
      boolean shouldReproduce, Cell newLocation, Set<Point2D> updatedCells) {

    if (!updatedCells.contains(newLocation.getLocation())) {
      nextStates.add(new CellStateUpdate(newLocation.getLocation(), State.SHARK.getValue())); //moves in
      updatedCells.add(newLocation.getLocation());
    }

    if (shouldReproduce) {
      System.out.println("Shark at " + shark.getLocation() + " has reproduced: ");
      nextStates.add(new CellStateUpdate(shark.getLocation(), State.SHARK.getValue())); // moves out
      shark.resetReproductionEnergy();
      updatedCells.add(shark.getLocation());
    } else if (!newLocation.getLocation().equals(shark.getLocation())) {
      nextStates.add(new CellStateUpdate(shark.getLocation(), State.EMPTY.getValue())); // moves out
      updatedCells.add(shark.getLocation());
    }
  }

  private void processFishMovement(Cell cell, Grid grid, List<CellStateUpdate> nextStates, Set<Point2D> updatedCells) {
    WaTorCell fish = (WaTorCell) cell;

    List<Cell> emptyNeighbors = getNeighborsByState(fish, grid, State.EMPTY.getValue());

    if (!emptyNeighbors.isEmpty()) {
      Cell newLocation = emptyNeighbors.get(random.nextInt(emptyNeighbors.size()));

      boolean shouldReproduce = fish.getReproductionEnergy() >= parameters.get("fishReproductionTime");

      if (shouldReproduce) {
        fish.resetReproductionEnergy();
        nextStates.add(new CellStateUpdate(fish.getLocation(), State.FISH.getValue())); // Offspring
        updatedCells.add(fish.getLocation());
      } else if (!updatedCells.contains(fish.getLocation())) {
        nextStates.add(new CellStateUpdate(fish.getLocation(), State.EMPTY.getValue())); // Move out
        updatedCells.add(fish.getLocation());
      }

      if (!updatedCells.contains(newLocation.getLocation())) {
        nextStates.add(new CellStateUpdate(newLocation.getLocation(), State.FISH.getValue())); // Move in
        updatedCells.add(newLocation.getLocation());
      }
    }
  }

  /*
  private void processMovement(Cell cell, Grid grid, List<CellStateUpdate> nextStates) {
    State currentState = State.fromValue(cell.getState());

    List<Cell> targetCells;
    if (currentState == State.SHARK) {
      targetCells = getNeighborsByState(cell, grid, State.FISH.getValue());
      if (targetCells.isEmpty()) {
        targetCells = getNeighborsByState(cell, grid, State.EMPTY.getValue());
      }
    } else {
      targetCells = getNeighborsByState(cell, grid, State.EMPTY.getValue());
    }

    if (!targetCells.isEmpty()) {
      Cell target = targetCells.get(random.nextInt(targetCells.size()));
      moveEntity(cell, target, nextStates);
    } else {
      nextStates.add(new CellStateUpdate(cell.getLocation(), currentState.getValue()));
    }
  }

  private void moveEntity(Cell fromCell, Cell toCell, List<CellStateUpdate> nextStates) {
    State entityState = State.fromValue(fromCell.getState());
    WaTorCell movingCell = (WaTorCell) fromCell;

    if (entityState == State.SHARK) {
      if (toCell.getState() == State.FISH.getValue()) {
        movingCell.addHealth(parameters.get("sharkEnergyGain").intValue());
        System.out.println("Shark at " + fromCell.getLocation() + " ate fish at " + toCell.getLocation() + " and gained energy.");
      }
      else {
        movingCell.decreaseHealth();
      }
    }

    boolean shouldReproduce = movingCell.getReproductionEnergy() >= parameters.get(getReproductionParam(entityState));
    nextStates.add(new CellStateUpdate(toCell.getLocation(), entityState.getValue()));

    if (shouldReproduce)  {
      movingCell.resetReproductionEnergy();
      nextStates.add(new CellStateUpdate(movingCell.getLocation(), entityState.getValue())); // Reproduce
    } else {
      nextStates.add(new CellStateUpdate(movingCell.getLocation(), State.EMPTY.getValue())); // Move out
    }
  }
*/
  private String getReproductionParam(State state) {
    return (state == State.FISH) ? "fishReproductionTime" : "sharkReproductionTime";
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