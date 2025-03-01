package cellsociety.config;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import cellsociety.controller.PreferencesController;
import cellsociety.view.grid.GridViewFactory.CellShapeType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The main config of the program
 *
 * @author Owen Jennings
 * @author Troy Ludwig
 * @author Justin Aronwald
 */
public class MainConfig {
  // CONSTANTS
  public static final String LANGUAGE_FILE_PATH = "cellsociety.languages.";
  public static final String COLOR_CONFIG_FILE = "cellsociety.colors.CellColors";
  private static final String DEFAULT_LANGUAGE = "English";
  private static final String INITIAL_LANGUAGE = PreferencesController.getPreference("language",
      DEFAULT_LANGUAGE);
  private static ResourceBundle myMessages = ResourceBundle.getBundle(
      LANGUAGE_FILE_PATH + INITIAL_LANGUAGE);
  public static final String TITLE = getMessage("TITLE");
  public static final int WIDTH = 1500;
  public static final int HEIGHT = 800;
  public static final int MARGIN = 20;
  public static final int GRID_WIDTH = (3 * WIDTH) / 5;
  public static final int GRID_HEIGHT = HEIGHT - (2 * MARGIN);
  public static final double INITIAL_STEP_SPEED = 0.5;
  public static final double STEP_SPEED =
      1 / Double.parseDouble(PreferencesController.getPreference("animationSpeed",
          String.valueOf(INITIAL_STEP_SPEED)));
  public static final int MAX_GRID_NUM_ROWS = 150;
  public static final int MAX_GRID_NUM_COLS = 150;
  public static final int MIN_GRID_NUM_ROWS = 5;
  public static final int MIN_GRID_NUM_COLS = 5;
  public static final int SIDEBAR_WIDTH = WIDTH - GRID_WIDTH - (3 * MARGIN);
  public static final CellShapeType DEFAULT_CELL_SHAPE = CellShapeType.RECTANGLE;
  public static final boolean VERBOSE_ERROR_MESSAGES = false; // determine if error messages should be simple or display more details to user
  public static final Logger LOGGER = LogManager.getLogger(); // The logger for this program
  private static final ResourceBundle myCellColors = ResourceBundle.getBundle(COLOR_CONFIG_FILE);

  /**
   * Get the message string from the config file for the provided key
   *
   * @param key The key you are looking for
   * @return The message specified in the language file for the provided key if it exists or a
   * missing key message
   */
  public static String getMessage(String key) {
    try {
      if (myMessages != null) {
        return myMessages.getString(key);
      } else {
        logMissingMessage(key);
        return "ERROR: MISSING KEY";
      }
    } catch (MissingResourceException e) {
      // queries key does not exist in language file or trouble finding messages file
      // return a default string
      try {
        // try displaying to user that key is missing in their preferred language, fallback to english
        return myMessages.getString("MISSING_KEY");
      } catch (MissingResourceException e1) {
        logMissingMessage(e.getMessage());
        return "ERROR: MISSING KEY";
      }
    }
  }

  /**
   * Set the language for the program if it exists. If the language does not exist, the preferred
   * language will not update.
   *
   * @param language: The new language to set
   */
  public static void setLanguage(String language) {
    try {
      myMessages = ResourceBundle.getBundle(LANGUAGE_FILE_PATH + language);
    } catch (Exception e) {
      LOGGER.error("Could not load language: {}", language);
      throw new IllegalArgumentException("Language file not found" + e);
    }
    PreferencesController.setPreference("language", language);
  }

  /**
   * Get the cell colors resource bundle
   *
   * @return A resource bundle containing all the cell colors
   */
  public static ResourceBundle getCellColors() {
    return myCellColors;
  }

  private static void logMissingMessage(String e) {
    if (LOGGER != null) {
      LOGGER.warn("Error loading a message: {}", e);
    }
  }
}
