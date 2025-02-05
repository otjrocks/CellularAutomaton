package cellsociety.model.xmlhandling;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cellsociety.model.XMLHandlers.XMLHandler;
import cellsociety.model.simulation.rules.GameOfLifeRules;

class XMLTest {

  private XMLHandler myXMLHandler;

  @BeforeEach
  void setUp() {
    myXMLHandler = new XMLHandler("src/main/resources/ExampleXMLs/GameOfLifeExample.xml");
  }

  @Test
  void getType() {
    assertEquals(myXMLHandler.getType(), "GameOfLife");
  }

  @Test
  void getTitle() {
    assertEquals(myXMLHandler.getTitle(), "Glider");
  }

  @Test
  void getAuthor() {
    assertEquals(myXMLHandler.getAuthor(), "Richard K. Guy");
  }

  @Test
  void getDescription() {
    assertEquals(myXMLHandler.getDescription(), "A basic configuration that produces a glider that moves diagonally across the grid");
  }

  @Test
  void getGridDimensions() {
    assertEquals(myXMLHandler.getGridHeight(), 10);
    assertEquals(myXMLHandler.getGridWidth(), 10);
  }

  @Test
  void getGrid() {
  }

  @Test
  void getSimData() {
    assertEquals(myXMLHandler.getSimData().type(), "GameOfLife");
    assertEquals(myXMLHandler.getSimData().name(), "Glider");
    assertEquals(myXMLHandler.getSimData().author(), "Richard K. Guy");
    assertEquals(myXMLHandler.getSimData().description(), "A basic configuration that produces a glider that moves diagonally across the grid");
  }

  @Test
  void getSim() {
    assertThat(myXMLHandler.getSim().getRules(), instanceOf(GameOfLifeRules.class));
    assertEquals(myXMLHandler.getSim().getData().type(), "GameOfLife");
    assertEquals(myXMLHandler.getSim().getData().name(), "Glider");
    assertEquals(myXMLHandler.getSim().getData().author(), "Richard K. Guy");
    assertEquals(myXMLHandler.getSim().getData().description(), "A basic configuration that produces a glider that moves diagonally across the grid");
  }

  @Test
  void getParams() {
    assertEquals(myXMLHandler.getParams(), new HashMap<String, Double>());
  }
}