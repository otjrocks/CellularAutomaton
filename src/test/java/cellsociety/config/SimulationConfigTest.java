package cellsociety.config;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.cell.DefaultCell;
import cellsociety.model.cell.WaTorCell;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.model.simulation.types.GameOfLife;
import cellsociety.model.simulation.types.WaTorWorld;
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
    assertTrue(SimulationConfig.getNewCell(0, 0, 0, simulationName) instanceof WaTorCell);
  }

  @Test
  void getNewDefaultCell() {
    String simulationName = "GameOfLife";
    assertTrue(SimulationConfig.getNewCell(0, 0, 0, simulationName) instanceof DefaultCell);
  }

  @Test
  void cellSimulationDoesNotExist() {
    String simulationName = "DummySimulation";
    assertThrows(IllegalArgumentException.class,
        () -> SimulationConfig.getNewCell(0, 0, 0, simulationName));
  }

  @Test
  void getGameOfLife() {
    String simulationName = "GameOfLife";
    assertTrue(
        SimulationConfig.getNewSimulation(simulationName, new SimulationMetaData("", "", "", ""),
            null) instanceof GameOfLife);
  }

  @Test
  void getWaTor() {
    String simulationName = "WaTorWorld";
    Map<String, Double> parameters = new HashMap<>();
    parameters.put("sharkReproductionTime", 1.0);
    parameters.put("sharkEnergyGain", 1.0);
    assertThrows(IllegalArgumentException.class,
        () -> SimulationConfig.getNewSimulation(simulationName, simMetaData, parameters));
    parameters.put("fishReproductionTime", 1.0);
    assertTrue(SimulationConfig.getNewSimulation(simulationName, simMetaData,
        parameters) instanceof WaTorWorld);
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
}