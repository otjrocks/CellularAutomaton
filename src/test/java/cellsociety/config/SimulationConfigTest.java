package cellsociety.config;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.cell.DefaultCell;
import cellsociety.model.cell.WaTorWorldCell;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.SimulationMetaData;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimulationConfigTest {

  // I had ChatGPT assist me in refactoring these tests after updating the Simulation Config file to use reflection API
  SimulationMetaData mySimMetaData;

  @BeforeEach
  void setUp() {
    mySimMetaData = new SimulationMetaData("Segregation", "", "", "");
  }

  @Test
  void getNewCell_WaTorWorldCell_CorrectInstance() {
    String simulationName = "WaTorWorld";
    assertInstanceOf(WaTorWorldCell.class, SimulationConfig.getNewCell(0, 0, 0, simulationName));
  }

  @Test
  void getNewCell_GameOfLife_DefaultCell() {
    String simulationName = "GameOfLife";
    assertInstanceOf(DefaultCell.class, SimulationConfig.getNewCell(0, 0, 0, simulationName));
  }

  @Test
  void getNewCell_InvalidSimulation_Exception() {
    String simulationName = "DummySimulation";
    assertThrows(IllegalArgumentException.class,
        () -> SimulationConfig.getNewCell(0, 0, 0, simulationName));
  }

  @Test
  void getNewSimulation_UnknownSimulation_Exception() {
    String simulationName = "UnknownSimulation";
    assertThrows(IllegalArgumentException.class,
        () -> SimulationConfig.getNewSimulation(simulationName, mySimMetaData, new HashMap<>()));
  }

  @Test
  void getParameters_WaTorWorld_ContainsExpectedParameters() {
    String simulationName = "WaTorWorld";
    assertTrue(SimulationConfig.getParameters(simulationName).contains("sharkReproductionTime"));
    assertTrue(SimulationConfig.getParameters(simulationName).contains("sharkEnergyGain"));
    assertTrue(SimulationConfig.getParameters(simulationName).contains("fishReproductionTime"));
  }

  @Test
  void getNewSimulation_Segregation_ValidSimulationCreation() {
    Map<String, Parameter<?>> parameters = new HashMap<>();
    String simulationName = "Segregation";
    parameters.put("toleranceThreshold",
        new Parameter<>("0.7"));
    assertDoesNotThrow(
        () -> SimulationConfig.getNewSimulation(simulationName, mySimMetaData, parameters));
  }

}
