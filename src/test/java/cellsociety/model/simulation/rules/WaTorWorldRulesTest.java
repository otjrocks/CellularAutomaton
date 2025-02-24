package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.cell.DefaultCell;
import cellsociety.model.cell.WaTorWorldCell;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.getNeighborOptions.MooreNeighbors;
import cellsociety.model.simulation.rules.WaTorWorldRules.State;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WaTorWorldRulesTest {

  private WaTorWorldRules waTorWorldRules;
  private Grid grid;
  private final Map<String, Parameter<?>> parameters = new HashMap<>();

  @BeforeEach
  void setUp() throws InvalidParameterException {
    grid = new Grid(5, 5);
    parameters.put("fishReproductionTime", new Parameter<>("3.0"));
    parameters.put("sharkReproductionTime", new Parameter<>("4.0"));
    parameters.put("sharkEnergyGain", new Parameter<>("2.0"));

    waTorWorldRules = new WaTorWorldRules(parameters, new MooreNeighbors(1));
  }

  //needed chatGPT to help process the list of updates and check for any matching
  @Test
  void testFishMovesToEmptySpace() {
    Cell fish = new WaTorWorldCell(WaTorWorldRules.State.FISH.getValue(), new Point2D.Double(2, 2));
    grid.addCell(fish);

    grid.addCell(
        new WaTorWorldCell(WaTorWorldRules.State.EMPTY.getValue(), new Point2D.Double(2, 3)));

    List<CellUpdate> updates = waTorWorldRules.getNextStatesForAllCells(grid);

    boolean fishMoved = false;
    for (CellUpdate update : updates) {
      if (update.getRow() == 2 && update.getCol() == 3 &&
          update.getState() == WaTorWorldRules.State.FISH.getValue()) {
        fishMoved = true;
        break;
      }
    }

    assertTrue(fishMoved, "Fish should move to a nearby empty space.");
  }

  @Test
  void testSharkEatsFish() {
    Cell shark = new WaTorWorldCell(WaTorWorldRules.State.SHARK.getValue(),
        new Point2D.Double(2, 2));
    grid.addCell(shark);

    Cell fish = new WaTorWorldCell(WaTorWorldRules.State.FISH.getValue(), new Point2D.Double(2, 3));
    grid.addCell(fish);

    List<CellUpdate> updates = waTorWorldRules.getNextStatesForAllCells(grid);

    boolean fishEaten = false;
    for (CellUpdate update : updates) {
      if (update.getRow() == 2 && update.getCol() == 3 &&
          update.getState() == State.SHARK.getValue()) {
        fishEaten = true;
        break;
      }
    }
    assertTrue(fishEaten, "Shark ate fish");
  }

  @Test
  void testSharkDies() {
    Cell shark = new WaTorWorldCell(WaTorWorldRules.State.SHARK.getValue(),
        new Point2D.Double(3, 3), 1, 1);
    grid.addCell(shark);

    List<CellUpdate> updates = waTorWorldRules.getNextStatesForAllCells(grid);

    boolean sharkDied = false;
    for (CellUpdate update : updates) {
      if (update.getRow() == 3 && update.getCol() == 3 &&
          update.getState() == WaTorWorldRules.State.EMPTY.getValue()) {
        sharkDied = true;
        break;
      }
    }

    assertTrue(sharkDied, "Shark should die when it's health is gone");
  }

  @Test
  void testSharkMovesToEmptySpace() {
    Cell shark = new WaTorWorldCell(WaTorWorldRules.State.SHARK.getValue(),
        new Point2D.Double(3, 3));
    grid.addCell(shark);

    grid.addCell(
        new WaTorWorldCell(WaTorWorldRules.State.EMPTY.getValue(), new Point2D.Double(3, 4)));

    List<CellUpdate> updates = waTorWorldRules.getNextStatesForAllCells(grid);

    boolean sharkMoved = false;
    for (CellUpdate update : updates) {
      if (update.getRow() == 3 && update.getCol() == 4 &&
          update.getState() == WaTorWorldRules.State.SHARK.getValue()) {
        sharkMoved = true;
        break;
      }
    }

    assertTrue(sharkMoved, "Shark should move to an empty space if there's no fish.");
  }

  @Test
  void testSetDefaultParameters() throws InvalidParameterException {
    assertEquals(3.0, waTorWorldRules.getParameters().get("fishReproductionTime").getDouble());
    assertEquals(4.0, waTorWorldRules.getParameters().get("sharkReproductionTime").getDouble());
    assertEquals(2.0, waTorWorldRules.getParameters().get("sharkEnergyGain").getDouble());
  }

  @Test
  void testStateGetValue() {
    assertEquals(0, WaTorWorldRules.State.EMPTY.getValue());
    assertEquals(1, WaTorWorldRules.State.FISH.getValue());
    assertEquals(2, WaTorWorldRules.State.SHARK.getValue());
  }

  @Test
  void testGetNeighborsMiddleCell() {
    Cell cell = new DefaultCell(1, new Point2D.Double(1, 1));
    grid.addCell(cell);

    grid.addCell(new DefaultCell(2, new Point2D.Double(0, 1))); // Top
    grid.addCell(new DefaultCell(2, new Point2D.Double(2, 1))); // Bottom
    grid.addCell(new DefaultCell(2, new Point2D.Double(1, 0))); // Left
    grid.addCell(new DefaultCell(2, new Point2D.Double(1, 2))); // Right

    List<Cell> neighbors = waTorWorldRules.getNeighbors(cell, grid);
    assertEquals(4, neighbors.size(),
        "Middle cell should only have 4 neighbors. Does not include diagonal neighbors");
  }

}