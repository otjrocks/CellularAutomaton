package cellsociety.model.simulation;


import java.util.ArrayList;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimulationDataTest {
  private SimulationMetaData simulationData;
  private ArrayList<Color> colors;

  @BeforeEach
  void setUp() {
    colors = new ArrayList<>();
    colors.add(Color.RED);
    colors.add(Color.BLUE);
    colors.add(Color.GREEN);

    simulationData = new SimulationMetaData("GameOfLife", "GOL Test", "Justin", "Justin's version", colors);
  }
  @Test
  void testGetters() {
    assertEquals("GameOfLife", simulationData.getType());
    assertEquals("GOL Test", simulationData.getName());
    assertEquals("Justin", simulationData.getAuthor());
    assertEquals("Justin's version", simulationData.getDescription());
    assertEquals(colors, simulationData.getColors());
  }

  @Test
  void testSetters() {
    simulationData.setType("NewGameOfLife");
    assertEquals("NewGameOfLife", simulationData.getType());

    simulationData.setName("GOLN Test");
    assertEquals("GOLN Test", simulationData.getName());

    simulationData.setAuthor("Justin2");
    assertEquals("Justin2", simulationData.getAuthor());

    simulationData.setDescription("Justin's second version");
    assertEquals("Justin's second version", simulationData.getDescription());

    colors.add(Color.YELLOW);
    simulationData.setColors(colors);
    assertEquals(colors, simulationData.getColors());
  }
}