package cellsociety.model.simulation;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimulationDataTest {
  private SimulationMetaData simulationData;

  @BeforeEach
  void setUp() {
    ArrayList<Color> colors = new ArrayList<>();
    colors.add(Color.RED);
    colors.add(Color.BLUE);
    colors.add(Color.GREEN);

    simulationData = new SimulationMetaData("GameOfLife", "GOL Test", "Justin", "Justin's version");
  }
  @Test
  void testGetters() {
    Assertions.assertEquals("GameOfLife", simulationData.type());
    Assertions.assertEquals("GOL Test", simulationData.name());
    Assertions.assertEquals("Justin", simulationData.author());
    Assertions.assertEquals("Justin's version", simulationData.description());
  }

}