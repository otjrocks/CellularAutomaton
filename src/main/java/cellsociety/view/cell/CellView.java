package cellsociety.view.cell;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;

/**
 * A view used to create a cell. This view is abstract to allow for various shapes/images to
 * represent a cell
 *
 * @author Owen Jennings
 */
public abstract class CellView extends Group {

  public static final Paint DEFAULT_COLOR = Color.WHITE;
  public static final Paint BORDER_COLOR = Color.BLACK;
  public static final double BORDER_WIDTH = 0.6;

  private final Shape myShape;

  /**
   * Create a cell with the default fill color
   *
   * @param x:      x location of cell
   * @param y:      y location of cell
   * @param width:  width of cell
   * @param height: height of cell
   */
  public CellView(double x, double y, double width, double height) {
    myShape = createShape(width, height);
    myShape.setFill(DEFAULT_COLOR);
    myShape.setStroke(BORDER_COLOR);
    myShape.setStrokeType(StrokeType.INSIDE);
    myShape.setStrokeWidth(BORDER_WIDTH);
    this.getChildren().add(myShape);
    this.getStyleClass().add("cell-view");
    this.setLayoutX(x);
    this.setLayoutY(y);
  }

  /**
   * Handle whether grid lines should be shown or not
   *
   * @param selected: Whether to show grid lines
   */
  public void setGridLines(boolean selected) {
    if (selected) {
      myShape.setStrokeWidth(BORDER_WIDTH);
    } else {
      myShape.setStrokeWidth(0);
    }
  }

  /**
   * Set the fill color for a cell
   *
   * @param color: The color you wish to set
   */
  public void setFill(Paint color) {
    myShape.setFill(color);
  }

  /**
   * Get the fill color for a cell
   *
   * @return: the color of the fill
   */
  public Color getFill() {
    return (Color) myShape.getFill();
  }

  /**
   * Reset the stroke color of the cell on theme updates
   */
  public void resetStrokeColor() {
    this.setStyle("-fx-stroke: -fx-primary;");
  }

  /**
   * Create the shape used in this view. It should be defined in the implementation of a cell view
   *
   * @return The specific Shape instance for the cell
   */
  protected abstract Shape createShape(double width, double height);
}
