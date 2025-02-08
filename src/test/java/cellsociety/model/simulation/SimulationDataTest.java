package cellsociety.model.simulation;


import static org.junit.Assert.assertEquals;

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

    simulationData = new SimulationMetaData("GameOfLife", "GOL Test", "Justin", "Justin's version");
  }
  @Test
  void testGetters() {
    assertEquals("GameOfLife", simulationData.type());
    assertEquals("GOL Test", simulationData.name());
    assertEquals("Justin", simulationData.author());
    assertEquals("Justin's version", simulationData.description());
  }

}