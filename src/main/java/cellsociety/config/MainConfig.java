package cellsociety.config;

import java.util.ResourceBundle;

import cellsociety.controller.PreferencesController;
import cellsociety.view.grid.GridViewFactory.CellShapeType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainConfig {

  public static final String LANGUAGE_FILE_PATH = "cellsociety.languages.";
  public static final String COLOR_CONFIG_FILE = "cellsociety.colors.CellColors";
  private static final String DEFAULT_LANGUAGE = "English";
  private static final String INITIAL_LANGUAGE = PreferencesController.getPreference("language",
      DEFAULT_LANGUAGE);

  private static ResourceBundle myMessages = ResourceBundle.getBundle(
      LANGUAGE_FILE_PATH + INITIAL_LANGUAGE);

  public static String getMessage(String key) {
    try {
      return myMessages.getString(key);
    } catch (Exception e) {
      // queries key does not exist in language file or trouble finding messages file
      // return a default string
      try {
        // try displaying to user that key is missing in their preferred language, fallback to english
        return myMessages.getString("MISSING_KEY");
      } catch (Exception e1) {
        return "ERROR: MISSING KEY";
      }
    }
  }

  public static void setLanguage(String language) {
    try {
      myMessages = ResourceBundle.getBundle(LANGUAGE_FILE_PATH + language);
    } catch (Exception e) {
      throw new IllegalArgumentException("Language file not found");
    }
    PreferencesController.setPreference("language", language);
  }

  private static final ResourceBundle myCellColors = ResourceBundle.getBundle(COLOR_CONFIG_FILE);

  public static ResourceBundle getCellColors() {
    return myCellColors;
  }

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
  // The logger for this program
  public static final Logger LOGGER = LogManager.getLogger();

}
