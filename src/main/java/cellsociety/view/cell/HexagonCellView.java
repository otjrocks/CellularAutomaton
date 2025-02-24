package cellsociety.view.cell;

import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class HexagonCellView extends CellView {

  /**
   * Create a cell with the default fill color
   *
   * @param x      :      x location of cell
   * @param y      :      y location of cell
   * @param width  :      width of cell
   * @param height :      height of cell
   */
  public HexagonCellView(int x, int y, int width, int height) {
    super(x, y, width, height);
  }

  @Override
  protected Shape createShape(int width, int height) {
    Polygon hexagon = new Polygon();

    // Calculate the radius of the hexagon based on width and height
    double radiusX = width / 2.0;  // The radius in the x-direction (half of width)
    double radiusY = height / 2.0; // The radius in the y-direction (half of height)

    // Calculate the angle for each of the 6 vertices of the hexagon
    for (int i = 0; i < 6; i++) {
      double angle = Math.toRadians(60 * i);  // Flat-top hexagon
      double pointX = radiusX + radiusX * Math.cos(angle);
      double pointY = radiusY + radiusY * Math.sin(angle);
      hexagon.getPoints().addAll(pointX, pointY);
    }

    return hexagon;
  }
}
