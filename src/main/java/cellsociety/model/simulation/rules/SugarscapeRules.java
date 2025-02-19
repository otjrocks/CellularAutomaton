package cellsociety.model.simulation.rules;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.cell.DefaultCell;
import cellsociety.model.cell.SugarscapeCell;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.SimulationRules;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A rules class to to implement the sugarscape simulation
 *
 * @author Justin Aronwald
 */
public class SugarscapeRules extends SimulationRules {

  public SugarscapeRules(Map<String, Parameter<?>> parameters)
      throws InvalidParameterException {
    super(parameters);
  }

  /**
   * Get a list of all required parameters for a simulation
   *
   * @return A list of strings representing the required parameter keys for this simulation
   */
  public static List<String> getRequiredParameters() {
    return List.of("patchSugarGrowBackRate", "patchSugarGrowBackInterval", "agentVision",
        "agentSugar", "agentMetabolism");
  }

  // An enum to store the possible states for the simulation
  public enum State {
    EMPTY, PATCHES, AGENTS;

    public int getValue() {
      return ordinal();
    }
  }

  /**
   * the method that handles all cell updates so the grid knows where to move the cells
   *
   * @param grid: The grid that you wish to get the next states for
   * @return - a list of updates for each cell that the grid uses to update each location
   */
  @Override
  public List<CellUpdate> getNextStatesForAllCells(Grid grid) {
    List<SugarscapeCell> patchCells = new ArrayList<>();
    List<SugarscapeCell> agentCells = new ArrayList<>();
    List<CellUpdate> nextStates = new ArrayList<>();
    Set<SugarscapeCell> updatedCells = new HashSet<>();

    getCellsByType(grid, patchCells, agentCells);

    handlePatchCellUpdate(patchCells);
    handleAgentCellUpdate(grid, agentCells, updatedCells, nextStates);

    return nextStates;
  }

  private void handleAgentCellUpdate(Grid grid, List<SugarscapeCell> agentCells,
      Set<SugarscapeCell> updatedCells,
      List<CellUpdate> nextStates) {
    for (SugarscapeCell agentCell : agentCells) {
      SugarscapeCell biggestPatch = getBiggestPatchForAgent(agentCell, grid);

      if (agentCell.getSugar() - agentCell.getMetabolism() <= 0) {
        nextStates.add(new CellUpdate(agentCell.getLocation(),
            new DefaultCell(State.EMPTY.getValue(), agentCell.getLocation())));
        continue;
      }

      if (biggestPatch == null || biggestPatch.getState() == State.EMPTY.getValue()
          || updatedCells.contains(biggestPatch)) {
        agentCell.setSugar(agentCell.getSugar() - agentCell.getMetabolism());
        continue;
      }
      moveAgentCell(nextStates, agentCell, biggestPatch);

      updatedCells.add(biggestPatch);
      updatedCells.add(agentCell);
    }
  }

  private static void moveAgentCell(List<CellUpdate> nextStates, SugarscapeCell agentCell,
      SugarscapeCell biggestPatch) {
    if (biggestPatch.getState() == State.AGENTS.getValue()) {
      return;
    }

    int newSugar = agentCell.getSugar() + biggestPatch.getSugar() - agentCell.getMetabolism();
    if (newSugar <= 0) {
      nextStates.add(new CellUpdate(agentCell.getLocation(),
          new DefaultCell(State.EMPTY.getValue(), agentCell.getLocation())));
      return;
    }

    biggestPatch.setSugar(0);

    SugarscapeCell newAgentCell = new SugarscapeCell(State.AGENTS.getValue(),
        biggestPatch.getLocation(),
        newSugar, 1, 0,
        agentCell.getVision(), agentCell.getMetabolism());

    SugarscapeCell newPatchCell = new SugarscapeCell(State.PATCHES.getValue(),
        agentCell.getLocation(), 0, biggestPatch.getSugarGrowBackInterval(),
        biggestPatch.getSugarGrowBackRate(), 0, 0);

    nextStates.add(new CellUpdate(agentCell.getLocation(), newPatchCell));
    nextStates.add(new CellUpdate(biggestPatch.getLocation(), newAgentCell));
  }

  private void getCellsByType(Grid grid, List<SugarscapeCell> patchCells,
      List<SugarscapeCell> agentCells) {
    Iterator<Cell> cellIterator = grid.getCellIterator();
    while (cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      if (cell.getState() == State.PATCHES.getValue()) {
        patchCells.add((SugarscapeCell) cell);
      } else if (cell.getState() == State.AGENTS.getValue()) {
        agentCells.add((SugarscapeCell) cell);
      }
    }
  }

  private void handlePatchCellUpdate(List<SugarscapeCell> patchCells) {
    for (SugarscapeCell patchCell : patchCells) {
      patchCell.regenerateSugar();
    }
  }


  //loops through all 4 directions up to vision amount of times in each direction
  // Chat GPT helped exclusively with the row/col increment logic as well as creating the MutableInt class,  everything else was me
  private SugarscapeCell getBiggestPatchForAgent(SugarscapeCell cell, Grid grid) {
    int row = cell.getRow();
    int col = cell.getCol();
    int vision = cell.getVision();

    SugarscapeCell biggestPatch = null;
    MutableInt maxSugar = new MutableInt(-1);
    MutableInt minDistance = new MutableInt(vision + 1);

    // up
    biggestPatch = checkAndUpdateBiggestPatch(row, col, vision, -1, 0, grid, biggestPatch, maxSugar,
        minDistance);
    // down
    biggestPatch = checkAndUpdateBiggestPatch(row, col, vision, 1, 0, grid, biggestPatch, maxSugar,
        minDistance);
    // left
    biggestPatch = checkAndUpdateBiggestPatch(row, col, vision, 0, -1, grid, biggestPatch, maxSugar,
        minDistance);
    // right
    biggestPatch = checkAndUpdateBiggestPatch(row, col, vision, 0, 1, grid, biggestPatch, maxSugar,
        minDistance);

    return biggestPatch;
  }

  private SugarscapeCell checkAndUpdateBiggestPatch(int row, int col, int vision, int rowIncrement,
      int colIncrement, Grid grid, SugarscapeCell biggestPatch, MutableInt maxSugar,
      MutableInt minDistance) {
    for (int i = 1; i <= vision; i++) {
      int newRow = row + (i * rowIncrement);
      int newCol = col + (i * colIncrement);

      if (newRow < 0 || newRow >= grid.getRows() || newCol < 0 || newCol >= grid.getCols()) {
        break;
      }
      Cell currentCell = grid.getCell(newRow, newCol);

      if (currentCell.getState() != State.PATCHES.getValue()) {
        continue;
      }

      SugarscapeCell curPatch = (SugarscapeCell) currentCell;

      if (curPatch.getSugar() > maxSugar.getValue()) {
        maxSugar.setValue(curPatch.getSugar());
        minDistance.setValue(i);
        biggestPatch = curPatch;
      } else if (curPatch.getSugar() == maxSugar.getValue() && i < minDistance.getValue()) {
        minDistance.setValue(i);
        biggestPatch = curPatch;
      }
    }

    return biggestPatch;
  }


  /**
   * The simulation has 3 states: EMPTY, PATCH, AGENT
   *
   * @return 3 for number of states
   */
  @Override
  public int getNumberStates() {
    return SugarscapeRules.State.values().length;
  }

  private Map<String, Parameter<?>> setDefaultParameters() {
    Map<String, Parameter<?>> parameters = new HashMap<>();
    parameters.put("patchSugarGrowBackRate", new Parameter<>(4));
    parameters.put("patchSugarGrowBackInteral", new Parameter<>(3));
    parameters.put("agentVision", new Parameter<>(3));
    parameters.put("agentSugar", new Parameter<>(10));
    parameters.put("agentMetabolism", new Parameter<>(2));
    return parameters;
  }

  //This class was added so that I could have as close to an int value as a pass by reference, not by value
  static class MutableInt {

    private int value;

    public MutableInt(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }

    public void setValue(int value) {
      this.value = value;
    }
  }

}
