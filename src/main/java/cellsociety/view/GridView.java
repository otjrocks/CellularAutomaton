package cellsociety.view;

import cellsociety.controller.MainController;
import cellsociety.view.cell.CellView;
import cellsociety.view.cell.HexagonCellView;
import cellsociety.view.cell.RectangleCellView;
import javafx.scene.Group;
import javafx.scene.paint.Paint;

/**
 * A view with all the cells in the grid and ability to update individual cells based on their
 * location
 *
 * @author Owen Jennings
 */
public class GridView extends Group {

  private final int myNumRows;
  private final int myNumColumns;
  private final int cellWidth;
  private final int cellHeight;
  private final CellView[][] myGrid;

  /**
   * Create a grid view
   *
   * @param width:      Width of the view
   * @param height:     Height of the view
   * @param numRows:    Number of rows in the grid
   * @param numColumns: Number of cells per row in the grid
   */
  public GridView(int width, int height, int numRows, int numColumns,
      MainController mainController) {
    myNumRows = numRows;
    myNumColumns = numColumns;
    cellWidth = width / numColumns;
    cellHeight = height / numRows;
    myGrid = new CellView[myNumRows][myNumColumns];
    initializeGrid();
    this.setId("gridView");
    this.setOnMouseClicked(event -> mainController.changeCellState(this.getRow(event.getY()),
        this.getColumn(event.getX())));
  }

  /**
   * For testing, get the cell view at the provided coordinate
   *
   * @param row The row you are querying
   * @param col The column you are querying
   * @return A cell view, if it exists
   */
  CellView getCell(int row, int col) {
    return myGrid[row][col];
  }

  /**
   * Set the color of a cell in the grid
   *
   * @param row:   row of cell
   * @param col:   column of cell
   * @param color: color you want to set
   */
  public void setColor(int row, int col, Paint color) {
    myGrid[row][col].setFill(color);
  }

  /**
   * Handle whether grid lines should be shown or not
   *
   * @param selected: Whether to show grid lines
   */
  public void setGridLines(boolean selected) {
    for (int row = 0; row < myNumRows; row++) {
      for (int col = 0; col < myNumColumns; col++) {
        myGrid[row][col].setGridLines(selected);
      }
    }
  }

  private void initializeGrid() {
    for (int row = 0; row < myNumRows; row++) {
      for (int column = 0; column < myNumColumns; column++) {
        CellView cellView = new RectangleCellView(cellWidth * column, cellHeight * row, cellWidth,
            cellHeight);
        this.getChildren().add(cellView);
        myGrid[row][column] = cellView;
      }
    }
  }

  private int getRow(double x) {
    return (int) (x / cellHeight);
  }

  private int getColumn(double y) {
    return (int) (y / cellWidth);
  }

  /**
   * Reset the grid line colors on theme change
   */
  public void updateGridLinesColor() {
    for (int row = 0; row < myNumRows; row++) {
      for (int column = 0; column < myNumColumns; column++) {
        myGrid[row][column].resetStrokeColor();
      }
    }
  }

  /**
   * Set the opacity of provided cell
   *
   * @param row         Row of cell
   * @param col         Column of cell
   * @param nextOpacity next opacity level for cell
   */
  public void setOpacity(int row, int col, double nextOpacity) {
    myGrid[row][col].setOpacity(nextOpacity);
  }
}
