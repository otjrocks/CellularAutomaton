package cellsociety.view;

import cellsociety.controller.MainController;
import cellsociety.controller.ViewController;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.Grid;
import cellsociety.model.simulation.Simulation;
import cellsociety.view.config.StateDisplayConfig;
import cellsociety.view.config.StateInfo;
import java.util.List;
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

  /**
   * Create a simulation view
   *
   * @param width:      width of simulation view
   * @param height:     height of simulation view
   * @param grid:       initial grid of the simulation view
   * @param simulation: initial simulation of the simulation view
   */
  public SimulationView(int width, int height, int numRows, int numCols, Grid grid,
      Simulation simulation, ViewController viewController) {
    myGridView = new GridView(width, height, numRows, numCols, viewController);
    mySimulation = simulation;
    initializeInitialGridStates(numRows, numCols, grid);
    getChildren().add(myGridView);
  }

  /**
   * Perform a single step of the animation
   * @param grid: Grid of the simulation
   * @param simulation: Simulation that contains rules for updating
   */
  public void step(Grid grid, Simulation simulation) {
    List<CellUpdate> stateUpdates = grid.updateGrid(simulation);
    for (CellUpdate stateUpdate : stateUpdates) {
      int nextState = stateUpdate.getState();
      StateInfo nextStateInfo = StateDisplayConfig.getStateInfo(mySimulation, nextState);
      Paint nextColor = nextStateInfo.color();
      myGridView.setColor(stateUpdate.getRow(), stateUpdate.getCol(), nextColor);
    }
  }

  public void setColor(int row, int col, Paint color) {
    myGridView.setColor(row, col, color);
  }

  private void initializeInitialGridStates(int numRows, int numCols, Grid grid) {
    for (int row = 0; row < numRows; row++) {
      for (int col = 0; col < numCols; col++) {
        int nextState = grid.getCell(row, col).getState();
        Color nextColor = StateDisplayConfig.getStateInfo(mySimulation, nextState).color();
        myGridView.setColor(row, col, nextColor);
      }
    }
  }

  /**
   * Handle whether grid lines should be shown or not
   *
   * @param selected: Whether to show grid lines
   */
  public void setGridLines(boolean selected) {
    myGridView.setGridLines(selected);
  }
}
