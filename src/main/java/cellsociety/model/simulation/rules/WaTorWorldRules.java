package cellsociety.model.simulation.rules;

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

/**
 * The rules implementation for simulation WaTor World. Handles fish movement, shark movement,
 * reproduction, and energy loss.
 *
 * @author Owen Jennings
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
      case SHARK -> {
        return getSharkNextState(cell, grid);
      }
      case FISH -> {
        return State.FISH.getValue();
      }
      case EMPTY -> {
        return State.EMPTY.getValue();
      }
      default -> {
        return cell.getState();
      }
    }
  }

  // Checks if there are valid moves for shark and it's alive
  private int getSharkNextState(Cell cell, Grid grid) {
    if (isSharkDead(cell)) {
      return State.EMPTY.getValue();
    }
    return shouldSharkMove(cell, grid) ? State.MOVE_PENDING.getValue() : State.SHARK.getValue();
  }

  // Checks if shark is out of health
  private boolean isSharkDead(Cell cell) {
    return ((WaTorCell) cell).getHealth() <= 0;
  }

  // Checks if shark has any valid moves
  private boolean shouldSharkMove(Cell cell, Grid grid) {
    return !getNeighborsByState(cell, grid, State.FISH.getValue()).isEmpty() ||
        !getNeighborsByState(cell, grid, State.EMPTY.getValue()).isEmpty();
  }

  // I asked ChatGPT for assistance with implementing the state transitions in this method
  @Override
  public List<CellStateUpdate> getNextStatesForAllCells(Grid grid) {
    List<CellStateUpdate> nextStates = new ArrayList<>();
    List<Cell> fishCells = new ArrayList<>();
    List<Cell> movingCells = new ArrayList<>();
    HashSet<String> occupiedCells = new HashSet<>();

    // First pass: Determine next state or mark cells as MOVE_PENDING
    Iterator<Cell> cellIterator = grid.getCellIterator();
    while (cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      State currentState = State.fromValue(cell.getState());

      if(currentState == State.SHARK){
        int nextState = getNextState(cell, grid);
        if (nextState == State.MOVE_PENDING.getValue()) {
          movingCells.add(cell);
        } else {
          nextStates.add(new CellStateUpdate(cell.getLocation(), nextState));
          if (nextState == State.SHARK.getValue()) {
                    occupiedCells.add(cell.getLocation().toString());
                }
        }
      }
      else if(currentState == State.FISH){
        fishCells.add(cell);
      }
      else if(currentState != State.EMPTY || cell.getState() != State.EMPTY.getValue()) {
        nextStates.add(new CellStateUpdate(cell.getLocation(), currentState.getValue()));
      }
    }

    // Second pass: Process movements
    for (Cell cell : movingCells) {
      processMovement(cell, grid, nextStates, occupiedCells);
    }

    for (Cell cell : fishCells) {
      processMovement(cell, grid, nextStates, occupiedCells);
    }

    return nextStates;
  }

  private void processMovement(Cell cell, Grid grid, List<CellStateUpdate> nextStates, HashSet<String> occupiedCells) {
    State currentState = State.fromValue(cell.getState());
    WaTorCell movingCell = (WaTorCell) cell;

    List<Cell> targetCells;
    if (currentState == State.SHARK) {
      targetCells = getNeighborsByState(cell, grid, State.FISH.getValue());
      if (!targetCells.isEmpty()) {
        Cell target = targetCells.get(random.nextInt(targetCells.size()));


        movingCell.addHealth(parameters.get("sharkEnergyGain").intValue());

        nextStates.add(new CellStateUpdate(target.getLocation(), State.SHARK.getValue()));
        nextStates.add(new CellStateUpdate(cell.getLocation(), determineReproductionOrEmpty(movingCell, State.SHARK)));

        occupiedCells.add(target.getLocation().toString());
        return;
      }
      targetCells = getValidEmptyNeighbors(cell, grid, occupiedCells);

    } else {
      targetCells = getValidEmptyNeighbors(cell, grid, occupiedCells);
    }

    if (!targetCells.isEmpty()) {
      Cell target = targetCells.get(random.nextInt(targetCells.size()));

      nextStates.add(new CellStateUpdate(target.getLocation(), currentState.getValue()));
      nextStates.add(new CellStateUpdate(cell.getLocation(), determineReproductionOrEmpty(movingCell, currentState)));

      occupiedCells.add(target.getLocation().toString());
    }

    if (currentState == State.SHARK) {
        movingCell.addHealth(-1);
        if (movingCell.getHealth() <= 0) {
            nextStates.add(new CellStateUpdate(cell.getLocation(), State.EMPTY.getValue())); // Shark dies
        }
    }
  }

  private int determineReproductionOrEmpty(WaTorCell cell, State state) {
    if (cell.getReproductionEnergy() >= parameters.get(getReproductionParam(state)).intValue()) {
        cell.resetReproductionEnergy();
        return state.getValue();
    }
    return State.EMPTY.getValue();
  }

  private String getReproductionParam(State state) {
    return (state == State.FISH) ? "fishReproductionTime" : "sharkReproductionTime";
  }

  private List<Cell> getValidEmptyNeighbors(Cell cell, Grid grid, HashSet<String> occupiedCells) {
    List<Cell> emptyNeighbors = getNeighborsByState(cell, grid, State.EMPTY.getValue());
    emptyNeighbors.removeIf(neighbor -> occupiedCells.contains(neighbor.getLocation().toString()));
    return emptyNeighbors;
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
