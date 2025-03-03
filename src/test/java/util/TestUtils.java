package util;

import static cellsociety.config.MainConfig.getMessage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import cellsociety.model.Grid;
import cellsociety.model.cell.DefaultCell;
import cellsociety.view.components.AlertField;
import java.awt.geom.Point2D.Double;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

/**
 * Static helper methods for common testing tasks
 */
public class TestUtils extends DukeApplicationTest {

  /**
   * Helper method to verify if a specific Text node matches expected text
   *
   * @param fxId    the FX ID of the Text node
   * @param message the message you are expecting
   */
  public void verifyText(String fxId, String message) {
    Text textNode = lookup(fxId).queryAs(Text.class);
    assertEquals(message, textNode.getText());
  }

  /**
   * Helper method to verify if a specific Button node matches expected text
   *
   * @param fxId       the FX ID of the Button
   * @param messageKey the message key from config
   */
  public void verifyButtonText(String fxId, String messageKey) {
    Button button = lookup(fxId).queryAs(Button.class);
    assertEquals(getMessage(messageKey), button.getText());
  }

  /**
   * Helper method to verify the alert field contains a message in its queue
   *
   * @param alertField The alert field
   * @param message    The expected message
   */
  public void verifyAlertFieldContains(AlertField alertField, String message) {
    assertTrue(alertField.getMessages().contains(message));
  }

  /**
   * Initialize a grid with cells with all state 0
   *
   * @param grid The grid you want to initialize
   */
  public static void initializeEmptyGrid(Grid grid) {
    for (int i = 0; i < grid.getRows(); i++) {
      for (int j = 0; j < grid.getCols(); j++) {
        grid.addCell(new DefaultCell(0, new Double(i, j)));
      }
    }
  }

}
