package cellsociety.view.cell;

import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

/**
 * A cell view used in the triangle grid view.
 *
 * @author Owen Jennings
 */
public class TriangleCellView extends CellView {

  /**
   * Create a cell with the default fill color.
   *
   * @param x      :      x location of cell
   * @param y      :      y location of cell
   * @param width  :  width of cell
   * @param height : height of cell
   */
  public TriangleCellView(double x, double y, double width, double height) {
    super(x, y, width, height);
  }

  @Override
  protected Shape createShape(double width, double height) {
    // I asked ChatGPT for assistance in creating an isometric triangle here.
    Polygon triangle = new Polygon();
    double centerX = width / 2;
    double topY = 0;

    triangle.getPoints().addAll(
        centerX, topY,
        0.0, height,
        width, height
    );
    return triangle;
  }
}
