package cellsociety.controller;

import static cellsociety.config.MainConfig.LOGGER;

import java.util.prefs.Preferences;
import org.apache.logging.log4j.Logger;

/**
 * A controller to handle the saving and accessing of the user's preferences file.
 *
 * @author Owen Jennings
 */
public class PreferencesController {

  private static final Preferences preferences = Preferences.userNodeForPackage(
      PreferencesController.class);

  /**
   * Saves a preference value as a string.
   *
   * @param key   the preference key
   * @param value the value to save as a string
   */
  public static void setPreference(String key, String value) {
    preferences.put(key, value);
    try {
      preferences.flush(); // Ensure the preference is written to storage
    } catch (Exception e) {
      LOGGER.warn("Error saving preferences: {}", e.getMessage());
    }
  }

  /**
   * Retrieves a preference value as a string.
   *
   * @param key          the preference key
   * @param defaultValue the default value if the preference is not found
   * @return the retrieved preference value as a string
   */
  public static String getPreference(String key, String defaultValue) {
    return preferences.get(key, defaultValue);
  }
}
