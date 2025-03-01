package cellsociety.view.cell;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * A rectangle implementation of a cell view
 *
 * @author Owen Jennings
 */
public class RectangleCellView extends CellView {

  /**
   * Create a rectangle cell view
   *
   * @param x      :      x location of cell
   * @param y      :      y location of cell
   * @param width  :      width of cell
   * @param height :      height of cell
   */
  public RectangleCellView(double x, double y, double width, double height) {
    super(width, height, width, height);
  }

  @Override
  protected Shape createShape(double width, double height) {
    return new Rectangle(width, height);
  }
}
