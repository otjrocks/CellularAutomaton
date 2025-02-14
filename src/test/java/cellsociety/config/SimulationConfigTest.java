package cellsociety.config;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.cell.DefaultCell;
import cellsociety.model.cell.WaTorCell;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.SimulationMetaData;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimulationConfigTest {

  SimulationMetaData simMetaData;

  @BeforeEach
  void setUp() {
    simMetaData = new SimulationMetaData("", "", "", "");
  }

  @Test
  void getNewWaTorCell() {
    String simulationName = "WaTorWorld";
    assertInstanceOf(WaTorCell.class, SimulationConfig.getNewCell(0, 0, 0, simulationName));
  }

  @Test
  void getNewDefaultCell() {
    String simulationName = "GameOfLife";
    assertInstanceOf(DefaultCell.class, SimulationConfig.getNewCell(0, 0, 0, simulationName));
  }

  @Test
  void cellSimulationDoesNotExist() {
    String simulationName = "DummySimulation";
    assertThrows(IllegalArgumentException.class,
        () -> SimulationConfig.getNewCell(0, 0, 0, simulationName));
  }

  @Test
  void getWaTor() {
    String simulationName = "WaTorWorld";
    Map<String, Parameter<?>> parameters = new HashMap<>();
    parameters.put("sharkReproductionTime", new Parameter<>(1.0));
    parameters.put("sharkEnergyGain", new Parameter<>(1.0));
    parameters.put("fishReproductionTime", new Parameter<>(1.0));
    assertThrows(NullPointerException.class,
        () -> SimulationConfig.getNewSimulation(simulationName, simMetaData, null));
  }

  @Test
  void getUnknown() {
    String simulationName = "UnknownSimulation";
    assertThrows(IllegalArgumentException.class,
        () -> SimulationConfig.getNewSimulation(simulationName, simMetaData, null));
  }


  @Test
  void getParameters() {
    String simulationName = "WaTorWorld";
    assertTrue(SimulationConfig.getParameters(simulationName).contains("sharkReproductionTime"));
    assertTrue(SimulationConfig.getParameters(simulationName).contains("sharkEnergyGain"));
    assertTrue(SimulationConfig.getParameters(simulationName).contains("fishReproductionTime"));
  }

  @Test
  void checkTooLargeParameters() {
    Map<String, Parameter<?>> parameters = new HashMap<>();
    String simulationName = "Segregation";
    parameters.put("toleranceThreshold", new Parameter<>("1.1")); // invalid parameter out of range [0,1]
    assertThrows(IllegalArgumentException.class,
        () -> SimulationConfig.getNewSimulation(simulationName, simMetaData, parameters));

  }

  @Test
  void checkTooSmallParameters() {
    Map<String, Parameter<?>> parameters = new HashMap<>();
    String simulationName = "Segregation";
    parameters.put("toleranceThreshold", new Parameter<>("-0.1")); // invalid parameter out of range [0,1]
    assertThrows(IllegalArgumentException.class,
        () -> SimulationConfig.getNewSimulation(simulationName, simMetaData, parameters));

  }

  @Test
  void checkValidParameters() {
    Map<String, Parameter<?>> parameters = new HashMap<>();
    String simulationName = "Segregation";
    parameters.put("toleranceThreshold", new Parameter<>("0.7")); // invalid parameter out of range [0,1]
    SimulationConfig.getNewSimulation(simulationName, simMetaData, parameters);
  }

  @Test
  void testValidParametersRockPaper() {
    Map<String, Parameter<?>> parameters = new HashMap<>();
    String simulationName = "RockPaperScissors";
    parameters.put("numStates", new Parameter<>("21")); // invalid number of states
    parameters.put("minThreshold", new Parameter<>("0.7"));
    assertThrows(IllegalArgumentException.class,
        () -> SimulationConfig.getNewSimulation(simulationName, simMetaData, parameters));
    parameters.clear();
    parameters.put("numStates", new Parameter<>("5")); // valid parameters
    parameters.put("minThreshold", new Parameter<>("0.7"));
    assertDoesNotThrow(() -> SimulationConfig.getNewSimulation(simulationName, simMetaData, parameters));
  }
}