package cellsociety.model.simulation.rules;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.cell.ForagingAntsCell;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;

class ForagingAntsRulesTest {
  private ForagingAntsRules FARules;
  private Grid grid;
  private final Map<String, Parameter<?>> parameters = new HashMap<>();

  @BeforeEach
  void setUp() throws InvalidParameterException {
    grid = new Grid(5, 5);
    parameters.put("antReproductionTime", new Parameter<>("3.0"));
    parameters.put("pheromoneDecayRate", new Parameter<>("0.2"));
    parameters.put("maxPheromoneAmount", new Parameter<>("100"));

    FARules = new ForagingAntsRules(parameters);
  }

  @Test
  void foragingAntRules_antNextToEmptyCell_antMovesToCell() {
    Cell ant = new ForagingAntsCell(ForagingAntsRules.State.ANT.getValue(), new Point2D.Double(2, 2));
    grid.addCell(ant);

    grid.addCell(new ForagingAntsCell(ForagingAntsRules.State.EMPTY.getValue(), new Point2D.Double(2, 3)));

    List<CellUpdate> updates = FARules.getNextStatesForAllCells(grid);

    boolean antMoved = false;
    for (CellUpdate update : updates) {
      if (update.getRow() == 2 && update.getCol() == 3 &&
          update.getState() == ForagingAntsRules.State.ANT.getValue()) {
        antMoved = true;
        break;
      }
    }

    assertTrue(antMoved, "Ant should move to a nearby empty space.");
  }

  @Test
  void foragingAntRules_antNextToFoodCell_antGetsTheFood() {
    Cell ant = new ForagingAntsCell(ForagingAntsRules.State.ANT.getValue(), new Point2D.Double(2, 2));
    grid.addCell(ant);

    grid.addCell(new ForagingAntsCell(ForagingAntsRules.State.EMPTY.getValue(), new Point2D.Double(2, 3)));
    grid.addCell(new ForagingAntsCell(ForagingAntsRules.State.FOOD.getValue(), new Point2D.Double(2, 1)));

    List<CellUpdate> updates = FARules.getNextStatesForAllCells(grid);

    boolean antGetsFood = false;
    for (CellUpdate update : updates) {
      if (update.getRow() == 2 && update.getCol() == 3 &&
          update.getState() == ForagingAntsRules.State.ANT.getValue() &&
          ((ForagingAntsCell) (update.getNextCell())).getHasFood()) {
        antGetsFood = true;
        break;
      }
    }

    assertTrue(antGetsFood, "Ant should grab the food.");
  }

  @Test
  void foragingAntRules_antNextToNestCell_antDropsPheremones() {
    Cell ant = new ForagingAntsCell(ForagingAntsRules.State.ANT.getValue(), new Point2D.Double(2, 3));
    grid.addCell(ant);

    grid.addCell(new ForagingAntsCell(ForagingAntsRules.State.EMPTY.getValue(), new Point2D.Double(2, 2)));
    grid.addCell(new ForagingAntsCell(ForagingAntsRules.State.NEST.getValue(), new Point2D.Double(2, 1)));

    List<CellUpdate> updates = FARules.getNextStatesForAllCells(grid);

    boolean antDropsHomePheremones = false;
    for (CellUpdate update : updates) {
      if (update.getRow() == 2 && update.getCol() == 2 &&
          update.getState() == ForagingAntsRules.State.ANT.getValue() &&
          ((ForagingAntsCell)(update.getNextCell())).getHomePheromone() > 0 &&
          ((ForagingAntsCell)(update.getNextCell())).getFoodPheromone() == 0) {
        antDropsHomePheremones = true;
        break;
      }
    }

    assertTrue(antDropsHomePheremones, "Ant should update home pheremones only");
  }

  @Test
  void foragingAntRules_antNextToNestCell_antDropsFoodPheremones() {
    Cell ant = new ForagingAntsCell(ForagingAntsRules.State.ANT.getValue(), new Point2D.Double(2, 3));
    grid.addCell(ant);

    grid.addCell(new ForagingAntsCell(ForagingAntsRules.State.EMPTY.getValue(), new Point2D.Double(2, 2)));
    grid.addCell(new ForagingAntsCell(ForagingAntsRules.State.FOOD.getValue(), new Point2D.Double(2, 1)));

    List<CellUpdate> updates = FARules.getNextStatesForAllCells(grid);

    boolean antDropsFoodPheremones = false;
    for (CellUpdate update : updates) {
      if (update.getRow() == 2 && update.getCol() == 2 &&
          update.getState() == ForagingAntsRules.State.ANT.getValue() &&
          ((ForagingAntsCell)(update.getNextCell())).getHomePheromone() == 0 &&
          ((ForagingAntsCell)(update.getNextCell())).getFoodPheromone() > 0) {
        antDropsFoodPheremones = true;
        break;
      }
    }

    assertTrue(antDropsFoodPheremones, "Ant should update food pheremones only");
  }
}