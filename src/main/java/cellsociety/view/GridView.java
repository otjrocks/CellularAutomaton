package cellsociety.view;

import cellsociety.view.CellView;
import javafx.scene.Group;
import javafx.scene.paint.Paint;

/**
 * A view with all the cells in the grid and ability to update individual cells based on their
 * location
 */
public class GridView extends Group {

  private final int myNumRows;
  private final int myNumColumns;
  private final int cellWidth;
  private final int cellHeight;
  private final CellView[][] myGrid;

  /**
   * Create a grid view
   * @param width: Width of the view
   * @param height: Height of the view
   * @param numRows: Number of rows in the grid
   * @param numColumns: Number of cells per row in the grid
   */
  public GridView(int width, int height, int numRows, int numColumns) {
    myNumRows = numRows;
    myNumColumns = numColumns;
    cellWidth = width / numColumns;
    cellHeight = height / numRows;
    myGrid = new CellView[myNumRows][myNumColumns];
    initializeGrid();
  }

  public void setColor(int row, int col, Paint color) {
    myGrid[row][col].setFill(color);
  }

  private void initializeGrid() {
    for (int row = 0; row < myNumRows; row++) {
      for (int column = 0; column < myNumColumns; column++) {
        CellView cellView = new CellView(cellWidth * column, cellHeight * row, cellWidth,
            cellHeight);
        this.getChildren().add(cellView);
        myGrid[row][column] = cellView;
      }
    }
  }
}
