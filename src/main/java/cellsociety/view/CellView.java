package cellsociety.view;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * A view used to create a cell
 *
 * @author Owen Jennings
 */
public class CellView extends Rectangle {

  public static final Paint DEFAULT_COLOR = Color.WHITE;
  public static final Paint BORDER_COLOR = Color.BLACK;
  public static final int BORDER_WIDTH = 2;

  /**
   * Create a cell with the default fill color
   *
   * @param x:      x location of cell
   * @param y:      y location of cell
   * @param width:  width of cell
   * @param height: height of cell
   */
  public CellView(int x, int y, int width, int height) {
    super(x, y, width, height);
    this.setFill(DEFAULT_COLOR);
    this.setStroke(BORDER_COLOR);
    this.setStrokeType(StrokeType.INSIDE);
    this.setStrokeWidth(BORDER_WIDTH);

  }

  /**
   * Create a cell with a specific fill color
   *
   * @param x:      x location of cell
   * @param y:      y location of cell
   * @param width:  width of cell
   * @param height: height of cell
   * @param fill:   fill color of cell
   */
  public CellView(int x, int y, int width, int height, Paint fill) {
    this(x, y, width, height);
    this.setFill(fill);
  }
}
