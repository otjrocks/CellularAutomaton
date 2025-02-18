package cellsociety.model.simulation.rules;

import cellsociety.model.Grid;
import cellsociety.model.cell.AgentCell;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.cell.DefaultCell;
import cellsociety.model.cell.PatchCell;
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
import javax.sound.midi.Patch;

/**
 * A rules class to to implement the sugarscape simulation
 *
 * @Author Justin Aronwald
 */
public class SugarscapeRules extends SimulationRules {

  public SugarscapeRules(Map<String, Parameter<?>> parameters)
    throws InvalidParameterException {
    super(parameters);
  }

  // An enum to store the possible states for the simulation
  public enum State {
    EMPTY, PATCHES, AGENTS;

    public int getValue() {
      return ordinal();
    }
  }



  /**
   * @param cell -  individual cell from grid
   * @param grid - the collection of cell objects representing the grid
   * @return - the next state of a cell based on the rules of Sugarscape Model
   */

  @Override
  public int getNextState(Cell cell, Grid grid) {
    return 0;
  }

  /**
   * the method that handles all cell updates so the grid knows where to move the cells
   *
   * @param grid: The grid that you wish to get the next states for
   * @return - a list of updates for each cell that the grid uses to update each location
   */
  @Override
  public List<CellUpdate> getNextStatesForAllCells(Grid grid) {
    List<PatchCell> patchCells = new ArrayList<>();
    List<AgentCell> agentCells = new ArrayList<>();
    List<CellUpdate> nextStates = new ArrayList<>();
    Set<Cell> updatedCells = new HashSet<>();

    getCellsByType(grid, patchCells, agentCells);

    handlePatchCellUpdate(patchCells);
    handleAgentCellUpdate(grid, agentCells, updatedCells, nextStates);

    return nextStates;
  }

  private void handleAgentCellUpdate(Grid grid, List<AgentCell> agentCells, Set<Cell> updatedCells,
      List<CellUpdate> nextStates) {
    for (AgentCell agentCell : agentCells) {
      PatchCell biggestPatch = getBiggestPatchForAgent(agentCell, grid);
      if (biggestPatch == null || biggestPatch.getState() == State.EMPTY.getValue() || updatedCells.contains(biggestPatch)) {
        continue;
      }
      moveAgentCell(nextStates, agentCell, biggestPatch);

      updatedCells.add(biggestPatch);
      updatedCells.add(agentCell);
    }
  }

  private static void moveAgentCell(List<CellUpdate> nextStates, AgentCell agentCell,
      PatchCell biggestPatch) {
    if (biggestPatch.getState() == State.AGENTS.getValue()) {
      return;
    }

    int newSugar = agentCell.getSugar() + biggestPatch.getSugar() - agentCell.getMetabolism();
    if (newSugar <= 0) {
      nextStates.add(new CellUpdate(agentCell.getLocation(), new DefaultCell(State.EMPTY.getValue(), agentCell.getLocation())));
      return;
    }

    biggestPatch.setSugar(0);

    AgentCell newAgentCell = new AgentCell(State.AGENTS.getValue(), biggestPatch.getLocation(),
        agentCell.getVision(), agentCell.getMetabolism(), newSugar);

    nextStates.add(new CellUpdate(agentCell.getLocation(), biggestPatch));
    nextStates.add(new CellUpdate(biggestPatch.getLocation(), newAgentCell));
  }

  private void getCellsByType(Grid grid, List<PatchCell> patchCells, List<AgentCell> agentCells) {
    Iterator<Cell> cellIterator = grid.getCellIterator();
    while (cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      if (cell.getState() == State.PATCHES.getValue()) {
        patchCells.add((PatchCell) cell);
      } else if (cell.getState() == State.AGENTS.getValue()) {
        agentCells.add((AgentCell) cell);
      }
    }
  }

  private void handlePatchCellUpdate(List<PatchCell> patchCells) {
    for (PatchCell patchCell : patchCells) {
      patchCell.regenerateSugar();
    }
  }


  //loops through all 4 directions up to vision amount of times in each direction
  // Chat GPT helped exclusively with the row/col increment logic as well as creating the MutableInt class,  everything else was me
  private PatchCell getBiggestPatchForAgent(AgentCell cell, Grid grid) {
    int row = cell.getRow();
    int col = cell.getCol();
    int vision = cell.getVision();

    PatchCell biggestPatch = null;
    MutableInt maxSugar = new MutableInt(-1);
    MutableInt minDistance = new MutableInt(vision + 1);

    // up
    biggestPatch = checkAndUpdateBiggestPatch(row, col, vision, -1, 0, grid, biggestPatch, maxSugar, minDistance);
    // down
    biggestPatch = checkAndUpdateBiggestPatch(row, col, vision, 1, 0, grid, biggestPatch, maxSugar, minDistance);
    // left
    biggestPatch = checkAndUpdateBiggestPatch(row, col, vision, 0, -1, grid, biggestPatch, maxSugar, minDistance);
    // right
    biggestPatch = checkAndUpdateBiggestPatch(row, col, vision, 0, 1, grid, biggestPatch, maxSugar, minDistance);

    return biggestPatch;
  }

  private PatchCell checkAndUpdateBiggestPatch(int row, int col, int vision, int rowIncrement, int colIncrement, Grid grid, PatchCell biggestPatch, MutableInt maxSugar, MutableInt minDistance) {
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

      PatchCell curPatch = (PatchCell) currentCell;

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
    parameters.put("pathSugarGrowBackRate", new Parameter<>(4));
    parameters.put("pathSugarGrowBackInteral", new Parameter<>(3));
    parameters.put("agentVision", new Parameter<>(3));
    parameters.put("agentSugar", new Parameter<>(10));
    parameters.put("agentMetabolism", new Parameter<>(2));
    return parameters;
  }

  //This class was added so that I could have as close to an int value as a pass by reference, not by value
  static class MutableInt {
    private int value;
    public MutableInt(int value) { this.value = value; }
    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }
  }

}
