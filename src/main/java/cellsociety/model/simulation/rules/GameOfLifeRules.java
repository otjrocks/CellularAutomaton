package cellsociety.model.simulation.rules;

import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.simulation.SimulationRules;
import java.util.List;
import java.util.Map;

public class GameOfLifeRules extends SimulationRules {

  private ArrayList<Integer> birthValues;
  private ArrayList<Integer> surviveValues;

  public GameOfLifeRules(Map<String, Parameter<?>> parameters) throws InvalidParameterException {
    super(parameters);
    if (parameters == null || parameters.isEmpty()) {
      birthValues = new ArrayList<>(Arrays.asList(3));
      surviveValues = new ArrayList<>(Arrays.asList(2, 3));
    } else {
      initializeBSValues(parameters);
    }
  }

  /**
   * @param cell - individual cell from grid
   * @return - a list of cell objects representing the neighbors of the cell (adjacent and
   * diagonals)
   */
  public List<Cell> getNeighbors(Cell cell, Grid grid) {
    return super.getNeighbors(cell, grid, true);
  }


  /**
   * Game of Life: Any cell with fewer than 2 neighbors dies due to underpopulation Any cell with 2
   * - 3 neighbors moves on to the next generation Any cell with more than 3 neighbors dies due to
   * overpopulation Any inactive cell with exactly 3 neighbors becomes active
   *
   * @param cell - individual cell from grid
   * @return the next state of a cell based on the rules of game of life
   */
  @Override
  public int getNextState(Cell cell, Grid grid) {

    if (cell.getRow() >= grid.getRows() || cell.getRow() < 0 || cell.getCol() >= grid.getCols()
        || cell.getCol() < 0) {
      throw new IndexOutOfBoundsException("Cell position out of bounds");
    }

    List<Cell> neighbors = getNeighbors(cell, grid);
    int aliveNeighbors = 0;
    for (Cell neighbor : neighbors) {
      if (neighbor.getState() == 1) {
        aliveNeighbors++;
      }
    }
    if (!(surviveValues.contains(aliveNeighbors))) {
      return 0;
    } else if (birthValues.contains(aliveNeighbors)) {
      return 1;
    }
    return cell.getState();
  }

  private void initializeBSValues(Map<String, Parameter<?>> parameters)
      throws InvalidParameterException {
    String rulestring = parameters.get("ruleString").getString();

    String[] sStrings = rulestring.split("/")[0].split("");
    String[] bStrings = rulestring.split("/")[1].split("");

    Integer[] sValues = convertStringArray(sStrings);
    Integer[] bValues = convertStringArray(bStrings);

    surviveValues = new ArrayList<>(Arrays.asList(sValues));
    birthValues = new ArrayList<>(Arrays.asList(bValues));
  }

  private Integer[] convertStringArray(String[] strings) {
    Integer[] ints = new Integer[strings.length];
    for (int i = 0; i < strings.length; i++) {
      ints[i] = Integer.parseInt(strings[i]);
      System.out.println(ints[i]);
    }
    return ints;
  }

  @Override
  public int getNumberStates() {
    return 2;
  }
}
