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

  private final GridView myGridView;
  private final Simulation mySimulation;
  private int myIterationCount = 0;

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
    myGridView = GridViewFactory.createCellView(cellShapeType, width,
        height, numRows, numCols, mainController);
    mySimulation = simulation;
    initializeInitialGridStates(numRows, numCols, grid);
    getChildren().add(myGridView);
    myGridView.updateGridLinesColor(); // ensure grid lines are proper color on simulation view initialization
  }

  /**
   * For testing, get the grid view for this simulation
   *
   * @return A grid view object
   */
  GridView getGridView() {
    return myGridView;
  }

  /**
   * Perform a single step of the animation
   *
   * @param grid:       Grid of the simulation
   * @param simulation: Simulation that contains rules for updating
   */
  public void step(Grid grid, Simulation simulation) {
    myIterationCount++;
    ViewModeView.updateIterationCounter(myIterationCount);
    List<CellUpdate> stateUpdates = grid.updateGrid(simulation);
    for (CellUpdate stateUpdate : stateUpdates) {
      int nextState = stateUpdate.getState();
      StateInfo nextStateInfo = StateDisplayConfig.getStateInfo(mySimulation, nextState);
      Paint nextColor = nextStateInfo.color();
      double nextOpacity = stateUpdate.getNextCell().getOpacity();
      myGridView.setColor(stateUpdate.getRow(), stateUpdate.getCol(), nextColor);
      myGridView.setOpacity(stateUpdate.getRow(), stateUpdate.getCol(), nextOpacity);
    }
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
    myGridView.setGridLines(selected);
  }

  /**
   * Reset the grid line colors on theme change
   */
  public void updateGridLinesColor() {
    myGridView.updateGridLinesColor();
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
}
