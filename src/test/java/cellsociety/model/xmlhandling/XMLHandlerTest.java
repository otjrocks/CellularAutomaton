package cellsociety.model.xmlhandling;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import cellsociety.model.simulation.rules.GameOfLifeRules;
import cellsociety.model.xml.GridException;
import cellsociety.model.xml.InvalidStateException;
import cellsociety.model.xml.XMLHandler;

class XMLHandlerTest {

  private XMLHandler myXMLHandler;

  @BeforeEach
  void setUp() {
    try {
      myXMLHandler = new XMLHandler("src/main/resources/simulations/ExampleXMLs/GameOfLifeExample.xml");
    } catch (Exception e) {
    }
  }

  @Test
  void getGridDimensions_validVariables_returnTrue() {
    assertEquals(10, myXMLHandler.getGridHeight());
    assertEquals(10, myXMLHandler.getGridWidth());
  }

  @Test
  void getSimData_validVariables_returnTrue() {
    assertEquals("GameOfLife", myXMLHandler.getSimData().type());
    assertEquals("Glider", myXMLHandler.getSimData().name());
    assertEquals("Richard K. Guy", myXMLHandler.getSimData().author());
    assertEquals(
        "A basic configuration that produces a glider that moves diagonally across the grid",
        myXMLHandler.getSimData().description());
  }

  @Test
  void getSim_validVariables_returnTrue() {
    assertInstanceOf(GameOfLifeRules.class, myXMLHandler.getSim().rules());
    assertEquals("GameOfLife", myXMLHandler.getSim().data().type());
    assertEquals("Glider", myXMLHandler.getSim().data().name());
    assertEquals("Richard K. Guy", myXMLHandler.getSim().data().author());
    assertEquals(
        "A basic configuration that produces a glider that moves diagonally across the grid",
        myXMLHandler.getSim().data().description());
  }

  @Test
  void getParams_validVariables_returnTrue() {
    assertEquals(0, myXMLHandler.getParams().size());
  }

  @Test
  void XMLHandler_loadInNonExistentFile_throwsIOException() {
    assertThrows(IOException.class,
        () -> new XMLHandler("reallyreallycool.xml"));

  }

  @Test
  void XMLHandler_loadInPoorlyFormattedFile_throwsSAXException() {
    assertThrows(SAXException.class,
        () -> new XMLHandler("src/main/resources/simulations/TestXMLs/PoorlyFormatted.xml"));

  }

  @Test
  void XMLHandler_loadInFileWithMissingFields_throwsInvalidStateException() {
    assertThrows(InvalidStateException.class,
        () -> new XMLHandler("src/main/resources/simulations/TestXMLs/MissingFields.xml"));

  }

  @Test
  void XMLHandler_loadInFileWithIncorrectArgumentType_throwsNumberFormatException() {
    assertThrows(NumberFormatException.class,
        () -> new XMLHandler("src/main/resources/simulations/TestXMLs/LetterGrid.xml"));

  }

  @Test
  void XMLHandler_loadInFileWithGridTooTall_throwsGridException() {
    assertThrows(GridException.class,
        () -> new XMLHandler("src/main/resources/simulations/TestXMLs/GridTooTall.xml"));

  }

  @Test
  void XMLHandler_loadInFileWithGridTooWide_throwsGridException() {
    assertThrows(GridException.class,
        () -> new XMLHandler("src/main/resources/simulations/TestXMLs/GridTooWide.xml"));

  }
}
