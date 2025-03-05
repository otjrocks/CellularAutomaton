package cellsociety.utility;

import javafx.scene.paint.Color;

/**
 * A utility class to help with handling colors in javafx
 *
 * @author Owen Jennings
 */
public class ColorUtility {
  // I used ChatGPT to assist in writing these methods.

  /**
   * Get the color string that is css compatible from a javafx color
   *
   * @param color The javafx color
   * @return The web supported color string
   */
  public static String getWebColorString(Color color) {
    return String.format("#%02X%02X%02X",
        (int) (color.getRed() * 255),
        (int) (color.getGreen() * 255),
        (int) (color.getBlue() * 255));
  }
}

