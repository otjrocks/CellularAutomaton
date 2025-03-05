package cellsociety.view.grid;

import cellsociety.controller.MainController;
import cellsociety.view.cell.CellView;
import cellsociety.view.cell.HexagonCellView;

/**
 * A hexagon grid implementation.
 *
 * @author Owen Jennings
 */
public class HexagonGridView extends GridView {
  // I asked ChatGPT for assistance in writing this class
  // I modified multiple of the constants and applied math ceiling to ensure that grid fits within provided area on screen and does not overflow area
  // getRow and getColumn translate a point into our vertical and horizontal spacing to get correct row and column for provided point

  /**
   * Create a grid view.
   *
   * @param width          Width of the view
   * @param height         Height of the view
   * @param numRows        Number of rows in the grid
   * @param numColumns     Number of cells per row in the grid
   * @param mainController The main controller of the program
   */
  public HexagonGridView(int width, int height, int numRows, int numColumns,
      MainController mainController) {
    super(width, height, numRows, numColumns, mainController);
  }

  @Override
  protected CellView[][] initializeGrid() {
    double hexWidth = getHexagonWidth();
    double hexHeight = getHexagonHeight();
    double horizontalSpacing = hexWidth * 0.75; // Horizontal spacing
    double verticalSpacing = hexHeight * Math.sqrt(3) / 2.0; // Vertical spacing for hexagons
    CellView[][] grid = new HexagonCellView[getNumRows()][getNumColumns()];
    initializeAllCellViews(horizontalSpacing, verticalSpacing, grid, hexWidth, hexHeight);
    return grid;
  }

  private void initializeAllCellViews(double horizontalSpacing, double verticalSpacing,
      CellView[][] grid,
      double hexWidth, double hexHeight) {
    for (int row = 0; row < getNumRows(); row++) {
      for (int column = 0; column < getNumColumns(); column++) {
        createHexagonCell(column, horizontalSpacing, row, verticalSpacing, grid, hexWidth,
            hexHeight);
      }
    }
  }

  private static void createHexagonCell(int column, double horizontalSpacing, int row,
      double verticalSpacing, CellView[][] grid, double hexWidth, double hexHeight) {
    double x = column * horizontalSpacing;
    double y = row * verticalSpacing;
    if (isOdd(column)) {
      y += verticalSpacing / 2; // Shift odd columns downward to create honeycomb effect
    }
    grid[row][column] = new HexagonCellView(x, y, hexWidth, hexHeight);
  }

  private static boolean isOdd(int value) {
    return value % 2 == 1;
  }

  private double getHexagonHeight() {
    return (double) getHeight() / Math.ceil(getNumColumns() * Math.sqrt(3) / 2.0);
  }

  private double getHexagonWidth() {
    return (double) getWidth() / (Math.ceil(
        getNumColumns() * 0.75));
  }
}
