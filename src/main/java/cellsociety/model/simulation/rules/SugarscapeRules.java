package cellsociety.model.simulation.rules;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.cell.DefaultCell;
import cellsociety.model.cell.SugarscapeCell;
import cellsociety.model.simulation.GetNeighbors;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.SimulationRules;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A rules class to implement the Sugar Scape simulation
 *
 * @author Justin Aronwald
 */
public class SugarscapeRules extends SimulationRules {

  /**
   * The default constructor of a SugerScape rules.
   *
   * @param parameters The required parameters map
   * @throws InvalidParameterException This is thrown for invalid parameters provided.
   */
  public SugarscapeRules(Map<String, Parameter<?>> parameters, GetNeighbors myGetNeighbors)
      throws InvalidParameterException {
    super(parameters, myGetNeighbors);
  }

  /**
   * An enum to store the possible states for the simulation
   */
  public enum State {
    EMPTY, PATCHES, AGENTS;

    /**
     * Return the ordinal value of the state
     *
     * @return an int representing the state
     */
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

    handlePatchCellUpdate(patchCells, nextStates);
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

  private void handlePatchCellUpdate(List<SugarscapeCell> patchCells, List<CellUpdate> nextStates) {
    for (SugarscapeCell patchCell : patchCells) {
      patchCell.regenerateSugar();
      nextStates.add(new CellUpdate(patchCell.getLocation(), patchCell));
    }
  }


  //loops through all 4 directions up to vision amount of times in each direction
  // ChatGPT helped exclusively with the minDistance/max Sugar logic
  private SugarscapeCell getBiggestPatchForAgent(SugarscapeCell agentCell, Grid grid) {
    List<Cell> neighbors = getNeighbors(agentCell, grid);
    SugarscapeCell biggestPatch = null;
    int maxSugar = -1;
    int minDistance = agentCell.getVision() + 1;
    biggestPatch = getBiggestPathOfAllNeighbors(agentCell, neighbors, maxSugar, minDistance,
        biggestPatch);
    return biggestPatch;
  }

  private SugarscapeCell getBiggestPathOfAllNeighbors(SugarscapeCell agentCell,
      List<Cell> neighbors,
      int maxSugar, int minDistance, SugarscapeCell biggestPatch) {
    for (Cell cell : neighbors) {
      if (cell.getState() != State.PATCHES.getValue()) {
        continue;
      }

      int distance = calculateDistance(agentCell, cell);
      if (distance > agentCell.getVision()) {
        continue;
      }

      SugarscapeCell patch = (SugarscapeCell) cell;

      if (patch.getSugar() > maxSugar || (patch.getSugar() == maxSugar && distance < minDistance)) {
        maxSugar = patch.getSugar();
        minDistance = distance;
        biggestPatch = patch;
      }
    }
    return biggestPatch;
  }

  private int calculateDistance(Cell a, Cell b) {
    return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getCol() - b.getCol());
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

}
