package cellsociety.model.simulation.rules;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.cell.DefaultCell;
import cellsociety.model.simulation.GetNeighbors;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.SimulationRules;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * The implementation of Schelling's Model of Segregation simulation
 * <p>
 * For a SegregationModel cell, there can be 3 states
 * <p>
 * A cell with state 0 indicates it's empty
 * <p>
 * A cell with state 1 indicates it's a part of Group 1
 * <p>
 * A cell with state 2 indicates it's a part of Group 2
 *
 * @author Justin Aronwald
 */
public class SegregationRules extends SimulationRules {
  private static final int toleranceMinChecker = 0;
  private static final int toleranceMaxChecker = 1;
  public static final String TOLERANCE_THRESHOLD = "toleranceThreshold";
  private final Random RANDOM = new Random();
  private final double myToleranceThreshold;

  /**
   * The default constructor of a RockPaperScissors rules.
   *
   * @param parameters The required parameters map
   * @throws InvalidParameterException This is thrown for invalid parameters provided.
   */
  public SegregationRules(Map<String, Parameter<?>> parameters, GetNeighbors myGetNeighbors)
      throws InvalidParameterException {
    super(parameters, myGetNeighbors);
    if (parameters == null || parameters.isEmpty()) {
      this.setParameters(setDefaultParameters());
    }
    checkMissingParameterAndThrowException(TOLERANCE_THRESHOLD);
    myToleranceThreshold = getParameters().get(TOLERANCE_THRESHOLD).getDouble();
    validateParameterRange();
  }

  private void validateParameterRange() throws InvalidParameterException {
    if (myToleranceThreshold < toleranceMinChecker || myToleranceThreshold > toleranceMaxChecker) {
      throwInvalidParameterException(TOLERANCE_THRESHOLD);
    }
  }

  /**
   * Get a list of all required parameters for a simulation
   *
   * @return A list of strings representing the required parameter keys for this simulation
   */
  public static List<String> getRequiredParameters() {
    return List.of(TOLERANCE_THRESHOLD);
  }


  /**
   * Schelling's Model of Segregation: There exists a probability tolerance threshold T (0.3)
   * representing the minimum number of neighbors that must be the same state for the cell to be
   * satisfied
   * <p>
   * If a cell has less than T of similar neighbors, they move to a random empty space If a cell >=
   * T of similar neighbors, they stay Cells move until all cells are satisfied or no empty spaces
   * remain
   *
   * @param cell -  individual cell from grid
   * @param grid - the list of cell objects representing the grid
   * @return - the next state of a cell based on the rules of Schelling's segregation model
   */
  // ChatGPT assisted in refactoring this method
  @Override
  public int getNextState(Cell cell, Grid grid) {
    List<Cell> neighbors = getNeighbors(cell, grid);
    int currentState = cell.getState();
    if (currentState == 0) {
      return 0;
    }
    int sameType = countSameTypeNeighbors(neighbors, currentState);
    int totalNeighbors = countOccupiedNeighbors(neighbors);
    double typePercentage = calculateTypePercentage(sameType, totalNeighbors);
    return determineNextState(totalNeighbors, typePercentage, currentState);
  }

  private int countSameTypeNeighbors(List<Cell> neighbors, int currentState) {
    int sameType = 0;
    for (Cell neighbor : neighbors) {
      if (neighbor.getState() != 0 && neighbor.getState() == currentState) {
        sameType++;
      }
    }
    return sameType;
  }

  private int countOccupiedNeighbors(List<Cell> neighbors) {
    int totalNeighbors = 0;
    for (Cell neighbor : neighbors) {
      if (neighbor.getState() != 0) {
        totalNeighbors++;
      }
    }
    return totalNeighbors;
  }

  private double calculateTypePercentage(int sameType, int totalNeighbors) {
    return totalNeighbors == 0 ? 0 : (double) sameType / totalNeighbors;
  }

  private int determineNextState(int totalNeighbors, double typePercentage, int currentState) {
    if (totalNeighbors == 0 || typePercentage >= myToleranceThreshold) {
      return currentState;
    }
    return -1; // mark as -1 to be moved
  }


  @Override
  public int getNumberStates() {
    return 3;
  }

  /**
   * This method runs getNextState on all cells
   *
   * @param grid The grid that you wish to get the next states for
   * @return - A list of updates that will occur for the grid
   */
  @Override
  public List<CellUpdate> getNextStatesForAllCells(Grid grid) {
    List<CellUpdate> nextStates = new ArrayList<>();
    List<Cell> emptyCells = new ArrayList<>();
    getEmptyCells(grid, emptyCells);

    List<Cell> unsatisfiedCells = new ArrayList<>();

    Iterator<Cell> cellIterator = grid.getCellIterator();
    while (cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      int nextState = getNextState(cell, grid);

      if (nextState == -1) {
        unsatisfiedCells.add(cell);
      } else {
        Cell newCell = new DefaultCell(nextState, cell.getLocation());
        nextStates.add(new CellUpdate(cell.getLocation(), newCell));
      }
    }

    for (Cell unsatisfiedCell : unsatisfiedCells) {
      moveCellToEmptyLocationIfAvailable(unsatisfiedCell, emptyCells, nextStates);
    }
    return nextStates;
  }

  void moveCellToEmptyLocationIfAvailable(Cell unsatisfiedCell, List<Cell> emptyCells,
      List<CellUpdate> nextStates) {
    if (!emptyCells.isEmpty()) {
      Cell newCellHome = getAndRemoveRandomEmptyCell(emptyCells);
      Cell unsatisfiedCellWithUpdatedLocation = new DefaultCell(unsatisfiedCell.getState(),
          newCellHome.getLocation());
      nextStates.add(new CellUpdate(newCellHome.getLocation(), unsatisfiedCellWithUpdatedLocation));
      nextStates.add(new CellUpdate(unsatisfiedCell.getLocation(),
          new DefaultCell(0, unsatisfiedCell.getLocation())));
      emptyCells.add(unsatisfiedCell);
    } else {
      Cell newUnsatisfiedCell = new DefaultCell(unsatisfiedCell.getState(),
          unsatisfiedCell.getLocation());
      nextStates.add(
          new CellUpdate(unsatisfiedCell.getLocation(), newUnsatisfiedCell));
    }
  }

  private Map<String, Parameter<?>> setDefaultParameters() {
    Map<String, Parameter<?>> parameters = new HashMap<>();
    parameters.put(TOLERANCE_THRESHOLD, new Parameter<>(0.3));
    return parameters;
  }

  void getEmptyCells(Grid grid, List<Cell> emptyCells) {
    int rows = grid.getRows();
    int cols = grid.getCols();
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        if (grid.getCell(row, col).getState() == 0) {
          emptyCells.add(grid.getCell(row, col));
        }
      }
    }
  }

  Cell getAndRemoveRandomEmptyCell(List<Cell> emptyCells) {
    return emptyCells.remove(RANDOM.nextInt(emptyCells.size()));
  }
}
