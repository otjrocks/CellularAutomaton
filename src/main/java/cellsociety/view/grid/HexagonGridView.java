package cellsociety.view.grid;

import cellsociety.controller.MainController;
import cellsociety.view.cell.CellView;
import cellsociety.view.cell.HexagonCellView;

public class HexagonGridView extends GridView {

  /**
   * Create a grid view
   *
   * @param width:          Width of the view
   * @param height:         Height of the view
   * @param numRows:        Number of rows in the grid
   * @param numColumns:     Number of cells per row in the grid
   * @param mainController: The main controller of the program
   */
  public HexagonGridView(int width, int height, int numRows, int numColumns,
      MainController mainController) {
    super(width, height, numRows, numColumns, mainController);
  }

  @Override
  protected CellView[][] initializeGrid() {
    // I asked ChatGPT for assistance in writing this method
    // Create the grid
    CellView[][] grid = new CellView[getNumRows()][getNumColumns()];

    // Adjust hexagon width and height to better fit the grid
    double hexWidth = (double) getWidth() / (getNumColumns());  // Use 0.5 to account for horizontal spacing
    double hexHeight = (double) getHeight() / getNumRows();

    // Adjust the horizontal offset for better spacing and alignment
    double horizontalSpacing = hexWidth * 0.75; // Horizontal spacing
    double verticalSpacing = hexHeight * Math.sqrt(3) / 2.0; // Vertical spacing for hexagons

    // Initialize each cell in the grid as a HexagonCellView
    for (int row = 0; row < getNumRows(); row++) {
      for (int column = 0; column < getNumColumns(); column++) {
        // Calculate the x and y position for each hexagon in a honeycomb layout
        double x = column * horizontalSpacing;

        // Offset every other row to create the honeycomb pattern
        double y = row * verticalSpacing;

        // Adjust for odd column (row offset for honeycomb)
        if (column % 2 == 1) {
          y += verticalSpacing / 2; // Shift odd columns downward
        }

        // Create the hexagon cell and add it to the grid
        HexagonCellView cell = new HexagonCellView(x, y, hexWidth, hexHeight);
        grid[row][column] = cell;
      }
    }

    return grid;
  }



  @Override
  protected int getRow(double x) {
    return 0; // TODO: implement
  }

  @Override
  protected int getColumn(double y) {
    return 0; // TODO: implement
  }

}
