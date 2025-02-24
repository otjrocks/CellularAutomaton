package cellsociety.view.cell;

import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class HexagonCellView extends CellView{

  /**
   * Create a cell with the default fill color
   *
   * @param x      :      x location of cell
   * @param y      :      y location of cell
   * @param width  :  width of cell
   * @param height : height of cell
   */
  public HexagonCellView(int x, int y, int width, int height) {
    super(x, y, width, height);
  }

  @Override
  protected Shape createShape(int width, int height) {
    // I asked ChatGPT for assistance in creating a hexagon
    Polygon hexagon = new Polygon();
    double centerX = width / 2.0;
    double centerY = height / 2.0;
    double radiusX = width / 2.0;
    double radiusY = height / 2.0;

    // Create 6 points for the hexagon
    for (int i = 0; i < 6; i++) {
      double angle = Math.toRadians(60 * i - 30); // Flat-top hexagon
      double pointX = centerX + radiusX * Math.cos(angle);
      double pointY = centerY + radiusY * Math.sin(angle);
      hexagon.getPoints().addAll(pointX, pointY);
    }
    return hexagon;
  }
}
