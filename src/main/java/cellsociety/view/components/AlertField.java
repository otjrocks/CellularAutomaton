package cellsociety.view.components;

import static cellsociety.config.MainConfig.SIDEBAR_WIDTH;
import static cellsociety.config.MainConfig.getMessages;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

/**
 * A view to handle the display of alert messages with automatic removal.
 */
public class AlertField extends VBox {

  public static final int HISTORY_SIZE = 5; // Maximum number of alerts to display, regardless of if alert timeline expires
  public static final int ALERT_DURATION_SECONDS = 5; // Time before an alert disappears

  public AlertField() {
    setSpacing(5); // Adds spacing between messages
  }

  /**
   * Flash a message in the alert area
   *
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
    String alertPrefix =
        isWarning ? getMessages().getString("WARNING_PREFIX") : getMessages().getString("INFO_PREFIX");
    Text newMessage = new Text(String.format(alertPrefix, message));
    newMessage.setTextAlignment(TextAlignment.CENTER);
    if (isWarning) {
      newMessage.getStyleClass().add("alert-warning");
    } else {
      newMessage.getStyleClass().add("alert-info");
    }
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
