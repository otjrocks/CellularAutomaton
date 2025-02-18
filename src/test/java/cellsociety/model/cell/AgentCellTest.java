package cellsociety.model.cell;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AgentCellTest {

  private AgentCell agentCell;

  @BeforeEach
  void setUp() {
    agentCell = new AgentCell(2, new Double(2, 2), 5, 1, 10);
  }

  @Test
  void constructor_testAgentCellThrowsExceptionForNegativeValues_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> new AgentCell(1, new Point2D.Double(0, 0), -1, 1, 1),
        "Should throw exception for negative vision");

    assertThrows(IllegalArgumentException.class, () -> new AgentCell(1, new Point2D.Double(0, 0), 1, -1, 1),
        "Should throw exception for negative metabolism");

    assertThrows(IllegalArgumentException.class, () -> new AgentCell(1, new Point2D.Double(0, 0), 1, 1, -1),
        "Should throw exception for negative sugar");
  }

  @Test
  void getVision_normalVision_returnsVision() {
    assertEquals(5, agentCell.getVision());
  }

  @Test
  void getMetabolism_normalMetabolism_returnsMetabolism() {
    assertEquals(1, agentCell.getMetabolism());
  }

  @Test
  void getSugar_normalSugar_returnsSugar() {
    assertEquals(10, agentCell.getSugar());
  }

}