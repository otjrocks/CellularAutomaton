package cellsociety.model.simulation.rules;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.cell.ForagingAntsCell;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.SimulationRules;

/**
 * A class representing the rules for Foraging Ants
 */
public class ForagingAntsRules extends SimulationRules {

  private final Random random = new Random();
  private final int myPheromoneDecayRate;
  private final int myAntReproductionTime;
  private final int myMaxPheromoneAmount;
  private final int myNewAntsNum;

  private int myReproductionTimer = 0;
  private final List<Cell> nestCells = new ArrayList<>();
  private final List<Cell> nestCellNeighbors = new ArrayList<>();

  /**
   * A default constructor for foraging ants
   *
   * @param parameters The required parameters. This class requires pheromoneDecayRate,
   *                   antReproductionTime, and maxPheromoneAmount
   * @throws InvalidParameterException An exception thrown if parameters are not provided or are
   *                                   incorrectly formatted.
   */
  public ForagingAntsRules(Map<String, Parameter<?>> parameters) throws InvalidParameterException {
    super(parameters);
    if (parameters == null || parameters.isEmpty()) {
      setParameters(setDefaultParameters());
    }
    checkMissingParameterAndThrowException("pheromoneDecayRate");
    checkMissingParameterAndThrowException("antReproductionTime");
    checkMissingParameterAndThrowException("maxPheromoneAmount");
    checkMissingParameterAndThrowException("newAntsNum");
    myPheromoneDecayRate = getParameters().get("pheromoneDecayRate").getInteger();
    myAntReproductionTime = getParameters().get("antReproductionTime").getInteger();
    myMaxPheromoneAmount = getParameters().get("maxPheromoneAmount").getInteger();
    myNewAntsNum = getParameters().get("newAntsNum").getInteger();
    validateParameterRange();
  }

  /**
   * Get a list of all required parameters for a simulation
   *
   * @return A list of strings representing the required parameter keys for this simulation
   */
  public static List<String> getRequiredParameters() {
    return List.of("pheromoneDecayRate", "maxPheromoneAmount", "antReproductionTime", "newAntsNum");
  }

  private void validateParameterRange() throws InvalidParameterException {
    if (myPheromoneDecayRate < 0) {
      throwInvalidParameterException("pheromoneDecayRate");
    }
    if (myAntReproductionTime < 0) {
      throwInvalidParameterException("antReproductionTime");
    }
    if (myMaxPheromoneAmount < 0) {
      throwInvalidParameterException("maxPheromoneAmount");
    }
    if (myNewAntsNum < 0) {
      throwInvalidParameterException("maxPheromoneAmount");
    }
  }

  private Map<String, Parameter<?>> setDefaultParameters() {
    Map<String, Parameter<?>> parameters = new HashMap<>();

    parameters.put("pheromoneDecayRate", new Parameter<>(3));
    parameters.put("antReproductionTime", new Parameter<>(4));
    parameters.put("maxPheromoneAmount", new Parameter<>(1));

    return parameters;
  }

  public enum State {
    EMPTY(0),
    ANT(1),
    FOOD(2),
    NEST(3);

    private final int value;

    State(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }

    public static void fromValue(int value) {
      for (State state : values()) {
        if (state.value == value) {
          return;
        }
      }
      throw new IllegalArgumentException("Invalid AntState value: " + value);
    }
  }

  private List<Cell> getNeighbors(Cell cell, Grid grid) {
    return super.getNeighbors(cell, grid, false);
  }

  @Override
  public int getNumberStates() {
    return 4;
  }

  @Override
  public List<CellUpdate> getNextStatesForAllCells(Grid grid) {
    
    List<CellUpdate> nextStates = new ArrayList<>();
    List<Cell> antCells = new ArrayList<>();
    Set<Point2D> occupiedCells = new HashSet<>();
    myReproductionTimer++;

    Iterator<Cell> cellIterator = grid.getCellIterator();
    while (cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      if (cell.getState() == State.ANT.getValue()) {
        antCells.add(cell);
      }
      if (cell.getState() == State.NEST.getValue()) {
        nestCells.add(cell);
      }
    }

    for (Cell ant : antCells) {
      processAntMovement(ant, grid, nextStates, occupiedCells);
    }

    checkIfTimeForMoreAnts(grid, nextStates, occupiedCells);

    return nextStates;
  }

  private void processAntMovement(Cell cell, Grid grid, List<CellUpdate> nextStates,
      Set<Point2D> occupiedCells) {
    ForagingAntsCell ant = (ForagingAntsCell) cell;
    ant.reduceHealth(1);
    if(ant.getHealth() <= 0){
        addEmptyCell(ant, nextStates);
    }
    else{
        List<Cell> emptyNeighbors = getNeighborsByState(ant, grid, State.EMPTY.getValue(),
            occupiedCells);
        List<Cell> foodNeighbors = getNeighborsByState(ant, grid, State.FOOD.getValue(), occupiedCells);
        List<Cell> nestNeighbors = getNeighborsByState(ant, grid, State.NEST.getValue(), occupiedCells);

        if (!ant.getHasFood()) {
        if (!foodNeighbors.isEmpty() && !emptyNeighbors.isEmpty()) {
            ForagingAntsCell nextLocation = (ForagingAntsCell) highestPheromoneNeighbor(emptyNeighbors,
                "home");
            addUpdatedAnt(ant, nextLocation, grid, nextStates, true);
            occupiedCells.add(nextLocation.getLocation());
            if (!nextLocation.getLocation().equals(ant.getLocation())) {
                addEmptyCell(ant, nextStates);
            }
        } else if (!emptyNeighbors.isEmpty()) {
            ForagingAntsCell nextLocation = (ForagingAntsCell) highestPheromoneNeighbor(emptyNeighbors,
                "food");
            addUpdatedAnt(ant, nextLocation, grid, nextStates, ant.getHasFood());
            occupiedCells.add(nextLocation.getLocation());
            if (!nextLocation.getLocation().equals(ant.getLocation())) {
                addEmptyCell(ant, nextStates);
            }
        }
        } else {
        if (!nestNeighbors.isEmpty() && !emptyNeighbors.isEmpty()) {
            ForagingAntsCell nextLocation = (ForagingAntsCell) highestPheromoneNeighbor(emptyNeighbors,
                "food");
            addUpdatedAnt(ant, nextLocation, grid, nextStates, false);
            occupiedCells.add(nextLocation.getLocation());
            if (!nextLocation.getLocation().equals(ant.getLocation())) {
                addEmptyCell(ant, nextStates);
            }
        } else if (!emptyNeighbors.isEmpty()) {
            ForagingAntsCell nextLocation = (ForagingAntsCell) highestPheromoneNeighbor(emptyNeighbors,
                "home");
            addUpdatedAnt(ant, nextLocation, grid, nextStates, ant.getHasFood());
            occupiedCells.add(nextLocation.getLocation());
            if (!nextLocation.getLocation().equals(ant.getLocation())) {
                addEmptyCell(ant, nextStates);
            }
        }

        }
      }
  }

  private double updatePheromone(ForagingAntsCell antCell, Grid grid, String type) {

    List<Cell> neighbors = getNeighbors(antCell, grid);
    double maxNeighborPheromone = 0;
    int state = (type.equals("home")) ? State.NEST.value : State.FOOD.value;
    for (Cell neighbor : neighbors) {
      if (neighbor.getState() == state) {
        return myMaxPheromoneAmount;
      }
      ForagingAntsCell antNeighbor = (ForagingAntsCell) neighbor;
      double currentNeighborPheremoneValue = (type.equals("home")) ? antNeighbor.getHomePheromone() : antNeighbor.getFoodPheromone();

      maxNeighborPheromone = Math.max(maxNeighborPheromone, currentNeighborPheremoneValue);
    }

    if (maxNeighborPheromone == 0) {
      return 0;
    }

    double desiredPheromone = maxNeighborPheromone - 2;
    double currentPheromoneValue = (type.equals("home")) ? antCell.getHomePheromone() : antCell.getFoodPheromone();
    double pheromoneDifference = desiredPheromone - currentPheromoneValue;

    return currentPheromoneValue + pheromoneDifference;
  }

  private List<Cell> getNeighborsByState(Cell cell, Grid grid, int state,
      Set<Point2D> occupiedCells) {
    List<Cell> allNeighbors = getNeighbors(cell, grid);
    List<Cell> neighborsByState = new ArrayList<>();

    for (Cell neighbor : allNeighbors) {
      if (neighbor.getState() == state && !occupiedCells.contains(neighbor.getLocation())) {
        neighborsByState.add(neighbor);
      }
    }
    return neighborsByState;
  }

  private Cell highestPheromoneNeighbor(List<Cell> emptyNeighbors, String type) {
    if (emptyNeighbors.isEmpty()) {
      return null;
    }
    List<Cell> candidates = new ArrayList<>();
    double maxPheromone = 0;

    for (Cell neighbor : emptyNeighbors) {
      ForagingAntsCell antNeighbor = (ForagingAntsCell) neighbor;
      double pheromoneLevel =
          (type.equals("home")) ? antNeighbor.getHomePheromone() : antNeighbor.getFoodPheromone();

      if (pheromoneLevel > maxPheromone) {
        maxPheromone = pheromoneLevel;
        candidates.clear();
        candidates.add(antNeighbor);
      } else if (pheromoneLevel == maxPheromone) {
        candidates.add(antNeighbor);
      }
    }

    if (random.nextDouble() < 0.1) {
      return emptyNeighbors.get(random.nextInt(emptyNeighbors.size()));
    }

    return candidates.get(random.nextInt(candidates.size()));
  }

  private void addNewAnt(ForagingAntsCell newAnt, List<CellUpdate> nextStates){
    nextStates.add(new CellUpdate(newAnt.getLocation(),
            new ForagingAntsCell(State.ANT.getValue(), newAnt.getLocation(),
            newAnt.getHomePheromone(), newAnt.getFoodPheromone(), 300, false)));
  }

  private void addUpdatedAnt(ForagingAntsCell ant, ForagingAntsCell nextLocation, Grid grid, List<CellUpdate> nextStates, boolean hasFood){
    nextStates.add((new CellUpdate(nextLocation.getLocation(),
                new ForagingAntsCell(State.ANT.getValue(), nextLocation.getLocation(),
                    updatePheromone(nextLocation, grid, "home") * (1 - myPheromoneDecayRate),
                    updatePheromone(nextLocation, grid, "food") * (1 - myPheromoneDecayRate),
                    ant.getHealth(), hasFood))));
  }

  private void addEmptyCell(ForagingAntsCell ant, List<CellUpdate> nextStates){
    nextStates.add(new CellUpdate(ant.getLocation(), 
                new ForagingAntsCell(State.EMPTY.getValue(), ant.getLocation(),
                ant.getHomePheromone() * (1 - myPheromoneDecayRate),
                ant.getFoodPheromone() * (1 - myPheromoneDecayRate), 0, false)));
  }

  private void checkIfTimeForMoreAnts(Grid grid, List<CellUpdate> nextStates, Set<Point2D> occupiedCells){
    if (myReproductionTimer >= myAntReproductionTime) {
      for (Cell nestCell : nestCells) {
        for (Cell neighbor : getNeighborsByState(nestCell, grid, State.EMPTY.getValue(),
            occupiedCells)) {
          nestCellNeighbors.add(neighbor);
        }
      }

      for(int i=0; i < myNewAntsNum; i++){
        Cell newCell = nestCellNeighbors.get(random.nextInt(nestCellNeighbors.size()));
        nestCellNeighbors.remove(newCell);
        ForagingAntsCell newAnt = (ForagingAntsCell)newCell;
        addNewAnt(newAnt, nextStates);
      }
      
      myReproductionTimer = 0;
    }
  }
}
