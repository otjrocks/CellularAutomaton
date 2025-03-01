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
import cellsociety.model.simulation.GetNeighbors;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.SimulationRules;

/**
 * A class representing the rules for Foraging Ants
 */
public class ForagingAntsRules extends SimulationRules {

  private final Random random = new Random();
  private final double myPheromoneDecayRate;
  private final int myAntReproductionTime;
  private final double myMaxPheromoneAmount;
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
  public ForagingAntsRules(Map<String, Parameter<?>> parameters, GetNeighbors myGetNeighbors)
      throws InvalidParameterException {
    super(parameters, myGetNeighbors);
    if (parameters == null || parameters.isEmpty()) {
      setParameters(setDefaultParameters());
    }
    checkMissingParameterAndThrowException("pheromoneDecayRate");
    checkMissingParameterAndThrowException("antReproductionTime");
    checkMissingParameterAndThrowException("maxPheromoneAmount");
    checkMissingParameterAndThrowException("newAntsNum");
    myPheromoneDecayRate = getParameters().get("pheromoneDecayRate").getDouble();
    myAntReproductionTime = getParameters().get("antReproductionTime").getInteger();
    myMaxPheromoneAmount = getParameters().get("maxPheromoneAmount").getDouble();
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
      throwInvalidParameterException("newAntsNum");
    }
  }

  /**
   * Set default parameters for simulation if no parameters are passed in
   *
   * @return A map of parameters with Strings as keys and doubles as values
   */
  private Map<String, Parameter<?>> setDefaultParameters() {
    Map<String, Parameter<?>> parameters = new HashMap<>();

    parameters.put("pheromoneDecayRate", new Parameter<>(0.2));
    parameters.put("antReproductionTime", new Parameter<>(10));
    parameters.put("maxPheromoneAmount", new Parameter<>(3));

    return parameters;
  }

  /**
   * Enum that holds State values and associated strings
   */
  public enum State {
    EMPTY(0),
    ANT(1),
    FOOD(2),
    NEST(3);

    private final int value;

    State(int value) {
      this.value = value;
    }

    /**
     * Get int representing this state
     *
     * @return int representing this state
     */
    public int getValue() {
      return value;
    }
  }

  /**
   * Retrieves number of different states involved in the simulation
   */
  @Override
  public int getNumberStates() {
    return 4;
  }

  /**
   * Method that determines which cells need to be updated and adds them to a List of CellUpdate
   * objects that will be carried out by the Grid
   *
   * @param grid: grid on which the cells are/will be located
   */
  @Override
  public List<CellUpdate> getNextStatesForAllCells(Grid grid) {

    List<CellUpdate> nextStates = new ArrayList<>();
    List<Cell> antCells = new ArrayList<>();
    List<ForagingAntsCell> emptyCells = new ArrayList<>();
    Set<Point2D> occupiedCells = new HashSet<>();
    myReproductionTimer++;

    Iterator<Cell> cellIterator = grid.getCellIterator();
    while (cellIterator.hasNext()) {
      ForagingAntsCell cell = (ForagingAntsCell) cellIterator.next();
      if (cell.getState() == State.ANT.getValue()) {
        antCells.add(cell);
      }
      if (cell.getState() == State.NEST.getValue()) {
        nestCells.add(cell);
      }
      if (cell.getState() == State.EMPTY.getValue()) {
        emptyCells.add(cell);
      }
    }

    for (Cell ant : antCells) {
      processAntMovement(ant, grid, nextStates, occupiedCells);
    }

    checkIfTimeForMoreAnts(grid, nextStates, occupiedCells);

    for (ForagingAntsCell empty : emptyCells) {
      if (!occupiedCells.contains(empty.getLocation())) {
        addEmptyCell(empty, nextStates);
      }
    }

    return nextStates;
  }

  /**
   * Method that carries out the proper of update of ant position for all ants in the simulation.
   *
   * @param cell:          current ant being updated
   * @param grid:          grid on which the ants are/will be located
   * @param nextStates:    list of CellUpdates that will hold ant's next cell representation
   * @param occupiedCells: cells that already have an ant moving to them this cycle
   */
  private void processAntMovement(Cell cell, Grid grid, List<CellUpdate> nextStates,
      Set<Point2D> occupiedCells) {
    ForagingAntsCell ant = (ForagingAntsCell) cell;
    ant.reduceHealth(1);
    if (ant.getHealth() <= 0) {
      addEmptyCell(ant, nextStates);
    } else {

      List<Cell> emptyNeighbors = getNeighborsByState(ant, grid, State.EMPTY.getValue(),
          occupiedCells);
      List<Cell> foodNeighbors = getNeighborsByState(ant, grid, State.FOOD.getValue(),
          occupiedCells);
      List<Cell> nestNeighbors = getNeighborsByState(ant, grid, State.NEST.getValue(),
          occupiedCells);

      if (!ant.getHasFood()) {
        if (!foodNeighbors.isEmpty() && !emptyNeighbors.isEmpty()) {

          ForagingAntsCell nextLocation = (ForagingAntsCell) highestPheromoneNeighbor(
              emptyNeighbors, "home");
          addUpdatedAnt(ant, nextLocation, grid, nextStates, true);
          occupiedCells.add(nextLocation.getLocation());

          if (!nextLocation.getLocation().equals(ant.getLocation())) {
            addEmptyCell(ant, nextStates);
          }

        } else if (!emptyNeighbors.isEmpty()) {

          ForagingAntsCell nextLocation = (ForagingAntsCell) highestPheromoneNeighbor(
              emptyNeighbors, "food");
          addUpdatedAnt(ant, nextLocation, grid, nextStates, ant.getHasFood());
          occupiedCells.add(nextLocation.getLocation());

          if (!nextLocation.getLocation().equals(ant.getLocation())) {
            addEmptyCell(ant, nextStates);
          }
        }

      } else {
        if (!nestNeighbors.isEmpty() && !emptyNeighbors.isEmpty()) {

          ForagingAntsCell nextLocation = (ForagingAntsCell) highestPheromoneNeighbor(
              emptyNeighbors, "food");
          addUpdatedAnt(ant, nextLocation, grid, nextStates, false);
          occupiedCells.add(nextLocation.getLocation());

          if (!nextLocation.getLocation().equals(ant.getLocation())) {
            addEmptyCell(ant, nextStates);
          }

        } else if (!emptyNeighbors.isEmpty()) {

          ForagingAntsCell nextLocation = (ForagingAntsCell) highestPheromoneNeighbor(
              emptyNeighbors, "home");
          addUpdatedAnt(ant, nextLocation, grid, nextStates, ant.getHasFood());
          occupiedCells.add(nextLocation.getLocation());

          if (!nextLocation.getLocation().equals(ant.getLocation())) {
            addEmptyCell(ant, nextStates);
          }
        }
      }
    }
  }

  /**
   * Method that updates the pheremones for the cell the ant is moving to
   *
   * @param antCell: the cell the ant is moving to and thus needs have its pheremones updated
   * @param grid:    grid on which the cell is located
   * @param type:    String differentiating between home and food pheromones
   */
  private double updatePheromone(ForagingAntsCell antCell, Grid grid, String type) {

    List<Cell> neighbors = getNeighbors(antCell, grid);
    double maxNeighborPheromone = 0;
    int state = (type.equals("home")) ? State.NEST.value : State.FOOD.value;
    for (Cell neighbor : neighbors) {
      if (neighbor.getState() == state) {
        return myMaxPheromoneAmount;
      }
      ForagingAntsCell antNeighbor = (ForagingAntsCell) neighbor;
      double currentNeighborPheremoneValue =
          (type.equals("home")) ? antNeighbor.getHomePheromone() : antNeighbor.getFoodPheromone();

      maxNeighborPheromone = Math.max(maxNeighborPheromone, currentNeighborPheremoneValue);
    }

    if (maxNeighborPheromone == 0) {
      return 0;
    }

    double desiredPheromone = maxNeighborPheromone - 2;
    double currentPheromoneValue =
        (type.equals("home")) ? antCell.getHomePheromone() : antCell.getFoodPheromone();
    double pheromoneDifference = desiredPheromone - currentPheromoneValue;

    return currentPheromoneValue + pheromoneDifference;
  }

  /**
   * Method that gets neighbors based on state value
   *
   * @param cell:          the cell whose neighbors you want to find
   * @param grid:          grid on which the cell is located
   * @param occupiedCells: cells that have an ant moving to them this cycle
   */
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

  /**
   * Method that finds the neighbor of a given cell with the highest pheromones
   *
   * @param emptyNeighbors: neighbors with an empty state value
   * @param type:           String to differentiate between home and food pheromones
   */
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

    // Introduces element of randomness to avoid oscillation between multiple ants
    if (random.nextDouble() < 0.1) {
      return emptyNeighbors.get(random.nextInt(emptyNeighbors.size()));
    }

    return candidates.get(random.nextInt(candidates.size()));
  }

  /**
   * Method that adds a new ant with full health to the grid
   *
   * @param newAnt:     the ant cell to be added
   * @param nextStates: list of CellUpdates to be handled by the grid
   */
  private void addNewAnt(ForagingAntsCell newAnt, List<CellUpdate> nextStates) {
    nextStates.add(new CellUpdate(newAnt.getLocation(),
        new ForagingAntsCell(State.ANT.getValue(), newAnt.getLocation(),
            newAnt.getHomePheromone(), newAnt.getFoodPheromone() * (1 - myPheromoneDecayRate), 300,
            false, myMaxPheromoneAmount)));
  }

  /**
   * Method that moves an ant to somewhere else in the grid (adds new cell with properly updated
   * values)
   *
   * @param ant:          the current ant to based update values on
   * @param nextLocation: the location where the ant is moving
   * @param grid:         the grid on which the ant is located
   * @param nextStates:   list of CellUpdates to be handled by the grid
   * @param hasFood:      does the ant have food after being updated
   */
  private void addUpdatedAnt(ForagingAntsCell ant, ForagingAntsCell nextLocation, Grid grid,
      List<CellUpdate> nextStates, boolean hasFood) {
    nextStates.add((new CellUpdate(nextLocation.getLocation(),
        new ForagingAntsCell(State.ANT.getValue(), nextLocation.getLocation(),
            updatePheromone(nextLocation, grid, "home"),
            updatePheromone(nextLocation, grid, "food"),
            ant.getHealth(), hasFood, myMaxPheromoneAmount))));
  }

  /**
   * Method that adds an empty cell in the case of when an ant moves or dies
   *
   * @param ant:        the ant cell to be referenced for position
   * @param nextStates: list of CellUpdates to be handled by the grid
   */
  private void addEmptyCell(ForagingAntsCell ant, List<CellUpdate> nextStates) {
    nextStates.add(new CellUpdate(ant.getLocation(),
        new ForagingAntsCell(State.EMPTY.getValue(), ant.getLocation(),
            ant.getHomePheromone() * (1 - myPheromoneDecayRate),
            ant.getFoodPheromone() * (1 - myPheromoneDecayRate), 0, false, myMaxPheromoneAmount)));
  }

  /**
   * Method that checks if reproduction time has been reached and adds a specified number of ants
   * and random cells adjacent to nests
   *
   * @param grid:          the grid where ants are being added
   * @param nextStates:    list of CellUpdates to be handled by the grid
   * @param occupiedCells: cells that have an ant moving to them this cycle
   */
  private void checkIfTimeForMoreAnts(Grid grid, List<CellUpdate> nextStates,
      Set<Point2D> occupiedCells) {
    if (myReproductionTimer >= myAntReproductionTime) {
      for (Cell nestCell : nestCells) {
        for (Cell neighbor : getNeighborsByState(nestCell, grid, State.EMPTY.getValue(),
            occupiedCells)) {
          nestCellNeighbors.add(neighbor);
        }
      }

      for (int i = 0; i < myNewAntsNum; i++) {
        Cell newCell = nestCellNeighbors.get(random.nextInt(nestCellNeighbors.size()));
        nestCellNeighbors.remove(newCell);
        ForagingAntsCell newAnt = (ForagingAntsCell) newCell;
        addNewAnt(newAnt, nextStates);
        occupiedCells.add(newAnt.getLocation());
      }

      myReproductionTimer = 0;
    }
  }
}
