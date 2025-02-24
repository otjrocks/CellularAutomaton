package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.Grid;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.cell.DefaultCell;
import cellsociety.model.cell.SugarscapeCell;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.getNeighborOptions.MooreNeighbors;
import cellsociety.model.simulation.rules.SugarscapeRules.State;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SugarscapeRulesTest {
  private Grid grid;
  private SugarscapeRules sugarscapeRules;
  private final Map<String, Parameter<?>> parameters = new HashMap<>();

  @BeforeEach
  void setUp() throws InvalidParameterException {
    grid = new Grid(5, 5);
    parameters.put("patchSugarGrowBackRate", new Parameter<>(4));
    parameters.put("patchSugarGrowBackInterval", new Parameter<>(3));
    parameters.put("patchSugar", new Parameter<>(3));
    parameters.put("agentVision", new Parameter<>(3));
    parameters.put("agentSugar", new Parameter<>(10));
    parameters.put("agentMetabolism", new Parameter<>(2));

    sugarscapeRules = new SugarscapeRules(parameters, new MooreNeighbors(parameters.get("agentVision").getInteger()));

    for (int i = 0; i < grid.getRows(); i++) {
      for (int j = 0; j < grid.getCols(); j++) {
        grid.addCell(new DefaultCell(0, new Point2D.Double(i, j)));
      }
    }
  }


  @Test
  void getNextStatesForAllCells_PatchSugarRegeneration_UpdatedSugar() {
    SugarscapeCell patch = new SugarscapeCell(SugarscapeRules.State.PATCHES.getValue(), new Point2D.Double(2, 2),3, 5, 10,0,0);

    patch.regenerateSugar();
    patch.regenerateSugar();
    patch.regenerateSugar();
    patch.regenerateSugar();
    patch.regenerateSugar();

    assertEquals(13, patch.getSugar());
  }

  @Test
  void getNextStatesForAllCells_AgentMovesToRichestPatch_UpdatedAgentCellAndSugar() {
    SugarscapeCell agent = new SugarscapeCell(SugarscapeRules.State.AGENTS.getValue(), new Point2D.Double(2, 2), 10, 1,0,2, 1);
    SugarscapeCell lowSugarPatch = new SugarscapeCell(SugarscapeRules.State.PATCHES.getValue(), new Point2D.Double(2, 3), 2, 3, 1, 0, 0);
    SugarscapeCell anotherLowSugarPatch = new SugarscapeCell(SugarscapeRules.State.PATCHES.getValue(), new Point2D.Double(2, 4), 2, 3, 1, 0,0 );
    SugarscapeCell highSugarPatch = new SugarscapeCell(SugarscapeRules.State.PATCHES.getValue(), new Point2D.Double(3, 2), 8, 3, 1, 0,0);

    grid.updateCell(agent);
    grid.updateCell(lowSugarPatch);
    grid.updateCell(highSugarPatch);
    grid.updateCell(anotherLowSugarPatch);

    List<CellUpdate> updates = sugarscapeRules.getNextStatesForAllCells(grid);
    boolean consumedPatch = false;
    int newSugar = -1;

    for (CellUpdate update : updates) {
      if (update.getNextCell().getLocation().equals(new Point2D.Double(3, 2)) && update.getNextCell().getState() == State.AGENTS.getValue()) {
        consumedPatch = true;
        SugarscapeCell agentCell = (SugarscapeCell) update.getNextCell();
        newSugar = agentCell.getSugar();
      }

      if (update.getNextCell().getLocation().equals(new Point2D.Double(3, 2)) && update.getNextCell().getState() == State.PATCHES.getValue()) {
        SugarscapeCell patchCell = (SugarscapeCell) update.getNextCell();
        assertEquals(0, patchCell.getSugar(), "Path cell reset to 0");
      }
    }

    assertTrue(consumedPatch, "Agent cell moved to the biggest patch");
    assertEquals(17, newSugar, "Agent (10) consumed biggest patch (7) but lost 1 due to metabolism");
  }

  @Test
  void getNextStatesForAllCells_AgentChoosesMinDistanceInTie_UpdatedAgentCell() {
    SugarscapeCell agent = new SugarscapeCell(SugarscapeRules.State.AGENTS.getValue(), new Point2D.Double(2, 2), 10, 1,0, 1, 1);
    SugarscapeCell patch1 = new SugarscapeCell(SugarscapeRules.State.PATCHES.getValue(), new Point2D.Double(2, 3), 5, 3, 1, 0, 0);
    SugarscapeCell patch2 = new SugarscapeCell(SugarscapeRules.State.PATCHES.getValue(), new Point2D.Double(2, 4), 5, 3, 1, 0,0);
    SugarscapeCell patch3 = new SugarscapeCell(SugarscapeRules.State.PATCHES.getValue(), new Point2D.Double(4, 2), 5, 3, 1, 0, 0);

    grid.updateCell(agent);
    grid.updateCell(patch1);
    grid.updateCell(patch2);
    grid.updateCell(patch3);

    List<CellUpdate> updates = sugarscapeRules.getNextStatesForAllCells(grid);
    boolean consumedPatch = false;

    for (CellUpdate update : updates) {
      if (update.getNextCell().getLocation().equals(new Point2D.Double(2, 3)) && update.getNextCell().getState() == State.AGENTS.getValue()) {
        consumedPatch = true;
        break;
      }
    }
    assertTrue(consumedPatch, "Agent cell moved to the closest patch at (2,3) ");
  }

  @Test
  void getNextStatesForAllCells_SugarReachesZero_AgentDies() {
    SugarscapeCell agent = new SugarscapeCell(SugarscapeRules.State.AGENTS.getValue(), new Point2D.Double(2, 2), 2, 1, 0, 5, 2);

    grid.updateCell(agent);

    List<CellUpdate> updates = sugarscapeRules.getNextStatesForAllCells(grid);
    boolean agentDied = false;

    for (CellUpdate update : updates) {
      if (update.getNextCell().getState() == State.EMPTY.getValue()) {
        agentDied = true;
        break;
      }
    }
    assertTrue(agentDied, "Agent cell did not move and died");
  }




}