package cellsociety.view.cell;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * A rectangle implementation of a cell view
 *
 * @author Owen Jennings
 */
public class RectangleCellView extends CellView {

  public RectangleCellView(int width, int height, int cellWidth, int cellHeight) {
    super(width, height, cellWidth, cellHeight);
  }

  @Override
  protected Shape createShape(int width, int height) {
    return new Rectangle(width, height);
  }
}
