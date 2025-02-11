package cellsociety.model.xmlhandling;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
  void getGridDimensions_validVariables_returnTrue() {
    assertEquals(myXMLHandler.getGridHeight(), 10);
    assertEquals(myXMLHandler.getGridWidth(), 10);
  }

  @Test
  void getSimData_validVariables_returnTrue() {
    assertEquals(myXMLHandler.getSimData().type(), "GameOfLife");
    assertEquals(myXMLHandler.getSimData().name(), "Glider");
    assertEquals(myXMLHandler.getSimData().author(), "Richard K. Guy");
    assertEquals(myXMLHandler.getSimData().description(), "A basic configuration that produces a glider that moves diagonally across the grid");
  }

  @Test
  void getSim_validVariables_returnTrue() {
    assertThat(myXMLHandler.getSim().getRules(), instanceOf(GameOfLifeRules.class));
    assertEquals(myXMLHandler.getSim().getData().type(), "GameOfLife");
    assertEquals(myXMLHandler.getSim().getData().name(), "Glider");
    assertEquals(myXMLHandler.getSim().getData().author(), "Richard K. Guy");
    assertEquals(myXMLHandler.getSim().getData().description(), "A basic configuration that produces a glider that moves diagonally across the grid");
  }

  @Test
  void getParams_validVariables_returnTrue() {
    assertEquals(myXMLHandler.getParams(), new HashMap<String, Double>());
  }

  @Test
  void XMLHandler_loadInNonexistantFile_throwsExceptionBasedOnAlert(){
    assertThrows(ExceptionInInitializerError.class, 
        () -> {
          XMLHandler handler = new XMLHandler("reallyreallycool.xml");
        });
    
  }

  @Test
  void XMLHandler_loadInPoorlyFormattedFile_throwsExceptionBasedOnAlert(){
    assertThrows(ExceptionInInitializerError.class, 
        () -> {
          XMLHandler handler = new XMLHandler("src/main/resources/TestXMLs/PoorlyFormatted.xml");
        });
    
  }

  @Test
  void XMLHandler_loadInFileWithMissingFields_throwsExceptionBasedOnAlert(){
    assertThrows(ExceptionInInitializerError.class, 
        () -> {
          XMLHandler handler = new XMLHandler("src/main/resources/TestXMLs/MissingFields.xml");
        });
    
  }

  @Test
  void XMLHandler_loadInFileWithIncorrectArgumentType_throwsExceptionBasedOnAlert(){
    assertThrows(ExceptionInInitializerError.class, 
        () -> {
          XMLHandler handler = new XMLHandler("src/main/resources/TestXMLs/LetterGrid.xml");
        });
    
  }

  @Test
  void XMLHandler_loadInFileWithGridTooTall_throwsExceptionBasedOnAlert(){
    assertThrows(ExceptionInInitializerError.class, 
        () -> {
          XMLHandler handler = new XMLHandler("src/main/resources/TestXMLs/GridTooTall.xml");
        });
    
  }

  @Test
  void XMLHandler_loadInFileWithGridTooWide_throwsExceptionBasedOnAlert(){
    assertThrows(ExceptionInInitializerError.class, 
        () -> {
          XMLHandler handler = new XMLHandler("src/main/resources/TestXMLs/GridTooWide.xml");
        });
    
  }
}
