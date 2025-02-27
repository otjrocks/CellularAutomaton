package cellsociety.view.grid;

import static cellsociety.config.MainConfig.getMessage;
import cellsociety.controller.MainController;
import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.view.cell.CellView;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;

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
    addGridElementsToGroupAndSetEventHandlers(mainController);
    this.setId("gridView");
  }

  /**
   * Initialize the grid's cells array. This method should handle the placement of cell views based
   * on their shape. For example, this method for a triangular grid should stagger isometric
   * triangles flipping every other column's triangle by 180 degrees.
   *
   * @return A 2D array of cell views representing the grid.
   */
  protected abstract CellView[][] initializeGrid();

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

  private void addGridElementsToGroupAndSetEventHandlers(MainController mainController) {
    for (int row = 0; row < myNumRows; row++) {
      for (int column = 0; column < myNumColumns; column++) {
        CellView cell = myGrid[row][column];
        this.getChildren().add(cell);
        int cellRow = row; // create copy of local variables to ensure that they are passed properly to main controller
        int cellColumn = column;
        cell.setOnMouseClicked(e -> mainController.changeCellState(cellRow, cellColumn));
      }
    }
  }

  public void addCellTooltip(int row, int col, Grid grid, String simType) {
    Shape cellShape = myGrid[row][col].getShape();
    
    Tooltip tooltip = new Tooltip();
    Tooltip.install(cellShape, tooltip);
    
    // Update tooltip dynamically when the user hovers
    cellShape.setOnMouseEntered(event ->  {
      Cell cell = grid.getCell(row, col);
      tooltip.setText("State: " + getMessage((simType + "_NAME_" + cell.getState()).toUpperCase()));
    });
  }
}
