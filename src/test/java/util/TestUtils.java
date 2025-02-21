package util;

import static cellsociety.config.MainConfig.getMessage;
import static org.junit.Assert.assertEquals;

import javafx.scene.control.Button;
import javafx.scene.text.Text;

/**
 * Static helper methods for common testing tasks
 */
public class TestUtils extends DukeApplicationTest {

  /**
   * Helper method to verify if a specific Text node matches expected text
   *
   * @param fxId       the FX ID of the Text node
   * @param messageKey the message key from config
   */
  public void verifyText(String fxId, String messageKey) {
    Text textNode = lookup(fxId).queryAs(Text.class);
    assertEquals(getMessage(messageKey), textNode.getText());
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

}
