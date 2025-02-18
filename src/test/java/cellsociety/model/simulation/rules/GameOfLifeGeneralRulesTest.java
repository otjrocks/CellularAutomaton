package cellsociety.model.simulation.rules;

import java.awt.geom.Point2D.Double;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.DefaultCell;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;

class GameOfLifeGeneralRulesTest {

  private Grid grid;
  private GameOfLifeRules gameOfLifeRules;


  @Test
  void GameOfLifeGeneral_rulestringBS_throwsException() throws InvalidParameterException{
    assertThrows(Exception.class, () -> gameOfLifeRules = new GameOfLifeRules(Map.of("ruleString", new Parameter<>("B/S"))),
        "Calling getNextState() on a null cell should throw NullPointerException.");
    
  }

  @Test
  void GameOfLifeGeneral_rulestringB4S2_cellIsCorrectlyBorn() throws InvalidParameterException{
    grid = new Grid(5, 5);

    gameOfLifeRules = new GameOfLifeRules(Map.of("ruleString", new Parameter<>("B4/S2")));
    Cell cell = new DefaultCell(0, new Double(2, 2));

    grid.addCell(new DefaultCell(1, new Double(1, 2)));
    grid.addCell(new DefaultCell(1, new Double(3, 2)));
    grid.addCell(new DefaultCell(1, new Double(2, 3)));
    grid.addCell(new DefaultCell(1, new Double(2, 1)));

    assertEquals(1, gameOfLifeRules.getNextState(cell, grid),
        "A dead cell with 4 neighbors should become alive");
  }

  @Test
  void GameOfLifeGeneral_rulestringB5S2_cellShouldeNotBeBorn() throws InvalidParameterException{
    grid = new Grid(5, 5);

    gameOfLifeRules = new GameOfLifeRules(Map.of("ruleString", new Parameter<>("B5/S2")));
    Cell cell = new DefaultCell(0, new Double(2, 2));

    grid.addCell(new DefaultCell(1, new Double(1, 2)));
    grid.addCell(new DefaultCell(1, new Double(3, 2)));
    grid.addCell(new DefaultCell(1, new Double(2, 3)));
    grid.addCell(new DefaultCell(1, new Double(2, 1)));

    assertEquals(0, gameOfLifeRules.getNextState(cell, grid),
        "A dead cell with less than 5 neighbors should remain dead");
  }

  @Test
  void GameOfLifeGeneral_noParameter_worksAsNormalConwaysGoL() throws InvalidParameterException{
    grid = new Grid(5, 5);

    gameOfLifeRules = new GameOfLifeRules(new HashMap<>());
    Cell cell = new DefaultCell(0, new Double(2, 2));

    grid.addCell(new DefaultCell(1, new Double(1, 2)));
    grid.addCell(new DefaultCell(1, new Double(3, 2)));
    grid.addCell(new DefaultCell(1, new Double(2, 3)));

    assertEquals(1, gameOfLifeRules.getNextState(cell, grid),
        "A dead cell with 3 neighbors should become alive");
  }

   @Test
   void GameOfLifeGeneral_rulestringB8S1234567_cellDoesNotRemainAlive() throws InvalidParameterException{
    grid = new Grid(5, 5);

    gameOfLifeRules = new GameOfLifeRules(Map.of("ruleString", new Parameter<>("B5/S2")));
    Cell cell = new DefaultCell(1, new Double(2, 2));

    grid.addCell(new DefaultCell(1, new Double(1, 1)));
    grid.addCell(new DefaultCell(1, new Double(1, 2)));
    grid.addCell(new DefaultCell(1, new Double(1, 3)));
    grid.addCell(new DefaultCell(1, new Double(2, 1)));
    grid.addCell(new DefaultCell(1, new Double(2, 3)));
    grid.addCell(new DefaultCell(1, new Double(3, 1)));
    grid.addCell(new DefaultCell(1, new Double(3, 2)));
    grid.addCell(new DefaultCell(1, new Double(3, 3)));


    assertEquals(0, gameOfLifeRules.getNextState(cell, grid),
        "Cell should not remain alive");
  }
}
  
