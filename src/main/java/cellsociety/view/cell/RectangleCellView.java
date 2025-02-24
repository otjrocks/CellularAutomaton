package cellsociety.view.cell;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * A rectangle implementation of a cell view
 *
 * @author Owen Jennings
 */
public class RectangleCellView extends CellView {

  public RectangleCellView(double width, double height, double cellWidth, double cellHeight) {
    super(width, height, cellWidth, cellHeight);
  }

  @Override
  protected Shape createShape(double width, double height) {
    return new Rectangle(width, height);
  }
}
