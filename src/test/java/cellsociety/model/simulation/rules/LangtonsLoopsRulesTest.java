package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.Grid;
import cellsociety.model.cell.DefaultCell;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationMetaData;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LangtonsLoopsRulesTest {

  private static final String[] TRANSITION_RULES = {
      "000000", "000012", "000020", "000030", "000050", "000063", "000071", "000112",
      "000122", "000132", "000212", "000220", "000230", "000262", "000272", "000320",
      "000525", "000622", "000722", "001022", "001120", "002020", "002030", "002050",
      "002125", "002220", "002322", "005222", "012321", "012421", "012525", "012621",
      "012721", "012751", "014221", "014321", "014421", "014721", "016251", "017221",
      "017255", "017521", "017621", "017721", "025271", "100011", "100061", "100077",
      "100111", "100121", "100211", "100244", "100277", "100511", "101011", "101111",
      "101244", "101277", "102026", "102121", "102211", "102244", "102263", "102277",
      "102327", "102424", "102626", "102644", "102677", "102710", "102727", "105427",
      "111121", "111221", "111244", "111251", "111261", "111277", "111522", "112121",
      "112221", "112244", "112251", "112277", "112321", "112424", "112621", "112727",
      "113221", "122244", "122277", "122434", "122547", "123244", "123277", "124255",
      "124267", "125275", "200012", "200022", "200042", "200071", "200122", "200152",
      "200212", "200222", "200232", "200242", "200250", "200262", "200272", "200326",
      "200423", "200517", "200522", "200575", "200722", "201022", "201122", "201222",
      "201422", "201722", "202022", "202032", "202052", "202073", "202122", "202152",
      "202212", "202222", "202272", "202321", "202422", "202452", "202520", "202552",
      "202622", "202722", "203122", "203216", "203226", "203422", "204222", "205122",
      "205212", "205222", "205521", "205725", "206222", "206722", "207122", "207222",
      "207422", "207722", "211222", "211261", "212222", "212242", "212262", "212272",
      "214222", "215222", "216222", "217222", "222272", "222442", "222462", "222762",
      "222772", "300013", "300022", "300041", "300076", "300123", "300421", "300622",
      "301021", "301220", "302511", "401120", "401220", "401250", "402120", "402221",
      "402326", "402520", "403221", "500022", "500215", "500225", "500232", "500272",
      "500520", "502022", "502122", "502152", "502220", "502244", "502722", "512122",
      "512220", "512422", "512722", "600011", "600021", "602120", "612125", "612131",
      "612225", "700077", "701120", "701220", "701250", "702120", "702221", "702251",
      "702321", "702525", "702720"
  };

  private LangtonsLoopsRules rules;
  private Map<String, Parameter<?>> parameters;
  private Grid grid;

  @BeforeEach
  void setUp() {
    parameters = new HashMap<>();
    grid = new Grid(3, 3);
    grid.addCell(new DefaultCell(0, new Point2D.Double(1, 1)));
    grid.addCell(new DefaultCell(0, new Point2D.Double(0, 1)));
    grid.addCell(new DefaultCell(0, new Point2D.Double(1, 2)));
    grid.addCell(new DefaultCell(0, new Point2D.Double(2, 1)));
    grid.addCell(new DefaultCell(0, new Point2D.Double(1, 0)));
  }

  @Test
  void checkCorrectInitialization() {
    assertDoesNotThrow(this::createSimulation);
  }

  private Simulation createSimulation() throws InvalidParameterException {
    rules = new LangtonsLoopsRules(parameters);
    Simulation simulation = new Simulation(rules,
        new SimulationMetaData("LangtonsLoops", "", "", ""));
    return simulation;
  }

  @Test
  void checkAllZeros() throws InvalidParameterException {
    // Rule string 000000
    validateRuleString("00000", 0);
  }

  @Test
  void checkAllOnes() throws InvalidParameterException {
    // Rule string 200522
    validateRuleString("20052", 2);
  }

  @Test
  void checkAllRulesStrings() throws InvalidParameterException {
    for (String ruleString : TRANSITION_RULES) {
      validateRuleString(ruleString.substring(0, 5), Integer.parseInt(ruleString.substring(5, 6)));
    }
  }

  @Test
  void checkUnknownRuleStringReturnsSameState() throws InvalidParameterException {
    // cannot have state 9, so 999991 should return the same state, which is 9, instead of 1
    Simulation sim = createSimulation();
    validateRuleString("999991", 9);
    assertEquals(8, sim.rules().getNumberStates());
  }

  @Test
  void checkNumStates() throws InvalidParameterException {
    Simulation sim = createSimulation();
    assertEquals(8, sim.rules().getNumberStates());
  }

  private void validateRuleString(String ruleString, int expected)
      throws InvalidParameterException {
    Simulation sim = createSimulation();
    grid.updateCell(
        new DefaultCell(Integer.parseInt(ruleString.substring(0, 1)), new Double(1, 1))); // Center
    grid.updateCell(
        new DefaultCell(Integer.parseInt(ruleString.substring(1, 2)), new Double(0, 1))); // Top
    grid.updateCell(
        new DefaultCell(Integer.parseInt(ruleString.substring(2, 3)), new Double(1, 2))); // Right
    grid.updateCell(
        new DefaultCell(Integer.parseInt(ruleString.substring(3, 4)), new Double(2, 1))); // Bottom
    grid.updateCell(
        new DefaultCell(Integer.parseInt(ruleString.substring(4, 5)), new Double(1, 0))); // Left
    assertEquals(expected, sim.rules().getNextState(grid.getCell(1, 1), grid));
  }


}