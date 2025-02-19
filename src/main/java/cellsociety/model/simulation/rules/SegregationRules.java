package cellsociety.model.simulation.rules;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.cell.DefaultCell;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.SimulationRules;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

//For a SegregationModel cell, there can be 3 states
// A cell with state 0 indicates it's empty
// A cell with state 1 indicates it's a part of Group 1
// A cell with state 2 indicates it's a part of Group 2

public class SegregationRules extends SimulationRules {

  private final Random RANDOM = new Random();
  private final double myToleranceThreshold;

  public SegregationRules(Map<String, Parameter<?>> parameters) throws InvalidParameterException {
    super(parameters);
    if (parameters == null || parameters.isEmpty()) {
      this.setParameters(setDefaultParameters());
    }
    checkMissingParameterAndThrowException("toleranceThreshold");
    myToleranceThreshold = getParameters().get("toleranceThreshold").getDouble();
    validateParameterRange();
  }

  private void validateParameterRange() throws InvalidParameterException {
    if (myToleranceThreshold < 0 || myToleranceThreshold > 1) {
      throwInvalidParameterException("toleranceThreshold");
    }
  }

  /**
   * Get a list of all required parameters for a simulation
   *
   * @return A list of strings representing the required parameter keys for this simulation
   */
  public static List<String> getRequiredParameters() {
    return List.of("toleranceThreshold");
  }

  /**
   * @param cell -  individual cell from grid
   * @param grid - the list of cell objects representing the grid
   * @return -  a list of cell objects representing the neighbors of the cell (adjacent and
   * diagonals)
   */
  public List<Cell> getNeighbors(Cell cell, Grid grid) {
    return super.getNeighbors(cell, grid, true);
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
  @Override
  public int getNextState(Cell cell, Grid grid) {

    if (cell.getRow() >= grid.getRows() || cell.getRow() < 0 || cell.getCol() >= grid.getCols()
        || cell.getCol() < 0) {
      throw new IndexOutOfBoundsException("Cell position out of bounds");
    }

    List<Cell> neighbors = getNeighbors(cell, grid);
    int currentState = cell.getState();

    if (currentState == 0) {
      return 0;
    }

    int sameType = 0;
    int totalNeighbors = 0;

    for (Cell neighbor : neighbors) {
      if (neighbor.getState() != 0) {
        totalNeighbors++;
        if (neighbor.getState() == currentState) {
          sameType++;
        }
      }
    }
    double typePercentage = (double) sameType / totalNeighbors;

    // No occupied neighbors or satisfied-> stay
    if (totalNeighbors == 0 || typePercentage >= myToleranceThreshold) {
      return currentState;
    }

    //mark as -1 to be moved
    return -1;
  }

  @Override
  public int getNumberStates() {
    return 3;
  }

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
    parameters.put("toleranceThreshold", new Parameter<>(0.3));
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
