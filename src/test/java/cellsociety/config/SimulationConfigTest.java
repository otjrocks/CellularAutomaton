package cellsociety.config;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.cell.DefaultCell;
import cellsociety.model.cell.WaTorWorldCell;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.SimulationMetaData;
import java.lang.reflect.InvocationTargetException;
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
    assertInstanceOf(WaTorWorldCell.class, SimulationConfig.getNewCell(0, 0, 0, simulationName));
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
  void checkValidParameters() {
    Map<String, Parameter<?>> parameters = new HashMap<>();
    String simulationName = "Segregation";
    parameters.put("toleranceThreshold",
        new Parameter<>("0.7")); // invalid parameter out of range [0,1]
    try {
      SimulationConfig.getNewSimulation(simulationName, simMetaData, parameters);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (InvalidParameterException e) {
      throw new RuntimeException(e);
    }
  }

}