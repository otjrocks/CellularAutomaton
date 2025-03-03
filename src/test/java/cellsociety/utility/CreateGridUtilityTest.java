package cellsociety.utility;

import cellsociety.model.edge.FixedEdgeStrategy;
import cellsociety.model.simulation.getNeighborOptions.VonNeumannNeighbors;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import cellsociety.model.Grid;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.model.simulation.rules.SpreadingOfFireRules;
import cellsociety.model.xml.GridException;
import cellsociety.model.xml.InvalidStateException;

public class CreateGridUtilityTest {

  private Simulation mockSimulation;
  private Document gridDoc;
  private Document randomGridDocFromStates;
  private Document randomGridDocFromDist;
  private Document invalidGridDoc;
  private Map<String, Parameter<?>> parameters = new HashMap<>();

  @BeforeEach
  void setUp() throws Exception {
    mockSimulation = new Simulation(
        new SpreadingOfFireRules(parameters, new VonNeumannNeighbors(1)),
        new SimulationMetaData("SpreadingOfFire", "FireSpread", "Troy Ludwig",
            "There is fire spreading", "VonNeumann", 1));
    String explicitXmlData = """
        <Grid>
            <Row>1,1,2</Row>
            <Row>0,1,1</Row>
            <Row>2,0,1</Row>
        </Grid>
        """;
    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    gridDoc = builder.parse(
        new ByteArrayInputStream(explicitXmlData.getBytes(StandardCharsets.UTF_8)));

    String randomStatesXmlData = """
        <RandomInitByState>
            <State name="TREE">5</State>
        </RandomInitByState>
        """;
    randomGridDocFromStates = builder.parse(
        new ByteArrayInputStream(randomStatesXmlData.getBytes(StandardCharsets.UTF_8)));

    String randomDistXmlData = """
        <RandomInitByProb>
            <State name="TREE">50</State>
        </RandomInitByProb>
        """;
    randomGridDocFromDist = builder.parse(
        new ByteArrayInputStream(randomDistXmlData.getBytes(StandardCharsets.UTF_8)));

    String invalidXmlData = """
        <Grid>
            <Row>1,5,2</Row>
            <Row>0,1,1</Row>
            <Row>2,0,1</Row>
        </Grid>
        """;
    invalidGridDoc = builder.parse(
        new ByteArrayInputStream(invalidXmlData.getBytes(StandardCharsets.UTF_8)));
  }

  @Test
  void testGenerateGridValid() throws Exception {
    Grid grid = CreateGridUtility.generateGrid(gridDoc, 3, 3, mockSimulation,
        new FixedEdgeStrategy());
    assertNotNull(grid);
    assertEquals(3, grid.getRows());
    assertEquals(3, grid.getCols());
  }

  @Test
  void testGenerateGridInvalidSize() {
    String invalidXml = """
        <Grid>
            <Row>1,1,2,3</Row>
            <Row>0,1,1,3</Row>
        </Grid>
        """;
    try {
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document invalidDoc = builder.parse(
          new ByteArrayInputStream(invalidXml.getBytes(StandardCharsets.UTF_8)));
      assertThrows(GridException.class,
          () -> CreateGridUtility.generateGrid(invalidDoc, 2, 3, mockSimulation,
              new FixedEdgeStrategy()));
    } catch (Exception e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  void testGenerateRandomGridFromStateNumber() {
    Grid grid = CreateGridUtility.generateRandomGridFromStateNumber(randomGridDocFromStates, 3, 3,
        new FixedEdgeStrategy(), mockSimulation);
    assertNotNull(grid);
    assertEquals(3, grid.getRows());
    assertEquals(3, grid.getCols());
  }

  @Test
  void testGenerateRandomGridFromDistribution() {
    Grid grid = CreateGridUtility.generateRandomGridFromDistribution(randomGridDocFromDist, 3, 3,
        new FixedEdgeStrategy(), mockSimulation);
    assertNotNull(grid);
    assertEquals(3, grid.getRows());
    assertEquals(3, grid.getCols());
  }

  @Test
  void testCheckValidStateException() {
    assertThrows(InvalidStateException.class, () -> {
      CreateGridUtility.generateGrid(invalidGridDoc, 3, 3, mockSimulation, new FixedEdgeStrategy());
    });
  }
}
