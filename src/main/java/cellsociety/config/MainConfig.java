package cellsociety.config;

import java.util.ResourceBundle;

public class MainConfig {

  public static final String LANGUAGE_FILE_PATH = "cellsociety.languages.";
  private static final String DEFAULT_LANGUAGE = "English";

  private static ResourceBundle myMessages = ResourceBundle.getBundle(
      LANGUAGE_FILE_PATH + DEFAULT_LANGUAGE);

  public static ResourceBundle getMessages() {
    return myMessages;
  }

  public static void setLanguage(String language) {
    myMessages = ResourceBundle.getBundle(LANGUAGE_FILE_PATH + language);
  }


  public static final String TITLE = getMessages().getString("TITLE");
  public static final int WIDTH = 1400;
  public static final int HEIGHT = 800;
  public static final String DEFAULT_RESOURCE_PACKAGE = "cellsociety.";
  public static final String DEFAULT_RESOURCE_FOLDER =
      "/" + DEFAULT_RESOURCE_PACKAGE.replace(".", "/");
  public static final String STYLESHEET_PATH = DEFAULT_RESOURCE_FOLDER + "styles.css";
  public static final String DEFAULT_FONT_PATH = DEFAULT_RESOURCE_FOLDER + "fonts.default.ttf";
  public static final String BOLD_FONT_PATH = DEFAULT_RESOURCE_FOLDER + "fonts.bold.ttf";

  public static final int MARGIN = 20;
  public static final int GRID_WIDTH = (2 * WIDTH) / 3;
  public static final int GRID_HEIGHT = HEIGHT - (2 * MARGIN);
  public static final double STEP_SPEED = 0.5;
  public static final int MAX_GRID_NUM_ROWS = 150;
  public static final int MAX_GRID_NUM_COLS = 150;
  public static final int MIN_GRID_NUM_ROWS = 5;
  public static final int MIN_GRID_NUM_COLS = 5;
  public static final int SIDEBAR_WIDTH = WIDTH - GRID_WIDTH - (3 * MARGIN);

  public static final boolean VERBOSE_ERROR_MESSAGES = true; // determine if error messages should be simple or display more details to user
}
