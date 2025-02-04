package cellsociety.config;

import java.util.ResourceBundle;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class MainConfig {
  // Language configuration
  public static final String LANGUAGE_FILE_PATH = "cellsociety.languages.";
  public static final String CURRENT_LANGUAGE = "English";
  public static final String LANGUAGE_PATH = LANGUAGE_FILE_PATH + CURRENT_LANGUAGE;
  public static final ResourceBundle MESSAGES = ResourceBundle.getBundle(LANGUAGE_PATH);

  public static final String TITLE = MESSAGES.getString("TITLE");
  public static final int WIDTH = 1400;
  public static final int HEIGHT = 800;
  public static final Paint BACKGROUND_COLOR = Color.LIGHTGRAY;
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
