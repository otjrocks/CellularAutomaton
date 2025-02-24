package cellsociety.view.grid;

import cellsociety.controller.MainController;
import cellsociety.view.cell.CellView;
import cellsociety.view.cell.TriangleCellView;

public class TriangleGridView extends GridView {

  /**
   * Create a grid view
   *
   * @param width          :      Width of the view
   * @param height         :     Height of the view
   * @param numRows        :    Number of rows in the grid
   * @param numColumns     : Number of cells per row in the grid
   * @param mainController : the main controller of the program
   */
  public TriangleGridView(int width, int height, int numRows, int numColumns,
      MainController mainController) {
    super(width, height, numRows, numColumns, mainController);
  }

  @Override
  protected CellView[][] initializeGrid() {
    // I asked ChatGPT for assistance in arranging the isometric triangles into a grid pattern
    CellView[][] grid = new CellView[getNumRows()][getNumColumns()];
    for (int row = 0; row < getNumRows(); row++) {
      for (int col = 0; col < getNumColumns(); col++) {
        double x = col * (getCellWidth() / 2);  // Half width to stagger triangles
        double y = row * getCellHeight();
        TriangleCellView triangle = new TriangleCellView(x, y, getCellWidth(), getCellHeight());
        if ((row + col) % 2 == 1) { // rotate triangle upside down every other column
          triangle.setRotate(180);
        }
        grid[row][col] = triangle;
      }
    }
    return grid;
  }

  private double getCellWidth() {
    return getWidth() / Math.ceil((getNumColumns() + 1) * 0.5);
  }

  private double getCellHeight() {
    return (double) getHeight() / getNumRows();
  }
}
