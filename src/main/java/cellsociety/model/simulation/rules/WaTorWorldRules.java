package cellsociety.model.simulation.rules;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.CellStateUpdate;
import cellsociety.model.cell.WaTorCell;
import cellsociety.model.simulation.SimulationRules;
import java.util.*;

/**
 * The rules implementation for simulation WaTor World. Handles fish movement, shark movement,
 * reproduction, and energy loss.
 *
 * @author Owen Jennings
 */
public class WaTorWorldRules extends SimulationRules {

  private final Random random = new Random();

  public WaTorWorldRules(Map<String, Double> parameters) {
    super.parameters = parameters;
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
    List<Cell> movingCells = new ArrayList<>();

    // First pass: Determine next state or mark cells as MOVE_PENDING
    Iterator<Cell> cellIterator = grid.getCellIterator();
    while (cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      int nextState = getNextState(cell, grid);

      if (nextState == State.MOVE_PENDING.getValue()) {
        movingCells.add(cell);
      } else {
        if (nextState != State.EMPTY.getValue() || cell.getState() != State.EMPTY.getValue()) {
          // do not add cell update if cell was empty before and is still empty, in case someone wants to move there
          nextStates.add(new CellStateUpdate(cell.getLocation(), nextState));
        }
      }
    }

    // Second pass: Process movements
    for (Cell cell : movingCells) {
      processMovement(cell, grid, nextStates);
    }

    return nextStates;
  }

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
    }
  }

  private void moveEntity(Cell fromCell, Cell toCell, List<CellStateUpdate> nextStates) {
    State entityState = State.fromValue(fromCell.getState());
    WaTorCell movingCell = (WaTorCell) fromCell;

    if (entityState == State.SHARK) {
      if (toCell.getState() == State.FISH.getValue()) {
        movingCell.addHealth(parameters.get("sharkEnergyGain").intValue());
      }
    }

    if (movingCell.getReproductionEnergy() >= parameters.get(getReproductionParam(entityState))) {
      movingCell.resetReproductionEnergy();
      nextStates.add(
          new CellStateUpdate(fromCell.getLocation(), entityState.getValue())); // Reproduce
    } else {
      nextStates.add(
          new CellStateUpdate(fromCell.getLocation(), State.EMPTY.getValue())); // Move out
    }

    nextStates.add(new CellStateUpdate(toCell.getLocation(), entityState.getValue())); // Move in
  }

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
