package cellsociety.view.grid;

import cellsociety.controller.MainController;
import cellsociety.view.cell.CellView;
import cellsociety.view.cell.RectangleCellView;

public class RectangleGridView extends GridView {

  private int cellWidth;
  private int cellHeight;

  /**
   * Create a grid view
   *
   * @param width          :      Width of the view
   * @param height         :     Height of the view
   * @param numRows        :    Number of rows in the grid
   * @param numColumns     : Number of cells per row in the grid
   * @param mainController : The main controller of this grid
   */
  public RectangleGridView(int width, int height, int numRows, int numColumns,
      MainController mainController) {
    super(width, height, numRows, numColumns, mainController);
  }

  @Override
  protected CellView[][] initializeGrid() {
    cellWidth = getWidth() / getNumColumns();
    cellHeight = getHeight() / getNumRows();
    CellView[][] grid = new CellView[getNumRows()][getNumColumns()];
    for (int row = 0; row < getNumRows(); row++) {
      for (int column = 0; column < getNumColumns(); column++) {
        CellView cellView = new RectangleCellView(cellWidth * column, cellHeight * row, cellWidth,
            cellHeight);
        grid[row][column] = cellView;
      }
    }
    return grid;
  }

  @Override
  protected int getRow(double y) {
    return (int) (y / cellHeight);
  }

  @Override
  protected int getColumn(double x) {
    return (int) (x / cellWidth);
  }
}
