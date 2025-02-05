package cellsociety.model.xmlhandling;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import cellsociety.model.XMLHandlers.XMLDefiner;
import cellsociety.model.XMLHandlers.XMLHandler;
import cellsociety.model.XMLHandlers.types.GameOfLifeXMLHandler;
import cellsociety.model.XMLHandlers.types.PercolationXMLHandler;
import cellsociety.model.XMLHandlers.types.SegregationXMLHandler;
import cellsociety.model.XMLHandlers.types.SpreadingOfFireXMLHandler;
import cellsociety.model.XMLHandlers.types.WWXMLHandler;

class XMLDefinerTest {

  @Test
  void GameOfLifeDefinition() throws SAXException, ParserConfigurationException, IOException {
    XMLHandler TestGoL = XMLDefiner.createHandler("src/main/resources/ExampleXMLs/GameOfLifeExample.xml");
    assertThat(TestGoL, instanceOf(GameOfLifeXMLHandler.class));
  }

  @Test
  void PercolationDefinition() throws SAXException, ParserConfigurationException, IOException {
    XMLHandler TestP = XMLDefiner.createHandler("src/main/resources/ExampleXMLs/PercolationExample.xml");
    assertThat(TestP, instanceOf(PercolationXMLHandler.class));
  }

  @Test
  void SpreadingOfFireDefinition() throws SAXException, ParserConfigurationException, IOException {
    XMLHandler TestSoF = XMLDefiner.createHandler("src/main/resources/ExampleXMLs/SpreadingOfFireExample.xml");
    assertThat(TestSoF, instanceOf(SpreadingOfFireXMLHandler.class));
  }

  @Test
  void SegregationDefinition() throws SAXException, ParserConfigurationException, IOException {
    XMLHandler TestS = XMLDefiner.createHandler("src/main/resources/ExampleXMLs/SegregationExample.xml");
    assertThat(TestS, instanceOf(SegregationXMLHandler.class));
  }

  @Test
  void WaTorWorldDefinition() throws SAXException, ParserConfigurationException, IOException {
    XMLHandler TestWW = XMLDefiner.createHandler("src/main/resources/ExampleXMLs/WaTorWorldExample.xml");
    assertThat(TestWW, instanceOf(WWXMLHandler.class));
  }
  
}