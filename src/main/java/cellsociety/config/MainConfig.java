package cellsociety.config;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class MainConfig {

  public static final String TITLE = "Cell Society";
  public static final int WIDTH = 1400;
  public static final int HEIGHT = 800;
  public static final Paint BACKGROUND_COLOR = Color.LIGHTGRAY;
  public static final int MARGIN = 20;
  public static final int GRID_WIDTH = (2 * WIDTH) / 3;
  public static final int GRID_HEIGHT = HEIGHT - (2 * MARGIN);
  public static final double STEP_SPEED = 0.5;
}
