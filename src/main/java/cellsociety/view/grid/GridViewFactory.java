package cellsociety.view.grid;

import cellsociety.controller.MainController;

/**
 * A factory to create grid views based on a cell type
 */
public class GridViewFactory {

  public enum CellShapeType {
    RECTANGLE("Rectangle"),
    HEXAGON("Hexagon"),
    TRIANGLE("Triangle");

    private final String displayName;

    CellShapeType(String displayName) {
      this.displayName = displayName;
    }

    // Override toString() to return display name
    @Override
    public String toString() {
      return displayName;
    }
  }


  /**
   * Factory method to create a GridView object based on the provided shape type and initialization
   * information
   *
   * @param shapeType: Type of cell shape (RECTANGLE, HEXAGON, TRIANGLE.)
   * @param width:     Width of the cell
   * @param height:    Height of the cell
   * @return The correct cell view for the information provided
   */
  public static GridView createCellView(CellShapeType shapeType, int width, int height, int numRows,
      int numColumns,
      MainController mainController) {
    return switch (shapeType) {
      case RECTANGLE -> new RectangleGridView(width, height, numRows, numColumns, mainController);
      case HEXAGON -> new HexagonGridView(width, height, numRows, numColumns, mainController);
      case TRIANGLE -> new TriangleGridView(width, height, numRows, numColumns, mainController);
    };
  }
}
