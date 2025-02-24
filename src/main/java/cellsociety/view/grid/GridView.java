package cellsociety.view.grid;

import cellsociety.controller.MainController;
import cellsociety.view.cell.CellView;
import javafx.scene.Group;
import javafx.scene.paint.Paint;

/**
 * A view with all the cells in the grid and ability to update individual cells based on their
 * location
 *
 * @author Owen Jennings
 */
public abstract class GridView extends Group {

  private final int myNumRows;
  private final int myNumColumns;
  private final int myWidth;
  private final int myHeight;
  private final CellView[][] myGrid;

  /**
   * Create a grid view
   *
   * @param width:         Width of the view
   * @param height:        Height of the view
   * @param numRows:       Number of rows in the grid
   * @param numColumns:    Number of cells per row in the grid
   * @param mainController : Main controller of the program
   */
  public GridView(int width, int height, int numRows, int numColumns,
      MainController mainController) {
    myNumRows = numRows;
    myNumColumns = numColumns;
    myWidth = width;
    myHeight = height;
    myGrid = initializeGrid();
    for (int row = 0; row < myNumRows; row++) {
      for (int column = 0; column < myNumColumns; column++) {
        CellView cell = myGrid[row][column];
        this.getChildren().add(cell);
        int cellRow = row;
        int cellColumn = column;
        cell.setOnMouseClicked(event -> mainController.changeCellState(cellRow, cellColumn));
      }
    }
    this.setId("gridView");
  }

  /**
   * For testing, get the cell view at the provided coordinate
   *
   * @param row The row you are querying
   * @param col The column you are querying
   * @return A cell view, if it exists
   */
  public CellView getCell(int row, int col) {
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

  /**
   * Get number of rows in grid
   *
   * @return int representing number of rows
   */
  public int getNumRows() {
    return myNumRows;
  }

  /**
   * Get number of columns in grid
   *
   * @return int representing number of columns
   */
  public int getNumColumns() {
    return myNumColumns;
  }

  /**
   * Get the width of this grid
   *
   * @return The width of the grid
   */
  public int getWidth() {
    return myWidth;
  }

  /**
   * Get the height of this grid
   *
   * @return The height of the grid
   */
  public int getHeight() {
    return myHeight;
  }

  protected abstract CellView[][] initializeGrid();

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
