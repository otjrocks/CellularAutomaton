package cellsociety.view.components;

import static cellsociety.config.MainConfig.MARGIN;
import static cellsociety.config.MainConfig.SIDEBAR_WIDTH;
import static cellsociety.config.MainConfig.getMessage;

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

  /**
   * Initialize the alert field
   */
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
    String alertPrefix = getAlertPrefix(isWarning);
    Text newMessage = createAlertText(message, isWarning, alertPrefix);
    this.getChildren().add(newMessage);
    handleAlertDismissalTimeline(newMessage);
  }

  private void handleAlertDismissalTimeline(Text newMessage) {
    // Remove message after a predetermined amount of time
    Timeline timeline = new Timeline(
        new KeyFrame(Duration.seconds(ALERT_DURATION_SECONDS), _ -> getChildren().remove(
            newMessage)));
    timeline.setCycleCount(1);
    timeline.play();
  }

  private static Text createAlertText(String message, boolean isWarning, String alertPrefix) {
    Text newMessage = new Text(String.format(alertPrefix, message));
    newMessage.setTextAlignment(TextAlignment.LEFT);
    if (isWarning) {
      newMessage.getStyleClass().add("alert-warning");
    } else {
      newMessage.getStyleClass().add("alert-info");
    }
    newMessage.setWrappingWidth(SIDEBAR_WIDTH - (MARGIN * 2));
    return newMessage;
  }

  private static String getAlertPrefix(boolean isWarning) {
    return isWarning ? getMessage("WARNING_PREFIX") : getMessage("INFO_PREFIX");
  }
}
