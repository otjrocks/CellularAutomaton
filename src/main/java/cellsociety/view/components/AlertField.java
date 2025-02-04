package cellsociety.view.components;

import static cellsociety.config.MainConfig.SIDEBAR_WIDTH;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * A view to handle the display of alert messages with automatic removal.
 */
public class AlertField extends VBox {

  public static final Color ALERT_COLOR = Color.RED;
  public static final Color INFO_COLOR = Color.CADETBLUE;
  public static final int HISTORY_SIZE = 5; // Maximum number of alerts to display, regardless of if alert timeline expires
  public static final int ALERT_DURATION_SECONDS = 5; // Time before an alert disappears

  public AlertField() {
    setSpacing(5); // Adds spacing between messages
  }

  /**
   * Flash a message in the alert area
   * @param message: The message you wish to alert the user with
   * @param warning: denotes if the alert is a warning or just an informational alert
   */
  public void flash(String message, boolean warning) {
    if (getChildren().size() >= HISTORY_SIZE) {
      getChildren().removeFirst(); // Remove the oldest message, regardless of if time has expired
    }
    createNewAlertMessage(message, warning);
  }

  private void createNewAlertMessage(String message, boolean isWarning) {
    String alertPrefix = isWarning ? "Warning: " : "Info: ";
    Text newMessage = new Text(alertPrefix + message);
    if (isWarning) {
      newMessage.setFill(ALERT_COLOR);
    } else {
      newMessage.setFill(INFO_COLOR);
    }
    newMessage.setFont(new Font("Arial", 14));
    newMessage.setWrappingWidth(SIDEBAR_WIDTH);
    this.getChildren().add(newMessage);

    // Remove message after a predetermined amount of time
    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(ALERT_DURATION_SECONDS), e -> {
      getChildren().remove(newMessage);
    }));
    timeline.setCycleCount(1);
    timeline.play();
  }
}
