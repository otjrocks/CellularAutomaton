package cellsociety.view;

import java.util.List;

import cellsociety.controller.MainController;
import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.simulation.Simulation;
import cellsociety.view.config.StateDisplayConfig;
import cellsociety.view.config.StateInfo;
import cellsociety.view.grid.GridView;
import cellsociety.view.grid.GridViewFactory;
import cellsociety.view.grid.GridViewFactory.CellShapeType;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Simulation view for the visualizing the simulation
 *
 * @author Owen Jennings
 */
public class SimulationView extends Group {

  private GridView myGridView;
  private final Simulation mySimulation;
  private final int myWidth;
  private final int myHeight;
  private final int myNumRows;
  private final int myNumColumns;
  private final MainController myMainController;
  private boolean myGridLinesEnabled = true;

  /**
   * Create a simulation view
   *
   * @param width:         width of simulation view
   * @param height:        height of simulation view
   * @param grid:          initial grid of the simulation view
   * @param cellShapeType: the type of the cell shape to create a grid with
   * @param simulation:    initial simulation of the simulation view
   */
  public SimulationView(int width, int height, int numRows, int numCols, Grid grid,
      CellShapeType cellShapeType, Simulation simulation, MainController mainController) {
    myWidth = width;
    myHeight = height;
    myNumRows = numRows;
    myNumColumns = numCols;
    myMainController = mainController;
    mySimulation = simulation;
    initializeGrid(width, height, numRows, numCols, grid, cellShapeType, mainController);
  }

  private void initializeGrid(int width, int height, int numRows, int numCols, Grid grid,
      CellShapeType cellShapeType, MainController mainController) {
    myGridView = GridViewFactory.createCellView(cellShapeType, width,
        height, numRows, numCols, mainController);
    initializeInitialGridStates(numRows, numCols, grid);
    getChildren().add(myGridView);
    myGridView.updateGridLinesColor(); // ensure grid lines are proper color on simulation view initialization
  }

  /**
   * Perform a single step of the animation
   *
   * @param grid:       Grid of the simulation
   * @param simulation: Simulation that contains rules for updating
   */
  public void step(Grid grid, Simulation simulation) {
    List<CellUpdate> stateUpdates = grid.updateGrid(simulation);
    updateGridViewFromCellUpdateList(stateUpdates);
  }

  /**
   * Set the color of a cell in the grid
   *
   * @param row   The row of the cell you want to update
   * @param col   The column of the cell you want to update
   * @param color The new color you want to set
   */
  public void setColor(int row, int col, Paint color) {
    myGridView.setColor(row, col, color);
  }

  /**
   * Handle whether grid lines should be shown or not
   *
   * @param selected: Whether to show grid lines
   */
  public void setGridLines(boolean selected) {
    myGridLinesEnabled = selected;
    myGridView.setGridLines(selected);
  }

  /**
   * Reset the grid line colors on theme change
   */
  public void updateGridLinesColor() {
    myGridView.updateGridLinesColor();
  }

  /**
   * Update the grid to use a new CellShapeType shape
   *
   * @param currentGridState A list of cell updates representing the current grid state.
   * @param value            The new CellShapeType value.
   */
  public void updateGridShape(List<CellUpdate> currentGridState, CellShapeType value) {
    this.getChildren().remove(myGridView);
    myGridView = GridViewFactory.createCellView(value, myWidth,
        myHeight, myNumRows, myNumColumns, myMainController);
    updateGridViewFromCellUpdateList(currentGridState);
    myGridView.updateGridLinesColor();
    myGridView.setGridLines(myGridLinesEnabled);
    this.getChildren().add(myGridView);
  }

  private void initializeInitialGridStates(int numRows, int numCols, Grid grid) {
    for (int row = 0; row < numRows; row++) {
      for (int col = 0; col < numCols; col++) {
        Cell nextCell = grid.getCell(row, col);
        int nextState = nextCell.getState();
        Color nextColor = StateDisplayConfig.getStateInfo(mySimulation, nextState).color();
        double nextOpacity = nextCell.getOpacity();
        myGridView.setColor(row, col, nextColor);
        myGridView.setOpacity(row, col, nextOpacity);
      }
    }
  }



  private void updateGridViewFromCellUpdateList(List<CellUpdate> stateUpdates) {
    for (CellUpdate stateUpdate : stateUpdates) {
      int nextState = stateUpdate.getState();
      StateInfo nextStateInfo = StateDisplayConfig.getStateInfo(mySimulation, nextState);
      Paint nextColor = nextStateInfo.color();
      double nextOpacity = stateUpdate.getNextCell().getOpacity();
      myGridView.setColor(stateUpdate.getRow(), stateUpdate.getCol(), nextColor);
      myGridView.setOpacity(stateUpdate.getRow(), stateUpdate.getCol(), nextOpacity);
    }
  }
}
