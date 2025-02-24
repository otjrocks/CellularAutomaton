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
    // I asked ChatGPT to help with the configuration of this grid
    // Create the grid
    CellView[][] grid = new CellView[getNumRows()][getNumColumns()];

    // Adjust hexagon width to utilize the entire grid width
    double hexWidth =
        getWidth() / (getNumColumns() + 0.5); // Use 0.5 to account for horizontal spacing
    double hexHeight = (double) getHeight() / getNumRows();

    // Initialize each cell in the grid as a HexagonCellView
    for (int row = 0; row < getNumRows(); row++) {
      for (int column = 0; column < getNumColumns(); column++) {
        // Calculate the x and y position for each hexagon in a honeycomb layout
        double x = column * hexWidth * 0.75; // Horizontal spacing (0.75x width between cells)
        double y = row * hexHeight * Math.sqrt(3) / 2.0; // Vertical spacing based on height

        // Offset every other row to create a honeycomb pattern
        if (column % 2 == 1) {
          y += Math.round(hexHeight * Math.sqrt(3) / 4.0); // Shift odd columns downward
        }

        // Create the hexagon cell
        HexagonCellView cell = new HexagonCellView((int) x, (int) y, (int) hexWidth,
            (int) hexHeight);
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
